package ece651.RISC.Server.Service;

import ch.qos.logback.core.joran.sanity.Pair;
import ece651.RISC.shared.*;

import java.util.*;

public class Round {
    private final Set<Player> operatedPlayers = new HashSet<>();
    private final ArrayList<MoveAction> moveActions = new ArrayList<>();
    private final ArrayList<AttackAction> attackActions = new ArrayList<>();
    private final ArrayList<Player> players;
    private final GameMap myMap;
    private final int resourceGrow;
    private final ArrayList<UpgradeTechAction> UpgradeTechAction = new ArrayList<>();
    private final ArrayList<UpgradeUnitAction> UpgradeUnitAction = new ArrayList<>();
    private final ArrayList<FormAllyAction> allyActions = new ArrayList<>();


    public Round(ArrayList<Player> players, GameMap map, int resourceGrow) {
        this.players = players;
        this.myMap = map;
        this.resourceGrow = resourceGrow;
    }

    public int playerOneTurn(Player player, ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions, ArrayList<UpgradeTechAction> UpgradeTechAction, ArrayList<UpgradeUnitAction> UpgradeUnitAction) {
        this.moveActions.addAll(moveActions);
        this.attackActions.addAll(attackActions);
        this.UpgradeTechAction.addAll(UpgradeTechAction);
        this.UpgradeUnitAction.addAll(UpgradeUnitAction);
        this.operatedPlayers.add(player);
        return operatedPlayers.size();
    }

    /**
     * do all the move action in the list
     */
    public void executeMoves(ArrayList<MoveAction> moveActions) {
        for (MoveAction move: moveActions) {
            //for evo3, need to change checker for move
            move.moveTerritory(myMap, move.getSourceTerritory().getId(), move.getTargetTerritory().getId());
            //for evo 2: deduct the food resource
            int resourceConsumed = -move.getHitUnits() * move.getSourceTerritory().getAccessibles().get(move.getTargetTerritory());
            move.getOwner().updateFoodResource(resourceConsumed);
        }
    }

    public ArrayList<AttackAction> parseAttacks(ArrayList<AttackAction> attackActions){
        //check the attack rule first
        //minus the deployed soldiers from the source territory first
        for(AttackAction a: attackActions){
            String checkAttack = a.getMyAC().checkAttackRule(a.getOwner(), a.getSourceTerritory(), a.getTargetTerritory(), a.getHitUnits());
            if(checkAttack == null){
                breakAlliance(a);
                a.getSourceTerritory().updateUnits(-a.getHitUnits());
                a.getOwner().updateFoodResource(-a.getHitUnits());
            }
            else{
                System.out.println(checkAttack);
                return null;
            }
        }
        ArrayList<AttackAction> parsed = new ArrayList<>();
        //use hashmap to group attack actions with the same target territory and the same owner
        LinkedHashMap<Map.Entry<Territory, Player>, ArrayList<AttackAction>> parsing = new LinkedHashMap<>();
        for(AttackAction a : attackActions){
            Map.Entry<Territory, Player> k = new AbstractMap.SimpleEntry<>(a.getTargetTerritory(), a.getOwner());
            if(!parsing.containsKey(k)){
                parsing.put(k, new ArrayList<>());
            }
            parsing.get(k).add(a);
        }
        //merge all the grouped attack actions
        Iterator<Map.Entry<Map.Entry<Territory, Player>, ArrayList<AttackAction>>> iterator = parsing.entrySet().iterator();
        while (iterator.hasNext()) {
            //for evo 2: not only changing the unit number, but also the number in arraylist myUnits
            ArrayList<Integer> myDeploy = new ArrayList<>(Collections.nCopies(7,0));
            Map.Entry<Map.Entry<Territory, Player>, ArrayList<AttackAction>> entry = iterator.next();
            int newUnits = 0;
            for(int j = 0; j < entry.getValue().size(); j++){
                newUnits += entry.getValue().get(j).getHitUnits();
                ArrayList<Integer> singleAttack = entry.getValue().get(j).getSourceTerritory().deployMyUnits(entry.getValue().get(j).getHitUnits());
                //add single attack order's units into myDeploy
                for(int k = 0; k < 7; ++k){
                    myDeploy.set(k, myDeploy.get(k) + singleAttack.get(k));
                }
            }
            //use the first source territory as the merged default source territory
            Territory newSourceTerritory = entry.getValue().get(0).getSourceTerritory();
            AttackAction merged = new AttackAction(newSourceTerritory, entry.getKey().getKey(), newUnits, Status.actionStatus.ATTACK, entry.getKey().getValue(), myDeploy);
            parsed.add(merged);
        }
        return parsed;
    }
    public void executeAttacks(ArrayList<AttackAction> attackActions) {
        Random rand = new Random();
        if(!attackActions.equals(null)){
            attackActions = parseAttacks(attackActions);
            while(attackActions.size() > 0){
                int order = rand.nextInt(attackActions.size());
                AttackAction attack = attackActions.get(order);
                attack.attackTerritoryEVO2(myMap, attack.getSourceTerritory().getId(), attack.getTargetTerritory().getId());
                attackActions.remove(order);
            }
        }
    }

    public void breakAlliance(AttackAction a){
        if(a.getSourceTerritory().getOwner().getAllyPlayer().getId() == a.getTargetTerritory().getOwnerId()){
            //if there's ally's units in the territory, send them back to their owner's territory
            for(Territory t1: a.getSourceTerritory().getOwner().getTerritories()){
                returnAllyUnits(t1);
            }
            for(Territory t2: a.getTargetTerritory().getOwner().getTerritories()){
                returnAllyUnits(t2);
            }
            //set both the players' ally player as null
            a.getSourceTerritory().getOwner().setAllyPlayer(null);
            a.getTargetTerritory().getOwner().setAllyPlayer(null);
        }
    }

    private void returnAllyUnits(Territory t) {
        if(t.getNumAllyUnits() > 0){
            //select one former ally's territory and send the units
            for(Territory dest: t.getAccessibles().keySet()){
                if(dest.getOwnerId() ==t.getAllyOwner().getId()){
                    ArrayList<Integer> returnUnits = t.getAllyUnits();
                    for(int i = 0; i < 7; ++i){
                        dest.updateAllyUnits(i, returnUnits.get(i));
                    }
                    break;
                }
            }
        }
    }

    public void executeUpgradeTech(ArrayList<UpgradeTechAction> UTAction){
        for(UpgradeTechAction uta: UTAction){
            for(int i = 0; i < players.size(); ++i){
                if(uta.getPlayerID() == players.get(i).getId()){
                    players.get(i).upgradeTechLevel();
                }
            }
        }
    }

    public void executeUpgradeUnit(ArrayList<UpgradeUnitAction> UUAction){
        for(UpgradeUnitAction a: UUAction){
            a.upgradeUnitLevel();
        }
    }


    // first traverse the allyActions list to see if there are two players who want to ally with each other
    // if there are, add them as each other's ally
    public void executeAllyActions(ArrayList<FormAllyAction> allyActions){
        for(FormAllyAction faa1 : allyActions) {
            for (FormAllyAction faa2 : allyActions) {
                if (faa1.getPlayer().getId() == faa2.getTargetPlayer().getId()
                        && faa1.getTargetPlayer().getId() == faa2.getPlayer().getId()
                        && faa1.getPlayer().getAllyPlayer() == null) {
                    faa1.formAlliance();
                }
            }
        }
    }

    public void naturalUnitIncrease(){
        for(Territory t: myMap.getTerritories()){
            t.updateUnits(1);
            t.updateMyUnits(0,1);
        }
    }

    public void naturalResourceIncrease() {
        for (Player p : players) {
            p.updateTechResource(resourceGrow * p.getTerritories().size());
            p.updateFoodResource(resourceGrow * p.getTerritories().size());
        }
    }

    public Status.gameStatus checkStatus(){
        //change how to determine win/lose
        //use a hashmap to store the grouped territories
        HashMap<Player, ArrayList<Territory>> sorted = new HashMap<>();
        for(Territory t : myMap.getTerritories()){
            if(!sorted.containsKey(t.getOwner())){
                sorted.put(t.getOwner(), new ArrayList<>());
            }
            sorted.get(t.getOwner()).add(t);
        }
        for(Player sp : players){
            if(!sorted.containsKey(sp)){
                sp.setStatus(Status.playerStatus.LOSE);
            }
            else if(sorted.get(sp).size() == myMap.getMapSize()){
                sp.setStatus(Status.playerStatus.WIN);
                return Status.gameStatus.FINISHED;
            }
        }
        return Status.gameStatus.PLAYING;
    }
    //play one turn of the game
    public Status.gameStatus playOneTurn() {
        executeUpgradeTech(UpgradeTechAction);
        executeUpgradeUnit(UpgradeUnitAction);
        executeMoves(moveActions);
        executeAttacks(attackActions);

        naturalUnitIncrease();
        naturalResourceIncrease();
        myMap.updateAccessible();
        myMap.setAccessibleIdsFromAccessible();
        return checkStatus();
    }
}

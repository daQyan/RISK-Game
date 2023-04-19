package ece651.RISC.Server.Service;

import ece651.RISC.shared.*;

import java.util.*;

public class Round {
    private Set<Player> operatedPlayers = new HashSet<>();
    private ArrayList<MoveAction> moveActions = new ArrayList<>();
    private ArrayList<AttackAction> attackActions = new ArrayList<>();
    private ArrayList<UpgradeTechAction> UTAction = new ArrayList<>();
    private ArrayList<UpgradeUnitAction> UUAction = new ArrayList<>();
    private ArrayList<Player> players;
    private GameMap myMap;
    private int resourceGrow;


    public Round(ArrayList<Player> players, GameMap map, int resourceGrow) {
        this.players = players;
        this.myMap = map;
        this.resourceGrow = resourceGrow;
    }

    public int playerOneTurn(Player player, ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) {
        this.moveActions.addAll(moveActions);
        this.attackActions.addAll(attackActions);
        this.operatedPlayers.add(player);
        return operatedPlayers.size();
    }

    /**
     * do all the move action in the list
     */
    public void executeMoves(ArrayList<MoveAction> moveActions) {
        for (MoveAction move: moveActions) {
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
        attackActions = parseAttacks(attackActions);
        while(attackActions.size() > 0){
            int order = rand.nextInt(attackActions.size());
            AttackAction attack = attackActions.get(order);
            attack.attackTerritoryEVO2(myMap, attack.getSourceTerritory().getId(), attack.getTargetTerritory().getId());
            attackActions.remove(order);
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
    public void naturalUnitIncrease(){
        for(Territory t: myMap.getTerritories()){
            t.updateUnits(1);
            t.updateMyUnits(0,1);
        }
    }

    //TODO implement this
    public void naturalResourceIncrease() {
        for (Player p : players) {
            p.updateTechResource(resourceGrow * p.getTerritories().size());
            p.updateFoodResource(resourceGrow * p.getTerritories().size());
        }
    }

    public Status.gameStatus checkStatus(){
        for(Player sp : players){
            if(sp.getTerritories().isEmpty()){
                sp.setStatus(Status.playerStatus.LOSE);
            }
            if(sp.getTerritories().size() == myMap.getMapSize()){
                sp.setStatus(Status.playerStatus.WIN);
                return Status.gameStatus.FINISHED;
            }
        }
        return Status.gameStatus.PLAYING;
    }
    //play one turn of the game
    public Status.gameStatus playOneTurn() {
        executeMoves(moveActions);
        executeAttacks(attackActions);
        executeUpgradeTech(UTAction);
        executeUpgradeUnit(UUAction);
        naturalUnitIncrease();
        naturalResourceIncrease();
        myMap.updateAccessible();
        return checkStatus();
    }
}

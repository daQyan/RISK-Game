package ece651.RISC.Server.Service;

import ece651.RISC.shared.AttackAction;
import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.MoveAction;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Status;
import ece651.RISC.shared.Territory;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Round {
    private Set<Player> operatedPlayers = new HashSet<>();
    private ArrayList<MoveAction> moveActions = new ArrayList<>();
    private ArrayList<AttackAction> attackActions = new ArrayList<>();
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
            move.moveTerritory();
        }
    }
    public ArrayList<AttackAction> parseAttacks(ArrayList<AttackAction> attackActions){
        //check the attack rule first
        //minus the deployed soldiers from the source territory first
        for(AttackAction a: attackActions){
            String checkAttack = a.getMyAC().checkAttackRule(a.getOwner(), a.getSourceTerritory(), a.getTargetTerritory(), a.getHitUnits());
            if(checkAttack == null){
                a.getSourceTerritory().updateUnits(-a.getHitUnits());
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
            Map.Entry<Map.Entry<Territory, Player>, ArrayList<AttackAction>> entry = iterator.next();
            int newUnits = 0;
            for(int j = 0; j < entry.getValue().size(); j++){
                newUnits += entry.getValue().get(j).getHitUnits();
            }
            //use the first source territory as the merged default source territory
            Territory newSourceTerritory = entry.getValue().get(0).getSourceTerritory();
            AttackAction merged = new AttackAction(newSourceTerritory, entry.getKey().getKey(), newUnits, Status.actionStatus.ATTACK, entry.getKey().getValue());
            parsed.add(merged);
        }
        return parsed;
    }
    public void executeAttacks(ArrayList<AttackAction> attackActions) {
        Random rand = new Random();
        attackActions = parseAttacks(attackActions);
        // attackActions 为null 就不能.size()了
        if(attackActions == null) return;
        while(attackActions.size() > 0){
            int order = rand.nextInt(attackActions.size());
            String result = attackActions.get(order).attackTerritory();
            attackActions.remove(order);
        }
    }

    public void naturalUnitIncrease(){
        for(Territory t: myMap.getTerritories()){
            t.updateUnits(1);
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
        naturalUnitIncrease();
        return checkStatus();
    }
}

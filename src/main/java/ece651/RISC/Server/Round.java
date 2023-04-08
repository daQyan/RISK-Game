package ece651.RISC.Server;

import ece651.RISC.shared.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Round {
    private Set<Player> operatedPlayers = new HashSet<>();
    private ArrayList<MoveAction> moveActions = new ArrayList<>();
    private ArrayList<AttackAction> attackActions = new ArrayList<>();
    private ArrayList<Player> players;
    private Server2Client server2Client;
    private GameMap myMap;
    public Round(ArrayList<Player> players, GameMap map, Server2Client server2Client) {
        this.players = players;
        this.myMap = map;
        this.server2Client = server2Client;
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

    public void executeAttacks(ArrayList<AttackAction> attackActions) {
        Random rand = new Random();
        while(attackActions.size() > 0){
            int order = rand.nextInt(attackActions.size());
            String result = attackActions.get(order).attackTerritory();
            if (result == "Owner Changed") {
                int newUnits = -attackActions.get(order).targetTerritory.getUnit();
                attackActions.get(order).targetTerritory.changeOwner(newUnits, attackActions.get(order).sourceTerritory.getOwner());
                // let player update its territory later
            }
            attackActions.remove(order);
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
        return checkStatus();
    }
}

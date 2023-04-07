package ece651.RISC.Server;

import ece651.RISC.shared.*;

import java.util.ArrayList;
import java.util.Random;

public class ServerGame {

    private ArrayList<Player> players;
    private GameMap myMap;
    private MapController myMapController;
    private MapFactory myMapFactory;
    private Status.gameStatus myStatus;

    private Server2Client server2Client;

    public ServerGame(ArrayList<Player> players, GameMap myMap, Server2Client server2Client) {
        this.players = players;
        this.myMap = myMap;
        this.myStatus = Status.gameStatus.PLAYING;
        this.myMapFactory = new MapFactory();
        this.myMapController= new MapController(myMap);
        this.server2Client = server2Client;
    }


//    public void addMove(Territory sourceTerritory, Territory targetTerritory, int moveUnits) {
//        // check moveUnits valid
//        MoveAction move = new MoveAction(sourceTerritory, targetTerritory, moveUnits, Status.actionStatus.MOVE, sourceTerritory.getOwner());
//        moveActions.add(move);
//
//    }
//
//    public void addAttack(Territory sourceTerritory, Territory targetTerritory, int hitUnits) {
//        // check hitUnits valid
//        AttackAction attack = new AttackAction(sourceTerritory, targetTerritory, hitUnits, Status.actionStatus.ATTACK, sourceTerritory.getOwner());
//        attackActions.add(attack);
//    }

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


    public void checkStatus(){
        for(Player sp : players){
            if(sp.getTerritories().isEmpty()){
                sp.setStatus(Status.playerStatus.LOSE);
            }
            if(sp.getTerritories().size() == myMap.getMapSize()){
                sp.setStatus(Status.playerStatus.WIN);
                this.myStatus = Status.gameStatus.FINISHED;
            }
        }
    }
    //play one turn of the game
    public void playOneTurn(ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions){
        executeMoves(moveActions);
        executeAttacks(attackActions);
        checkStatus();
        //send information to players for networked game
        server2Client.sendMap(myMap);
    }





}

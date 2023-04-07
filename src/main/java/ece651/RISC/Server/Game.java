package ece651.RISC.Server;

import ece651.RISC.Status;
import ece651.RISC.shared.Map;
import ece651.RISC.shared.Territory;

import java.util.ArrayList;

public class Game {

    private ArrayList<ServerPlayer> players;
    private Map myMap;
    private ArrayList<MoveAction> moveActions;
    private ArrayList<AttackAction> attackActions;
    private Status.gameStatus myStatus;

    public Game(ArrayList<ServerPlayer> players, Map myMap, ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) {
        this.players = players;
        this.myMap = myMap;
        this.moveActions = moveActions;
        this.attackActions = attackActions;
        this.myStatus = Status.gameStatus.PLAYING;
    }


    public void addMove(Territory sourceTerritory, Territory targetTerritory, int moveUnits) {
        // check moveUnits valid
        MoveAction move = new MoveAction(sourceTerritory, targetTerritory, moveUnits, Status.actionStatus.MOVE, sourceTerritory.getOwner());
        moveActions.add(move);

    }

    public void addAttack(Territory sourceTerritory, Territory targetTerritory, int hitUnits) {
        // check hitUnits valid
        AttackAction attack = new AttackAction(sourceTerritory, targetTerritory, hitUnits, Status.actionStatus.ATTACK, sourceTerritory.getOwner());
        attackActions.add(attack);
    }

    /**
     * do all the move action in the list
     */
    public void executeMoves() {
        for (MoveAction move: moveActions) {
            move.moveTerritory();
        }
    }

    public void executeAttacks() {
        for (AttackAction attack: attackActions) {
            String result = attack.attackTerritory();
            if (result == "Owner Changed") {
                int newUnits = -attack.targetTerritory.getNumUnits();
                attack.targetTerritory.setOwner(attack.sourceTerritory.getOwner());
                attack.targetTerritory.setNumUnits(newUnits);
                // let player update its territory later
            }
            attack.attackTerritory();
        }
    }

    public void checkStatus(){
        for(ServerPlayer p : players){
            if(p.getTerritories().isEmpty()){
                p.changeStatus(Status.playerStatus.LOSE);
            }
            if(p.getTerritories().size() == myMap.getMapSize()){
                p.changeStatus(Status.playerStatus.WIN);
                this.myStatus = Status.gameStatus.FINISHED;
            }
        }
    }
    //play one turn of the game
    public void playOneTurn(){
        executeMoves();
        executeAttacks();
        checkStatus();
        if(this.myStatus.equals(Status.gameStatus.FINISHED)){

        }
    }

    public void playGame(){
        //initialize the game with players

        //play the game until having a winner
        while(!this.myStatus.equals(Status.gameStatus.FINISHED)){
            playOneTurn();
        }
    }

}

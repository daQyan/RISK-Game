package ece651.RISC;

import java.util.ArrayList;

public class Game {

    private ArrayList<Player> players;
    private Map myMap;
    private ArrayList<MoveAction> moveActions;
    private ArrayList<AttackAction> attackActions;
    private Status.gameStatus myStatus;

    public Game(ArrayList<Player> players, Map myMap, ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) {
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
                int newUnits = -attack.targetTerritory.getUnit();
                attack.targetTerritory.changeOwner(newUnits, attack.sourceTerritory.getOwner());
                // let player update its territory later
            }
            attack.attackTerritory();
        }
    }

    public void checkStatus(){
        for(Player p : players){
            if(p.getMyTerritory().isEmpty()){
                p.changeStatus(Status.playerStatus.LOSE);
            }
            if(p.getMyTerritory().size() == myMap.getMapSize()){
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
        //initialize the game
        while(!this.myStatus.equals(Status.gameStatus.FINISHED)){
            playOneTurn();
        }
    }



}

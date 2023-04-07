package ece651.RISC.Server;

import ece651.RISC.Status;
import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.MapController;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Territory;

import java.util.ArrayList;
import java.util.Random;

public class Game {

    private ArrayList<Player> players;
    private GameMap myMap;
    private MapController myMapController;
    private MapFactory myMapFactory;
    private ArrayList<MoveAction> moveActions;
    private ArrayList<AttackAction> attackActions;
    private Status.gameStatus myStatus;

    public Game(ArrayList<Player> players, GameMap myMap, ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) {
        this.players = players;
        this.myMap = myMap;
        this.moveActions = moveActions;
        this.attackActions = attackActions;
        this.myStatus = Status.gameStatus.PLAYING;
        this.myMapFactory = new MapFactory();
        this.myMapController= new MapController(myMap);
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
        //send information to players for networked game
    }

    public void playGame(){
        //initialize the game with players

        //play the game until having a winner
        while(!this.myStatus.equals(Status.gameStatus.FINISHED)){
            playOneTurn();
        }
        //server should monitor the game status and close sockets when the game is finished
    }




}

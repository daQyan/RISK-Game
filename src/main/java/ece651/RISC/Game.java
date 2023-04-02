package ece651.RISC;

import java.util.ArrayList;

public class Game {

    private ArrayList<Player> players;
    private Map myMap;
    private ArrayList<MoveAction> moveActions;
    private ArrayList<AttackAction> attackActions;

    public Game(ArrayList<Player> players, Map myMap, ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) {
        this.players = players;
        this.myMap = myMap;
        this.moveActions = moveActions;
        this.attackActions = attackActions;
    }


    public void addMove(Territory sourceTerritory, Territory targetTerritory, int moveUnits) {
        // check moveUnits valid
        MoveAction move = new MoveAction(sourceTerritory, targetTerritory, moveUnits);
        moveActions.add(move);

    }

    public void addAttack(Territory sourceTerritory, Territory targetTerritory, int hitUnits) {
        // check hitUnits valid
        AttackAction attack = new AttackAction(sourceTerritory, targetTerritory, hitUnits);
        attackActions.add(attack);
    }

    /**
     * do all the move action in the list
     */
    public void parseMoves() {
        for (MoveAction move: moveActions) {
            move.moveTerritory();
        }
    }

    public void parseAttacks() {
        for (AttackAction attack: attackActions) {
            attack.attackTerritory();
//            if (checkOwnership(attack.targetTerritory)){
//                updateOwnership(attack.targetTerritory);
//
//            }

        }
    }




}

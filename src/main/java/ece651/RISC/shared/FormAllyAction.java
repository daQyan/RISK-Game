package ece651.RISC.shared;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.ArrayList;

/**
 * This class represents a form ally action between two players.
 */
public class FormAllyAction {
    @JSONField(serialize = false, deserialize = false)
    private Player myPlayer;
    @JSONField(serialize = false, deserialize = false)
    private Player targetPlayer;

    /**
     * Constructor for the FormAllyAction class.
     * @param myPlayer The player who initiates the ally request.
     * @param targetPlayer The player who is requested to become an ally.
     */
    public FormAllyAction(Player myPlayer, Player targetPlayer) {
        this.myPlayer = myPlayer;
        this.targetPlayer = targetPlayer;
    }

    public void setMyPlayer(Player myPlayer) {
        this.myPlayer = myPlayer;
    }

    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    /**
     * Getter method for the initiating player.
     * @return The player who initiates the ally request.
     */
    public Player getMyPlayer() {
        return myPlayer;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    /**
     * Check the rules for forming an alliance.
     * @param player The initiating player.
     * @param targetPlayer The player who is requested to become an ally.
     * @return null if the action is valid, an error message if not.
     * @throws IllegalArgumentException if there are less than 3 players in the game, or if the initiating player
     * tries to form an alliance with himself/herself.
     */
    public void checkFormAllyRule(Player player, Player targetPlayer, int numPlayers) {
//        if (numPlayers < 3) {
//            throw new IllegalArgumentException("There are less than 3 players in the game, you cannot form ally");
//        }
        if (player.getId() == targetPlayer.getId()) {
            throw new IllegalArgumentException("You cannot form ally with yourself");
        }
    }

    // execute ally-action，update the ally list of both players
    public void formAlliance(ArrayList<Territory> territories, int numPlayers) {
        checkFormAllyRule(myPlayer, targetPlayer, numPlayers);
        myPlayer.addAlly(targetPlayer, territories);
//        targetPlayer.addAlly(myPlayer, territories);
    }
}

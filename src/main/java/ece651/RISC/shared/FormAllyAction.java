package ece651.RISC.shared;

import java.util.ArrayList;

public class FormAllyAction {
    private Player myPlayer;
    private Player targetPlayer;

    public FormAllyAction(Player myPlayer, Player targetPlayer) {
        this.myPlayer = myPlayer;
        this.targetPlayer = targetPlayer;
    }

    public Player getPlayer() {
        return myPlayer;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    // check at least 3 players in the game
    // check the target player is not the player himself
    public String checkFormAllyRule(Player player, Player targetPlayer) {
        if (player.getNumPlayers() < 3) {
            throw new IllegalArgumentException("There are less than 3 players in the game, you cannot form ally");
        }
        if (player.getId() == targetPlayer.getId()) {
            throw new IllegalArgumentException("You cannot form ally with yourself");
        }
        return null;
    }

    // execute ally-action，update the ally list of both players
    public void formAlliance(ArrayList<Territory> territories) {
        myPlayer.addAlly(targetPlayer, territories);
        targetPlayer.addAlly(myPlayer, territories);
    }
}

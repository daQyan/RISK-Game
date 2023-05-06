package ece651.RISC.shared;

import java.util.Random;

/**
 * The Combat class represents a combat system in the game.
 * It contains methods to roll dice to determine the outcome of a combat.
 */
public class Combat {
    private Random Dice;

    public Combat() {
        this.Dice = new Random();
    }

    /**
     * Rolls two 20-sided dice and compares their results.
     *
     * @return true if the player wins the combat, false otherwise.
     */
    public boolean rollCombatDice() {
        int myDice = Dice.nextInt(19) + 1;
        int enemyDice = Dice.nextInt(19) + 1;
        if (myDice > enemyDice) {
            return true;
        }
        return false;
    }

    /**
     * Rolls two 20-sided dice with bonuses and compares their results.
     *
     * @param myBonus    the bonus value added to the player's dice roll.
     * @param enemyBonus the bonus value added to the enemy's dice roll.
     * @return true if the player wins the combat, false otherwise.
     */
    public boolean rollCombatDice(int myBonus, int enemyBonus) {
        int myDice = Dice.nextInt(19) + 1 + myBonus;
        int enemyDice = Dice.nextInt(19) + 1 + enemyBonus;
        if (myDice > enemyDice) {
            return true;
        }
        return false;
    }
}

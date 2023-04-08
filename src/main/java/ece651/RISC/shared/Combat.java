package ece651.RISC.shared;

import java.util.Random;

public class Combat {
    private Random Dice;
    public boolean rollCombatDice(){
        int myDice = Dice.nextInt(19) + 1;
        int enemyDice = Dice.nextInt(19) + 1;
        if(myDice > enemyDice){
            return true;
        }
        return false;
    }
}

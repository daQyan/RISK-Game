package ece651.RISC.shared;

import java.util.ArrayList;
import java.util.Random;

public class Combat {
    private Random Dice;
    public Combat(){
        this.Dice  = new Random();
    }
    public boolean rollCombatDice(){
        int myDice = Dice.nextInt(19) + 1;
        int enemyDice = Dice.nextInt(19) + 1;
        if(myDice > enemyDice){
            return true;
        }
        return false;
    }
    public boolean rollCombatDice(int myBonus, int enemyBonus){
        int myDice = Dice.nextInt(19) + 1 + myBonus;
        int enemyDice = Dice.nextInt(19) + 1 + enemyBonus;
        if(myDice > enemyDice){
            return true;
        }
        return false;
    }
}

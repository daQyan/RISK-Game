package ece651.RISC.shared;

import java.util.ArrayList;

public class AttackAction extends Action {
    private Combat myCombat;
    private ArrayList<Integer> myUnits = new ArrayList<>();
    private ArrayList<Integer> enemyUnits = new ArrayList<>();
    //for source territory, the player would by default deploy his/her units from the highest unit to the lowest unit
    public AttackAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits, Status.actionStatus type, Player owner) {
        super(sourceTerritory, targetTerritory, hitUnits, type, owner);
        this.myCombat = new Combat();
        this.enemyUnits = targetTerritory.getMyUnits();
    }
    public AttackAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits, Status.actionStatus type, Player owner, ArrayList<Integer> myDeploy) {
        super(sourceTerritory, targetTerritory, hitUnits, type, owner);
        this.myCombat = new Combat();
        this.myUnits = myDeploy;
        this.enemyUnits = targetTerritory.getMyUnits();
    }

    public void moveOut(){
        sourceTerritory.updateUnits(-hitUnits);
    }

    /**
     * attack the territory from the chosen territory
     */
    public String attackTerritory() {
        String attackResult;
        while(hitUnits > 0 && targetTerritory.getNumUnits() > 0) {
            if (myCombat.rollCombatDice() == true) {
                targetTerritory.updateUnits(-1);
            } else {
                --hitUnits;
            }
        }
        // if the attack wins
        if (targetTerritory.getNumUnits() <= 0 && hitUnits > 0) {
            targetTerritory.setOwner(this.owner);
            targetTerritory.setNumUnits(hitUnits);
            attackResult = this.owner.getName() + " has taken over " + targetTerritory.getName() + "!";
        }
        else{
            attackResult = this.owner.getName() + " has failed in the attack on " + targetTerritory.getName() + "!";
        }
        return attackResult;
    }

    public String attackTerritoryEVO2(){
        String attackResult;
        while(hitUnits > 0 && targetTerritory.getNumUnits() > 0) {
            if (myCombat.rollCombatDice() == true) {
                targetTerritory.updateUnits(-1);
            } else {
                --hitUnits;
            }
        }
        // if the attack wins
        if (targetTerritory.getNumUnits() <= 0 && hitUnits > 0) {
            targetTerritory.setOwner(this.owner);
            targetTerritory.setNumUnits(hitUnits);
            attackResult = this.owner.getName() + " has taken over " + targetTerritory.getName() + "!";
        }
        else{
            attackResult = this.owner.getName() + " has failed in the attack on " + targetTerritory.getName() + "!";
        }
        return attackResult;
    }
}

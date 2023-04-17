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
        int minSelf = 0, minEnemy = 0;
        int maxSelf = 6, maxEnemy = 6;
        findStartingUnit(minSelf, minEnemy, maxSelf,maxEnemy);
        int myIndex = maxSelf;
        int enemyIndex = minEnemy;
        while(hitUnits > 0 && targetTerritory.getNumUnits() > 0 && minSelf <= 6 && minEnemy <= 6 && maxSelf >= 0 && maxEnemy >= 0) {
            if (myCombat.rollCombatDice(myIndex, enemyIndex) == true) {
                targetTerritory.updateUnits(-1);
                targetTerritory.updateMyUnits(enemyIndex, -1);
            } else {
                --hitUnits;
                this.myUnits.set(myIndex, myUnits.get(myIndex) - 1);
            }
            switchOrderAndUpdate(myIndex, enemyIndex, minSelf, minEnemy, maxSelf, maxEnemy);
        }
        // if the attack wins
        if (targetTerritory.getNumUnits() <= 0 && hitUnits > 0) {
            targetTerritory.setOwner(this.owner);
            targetTerritory.setNumUnits(hitUnits);
            targetTerritory.setMyUnits(myUnits);
            attackResult = this.owner.getName() + " has taken over " + targetTerritory.getName() + "!";
        }
        else{
            attackResult = this.owner.getName() + " has failed in the attack on " + targetTerritory.getName() + "!";
        }
        return attackResult;
    }

    private void findStartingUnit(int minSelf, int minEnemy, int maxSelf, int maxEnemy){
        while(minSelf <= 6){
            if(myUnits.get(minSelf) > 0){
                break;
            }
            ++minSelf;
        }
        while(minEnemy <= 6){
            if(myUnits.get(minEnemy) > 0){
                break;
            }
            ++minEnemy;
        }
        while(maxSelf >= 0){
            if(myUnits.get(maxSelf) > 0){
                break;
            }
            --maxSelf;
        }
        while(maxEnemy >= 0){
            if(myUnits.get(maxEnemy) > 0){
                break;
            }
            --maxEnemy;
        }
    }
    private void switchOrderAndUpdate(int myIndex, int enemyIndex, int minSelf, int minEnemey, int maxSelf, int maxEnemy){
        if(myIndex == maxSelf && enemyIndex == minEnemey){
            //update the starting point
            findStartingUnit(minSelf, minEnemey, maxSelf, maxEnemy);
            myIndex = minSelf;
            enemyIndex = maxEnemy;
        }
        else if(myIndex == minSelf && enemyIndex == maxEnemy){
            findStartingUnit(minSelf, minEnemey, maxSelf, maxEnemy);
            myIndex = maxSelf;
            enemyIndex = minEnemey;
        }

    }
}

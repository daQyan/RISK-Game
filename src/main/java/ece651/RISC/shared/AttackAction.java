package ece651.RISC.shared;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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
    public String attackTerritory(GameMap gameMap, int sourceTerritoryId, int targetTerritoryId) {
        targetTerritory = gameMap.getTerritory(targetTerritoryId);
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
        AtomicInteger minSelf = new AtomicInteger(0);
        AtomicInteger minEnemy = new AtomicInteger(0);
        AtomicInteger maxSelf = new AtomicInteger(6);
        AtomicInteger maxEnemy = new AtomicInteger(6);
        findStartingUnit(minSelf, minEnemy, maxSelf, maxEnemy);
        AtomicInteger myIndex = new AtomicInteger(maxSelf.get());
        AtomicInteger enemyIndex = new AtomicInteger(minEnemy.get());
        while(hitUnits > 0 && targetTerritory.getNumUnits() > 0 && minSelf.get() <= 6 && minEnemy.get() <= 6 && maxSelf.get() >= 0 && maxEnemy.get() >= 0) {
            Unit myUnit = new Unit();
            if (myCombat.rollCombatDice(myUnit.getBonusByType(myIndex.get()), myUnit.getBonusByType(enemyIndex.get())) == true) {
                targetTerritory.updateUnits(-1);
                targetTerritory.updateMyUnits(enemyIndex.get(), -1);
                //enemyUnits.set(enemyIndex.get(), enemyUnits.get(enemyIndex.get()) - 1);
            } else {
                --hitUnits;
                this.myUnits.set(myIndex.get(), myUnits.get(myIndex.get()) - 1);
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
    //problem here
    private void findStartingUnit(AtomicInteger minSelf, AtomicInteger minEnemy, AtomicInteger maxSelf, AtomicInteger maxEnemy){
        while(minSelf.get() <= 6){
            if(myUnits.get(minSelf.get()) > 0){
                break;
            }
            minSelf.set(minSelf.get() + 1);
        }
        while(minEnemy.get() <= 6){
            if(enemyUnits.get(minEnemy.get()) > 0){
                break;
            }
            minEnemy.set(minEnemy.get() + 1);
        }
        while(maxSelf.get() >= 0){
            if(myUnits.get(maxSelf.get()) > 0){
                break;
            }
            maxSelf.set(maxSelf.get() - 1);
        }
        while(maxEnemy.get() >= 0){
            if(enemyUnits.get(maxEnemy.get()) > 0){
                break;
            }
            maxEnemy.set(maxEnemy.get() - 1);
        }
    }
    private void switchOrderAndUpdate(AtomicInteger myIndex, AtomicInteger enemyIndex, AtomicInteger minSelf, AtomicInteger minEnemy, AtomicInteger maxSelf, AtomicInteger maxEnemy){
        if(myIndex.get() == maxSelf.get() && enemyIndex.get() == minEnemy.get()){
            //update the starting point
            findStartingUnit(minSelf, minEnemy, maxSelf, maxEnemy);
            myIndex.set(minSelf.get());
            enemyIndex.set(maxEnemy.get());
        }
        else if(myIndex.get() == minSelf.get() && enemyIndex.get() == maxEnemy.get()){
            findStartingUnit(minSelf, minEnemy, maxSelf, maxEnemy);
            myIndex.set(maxSelf.get());
            enemyIndex.set(minEnemy.get());
        }

    }
}

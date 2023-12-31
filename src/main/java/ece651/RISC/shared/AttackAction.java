package ece651.RISC.shared;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * AttackAction class represents an attack action that a player can perform on a territory owned by another player.
 * It contains methods to attack a territory and update the game map accordingly.
 * It extends the Action class and implements the Serializable interface.
 */

public class AttackAction extends Action {
    private final Combat myCombat;
    private ArrayList<Integer> myUnits = new ArrayList<>();
    private final ArrayList<Integer> enemyUnits;

    /**
     * Constructor for an AttackAction object with default unit deployment (highest unit to lowest unit).
     *
     * @param sourceTerritory the territory launching the attack
     * @param targetTerritory the territory being attacked
     * @param hitUnits        the number of units attacking
     * @param type            the status of the action (SUCCESS or FAILED)
     * @param owner           the player performing the attack action
     */

    //for source territory, the player would by default deploy his/her units from the highest unit to the lowest unit
    public AttackAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits, Status.actionStatus type, Player owner) {
        super(sourceTerritory, targetTerritory, hitUnits, type, owner);
        this.myCombat = new Combat();
        this.enemyUnits = targetTerritory.getMyUnits();
    }

    /**
     * Constructor for an AttackAction object with customized unit deployment.
     *
     * @param sourceTerritory the territory launching the attack
     * @param targetTerritory the territory being attacked
     * @param hitUnits        the number of units attacking
     * @param type            the status of the action (SUCCESS or FAILED)
     * @param owner           the player performing the attack action
     * @param myDeploy        the list of units deployed by the attacking player, from the highest unit to the lowest unit
     */
    public AttackAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits, Status.actionStatus type, Player owner, ArrayList<Integer> myDeploy) {
        super(sourceTerritory, targetTerritory, hitUnits, type, owner);
        this.myCombat = new Combat();
        this.myUnits = myDeploy;
        this.enemyUnits = targetTerritory.getMyUnits();
    }

    public void moveOut() {
        sourceTerritory.updateUnits(-hitUnits);
    }

    /**
     * Attack the target territory from the source territory.
     *
     * @param gameMap           the game map containing all territories
     * @param sourceTerritoryId the ID of the source territory
     * @param targetTerritoryId the ID of the target territory
     * @return the result of the attack as a string
     */
    public String attackTerritory(GameMap gameMap, int sourceTerritoryId, int targetTerritoryId) {
        targetTerritory = gameMap.getTerritory(targetTerritoryId);
        String attackResult;
        while(hitUnits > 0 && targetTerritory.getNumUnits() > 0) {
            if (myCombat.rollCombatDice()) {
                targetTerritory.updateUnits(-1);
            } else {
                --hitUnits;
            }
        }
        // if the attack wins
        if (targetTerritory.getNumUnits() <= 0 && hitUnits > 0) {
            targetTerritory.setOwner(this.owner);
            targetTerritory.setOwnerId(this.owner.getId());
            targetTerritory.setNumUnits(hitUnits);
            attackResult = this.owner.getName() + " has taken over " + targetTerritory.getName() + "!";
        } else {
            attackResult = this.owner.getName() + " has failed in the attack on " + targetTerritory.getName() + "!";
        }
        return attackResult;
    }

    public String attackTerritoryEVO2(GameMap gameMap, int sourceTerritoryId, int targetTerritoryId){
        //attack should not happen between two allies here as we have checked it in round's parseAttack
        if(gameMap.getTerritory(sourceTerritoryId).getAllyOwner() != -1 && gameMap.getTerritory(sourceTerritoryId).getAllyOwner() == gameMap.getTerritory(targetTerritoryId).getOwnerId()){
            //move myUnits to targetTerritory
            for(int i = 0; i < 7; ++i){
                gameMap.getTerritory(targetTerritoryId).updateAllyUnits(i, myUnits.get(i));
            }
            gameMap.getTerritory(targetTerritoryId).updateAllyUnitsNum(hitUnits);
            return("Successfully moved to the designated ally's territory");
        }
        String attackResult;
        AtomicInteger minSelf = new AtomicInteger(0);
        AtomicInteger minEnemy = new AtomicInteger(0);
        AtomicInteger maxSelf = new AtomicInteger(6);
        AtomicInteger maxEnemy = new AtomicInteger(6);
        findStartingUnit(minSelf, minEnemy, maxSelf, maxEnemy);
        AtomicInteger myIndex = new AtomicInteger(maxSelf.get());
        AtomicInteger enemyIndex = new AtomicInteger(minEnemy.get());
        while(hitUnits > 0 && gameMap.getTerritory(targetTerritoryId).getNumUnits() > 0 && minSelf.get() <= 6 && minEnemy.get() <= 6 && maxSelf.get() >= 0 && maxEnemy.get() >= 0) {
            Unit myUnit = new Unit();
            if (myCombat.rollCombatDice(myUnit.getBonusByType(myIndex.get()), myUnit.getBonusByType(enemyIndex.get()))) {
                gameMap.getTerritory(targetTerritoryId).updateUnits(-1);
                gameMap.getTerritory(targetTerritoryId).updateMyUnits(enemyIndex.get(), -1);
                //enemyUnits.set(enemyIndex.get(), enemyUnits.get(enemyIndex.get()) - 1);
            } else {
                --hitUnits;
                this.myUnits.set(myIndex.get(), myUnits.get(myIndex.get()) - 1);
            }
            switchOrderAndUpdate(myIndex, enemyIndex, minSelf, minEnemy, maxSelf, maxEnemy);
        }
        // if the attack wins
        if (gameMap.getTerritory(targetTerritoryId).getNumUnits() <= 0 && hitUnits > 0) {
            //if the target territory has an ally owner, change the ally owner's status
            if(gameMap.getTerritory(targetTerritoryId).getAllyOwner() != -1){
                //return the territory's ally's units back
                returnAllyUnits(gameMap.getTerritory(targetTerritoryId), gameMap);
                gameMap.getTerritory(targetTerritoryId).setNumAllyUnits(0);
                gameMap.getTerritory(targetTerritoryId).setAllyOwner(-1);
            }
            gameMap.getTerritory(targetTerritoryId).setOwner(this.owner);
            gameMap.getTerritory(targetTerritoryId).setOwnerId(this.owner.getId());
            gameMap.getTerritory(targetTerritoryId).setNumUnits(hitUnits);
            gameMap.getTerritory(targetTerritoryId).setMyUnits(myUnits);
            targetTerritory.setAllyOwner(this.sourceTerritory.getAllyOwner());
            attackResult = this.owner.getName() + " has taken over " + gameMap.getTerritory(targetTerritoryId).getName() + "!";
        }
        else{
            attackResult = this.owner.getName() + " has failed in the attack on " + gameMap.getTerritory(targetTerritoryId).getName() + "!";
        }
        return attackResult;
    }
    public void returnAllyUnits(Territory t, GameMap mp) {
        if(t.getNumAllyUnits() > 0){
            //select one former ally's territory and send the units
            for(Territory dest: mp.getTerritories()){
                if(dest.getOwnerId() ==t.getAllyOwner()){
                    ArrayList<Integer> returnUnits = t.getAllyUnits();
                    for(int i = 0; i < 7; ++i){
                        dest.updateMyUnits(i, returnUnits.get(i));
                        t.updateAllyUnits(i, -1 * returnUnits.get(i));
                    }
                    dest.updateUnits(t.getNumAllyUnits());
                    break;
                }
            }
        }
    }

    /**
     * Finds the starting unit boundaries for attacking in EVO2 version.
     *
     * @param minSelf  the lowest index of non-zero value for the attacker's units
     * @param minEnemy the lowest index of non-zero value for the defender's units
     * @param maxSelf  the highest index of non-zero value for the attacker's units
     * @param maxEnemy the highest index of non-zero value for the defender's units
     */

    private void findStartingUnit(AtomicInteger minSelf, AtomicInteger minEnemy, AtomicInteger maxSelf, AtomicInteger maxEnemy) {
        while (minSelf.get() <= 6) {
            if (myUnits.get(minSelf.get()) > 0) {
                break;
            }
            minSelf.set(minSelf.get() + 1);
        }
        while (minEnemy.get() <= 6) {
            if (enemyUnits.get(minEnemy.get()) > 0) {
                break;
            }
            minEnemy.set(minEnemy.get() + 1);
        }
        while (maxSelf.get() >= 0) {
            if (myUnits.get(maxSelf.get()) > 0) {
                break;
            }
            maxSelf.set(maxSelf.get() - 1);
        }
        while (maxEnemy.get() >= 0) {
            if (enemyUnits.get(maxEnemy.get()) > 0) {
                break;
            }
            maxEnemy.set(maxEnemy.get() - 1);
        }
    }

    /**
     * This method switches the order of unit fighting in each round and updates the units' index of both sides
     * based on the current highest and lowest unit index for each side.
     * If the current unit from myUnits has already been defeated, the next highest unit would be used.
     * If the current unit from enemyUnits has already been defeated, the next lowest unit would be used.
     *
     * @param myIndex    the current index of myUnits
     * @param enemyIndex the current index of enemyUnits
     * @param minSelf    the current lowest index of myUnits
     * @param minEnemy   the current lowest index of enemyUnits
     * @param maxSelf    the current highest index of myUnits
     * @param maxEnemy   the current highest index of enemyUnits
     */
    private void switchOrderAndUpdate(AtomicInteger myIndex, AtomicInteger enemyIndex, AtomicInteger minSelf, AtomicInteger minEnemy, AtomicInteger maxSelf, AtomicInteger maxEnemy) {
        if (myIndex.get() == maxSelf.get() && enemyIndex.get() == minEnemy.get()) {
            //update the starting point
            findStartingUnit(minSelf, minEnemy, maxSelf, maxEnemy);
            myIndex.set(minSelf.get());
            enemyIndex.set(maxEnemy.get());
        } else if (myIndex.get() == minSelf.get() && enemyIndex.get() == maxEnemy.get()) {
            findStartingUnit(minSelf, minEnemy, maxSelf, maxEnemy);
            myIndex.set(maxSelf.get());
            enemyIndex.set(minEnemy.get());
        }

    }
}

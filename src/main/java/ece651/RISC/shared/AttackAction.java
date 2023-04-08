package ece651.RISC.shared;

public class AttackAction extends Action {
    private Combat myCombat;
    public AttackAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits, Status.actionStatus type, Player owner) {
        super(sourceTerritory, targetTerritory, hitUnits, type, owner);
        this.myCombat = new Combat();
    }

    public void moveOut(){
        sourceTerritory.updateUnits(-hitUnits);
    }

    /**
     * attack the territory from the chosen territory
     */
    public String attackTerritory() {
        String attackResult = new String();
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

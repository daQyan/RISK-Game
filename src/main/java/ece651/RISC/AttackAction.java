package ece651.RISC;

public class AttackAction extends Action {
    private Combat myCombat;
    public AttackAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits, Status.actionStatus type, Player owner) {
        super(sourceTerritory, targetTerritory, hitUnits, type, owner);
    }

    /**
     * attack the territory from the chosen territory
     */
    public String attackTerritory() {
        String checkAttack = myAC.checkAttackRule(this.owner, this.sourceTerritory, this.targetTerritory, hitUnits);
        if(checkAttack == null){
            while(hitUnits > 0 && targetTerritory.getUnit() >= 0){
                if(myCombat.rollCombatDice() == true){
                    targetTerritory.updateUnits(-1);
                }
                else{
                    --hitUnits;
                    sourceTerritory.updateUnits(-1);
                }
            }
            // if the attack wins
            if (targetTerritory.getUnit() < 0) {
                targetTerritory.changeOwner(hitUnits, this.owner);
                checkAttack = this.owner.getName() + " has taken over " + targetTerritory.getName() + "!";
            }
        }
        return checkAttack;
    }
}

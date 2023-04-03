package ece651.RISC;

public class AttackAction extends Action {

    public AttackAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits, Status.actionStatus type, Player owner) {
        super(sourceTerritory, targetTerritory, hitUnits, type, owner);
    }

    /**
     * attack the territory from the chosen territory
     */
    public String attackTerritory() {
        String checkAttack = myAC.checkAttackRule(this.owner, this.sourceTerritory, this.targetTerritory, hitUnits);
        if(checkAttack == null){
            sourceTerritory.updateUnits(-hitUnits);
            targetTerritory.updateUnits(hitUnits);
            // if the attack wins
            if (targetTerritory.getUnit() < 0) {
                checkAttack = "Owner Changed";
            }
        }
        return checkAttack;
    }
}

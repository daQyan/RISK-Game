package ece651.RISC;

public class AttackAction extends Action {

    public AttackAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits, String type, Player owner) {
        super(sourceTerritory, targetTerritory, hitUnits, type, owner);
    }

    /**
     * attack the territory from the chosen territory
     */
    public String attackTerritory() {
        // check adjacent and not owned
        // see if the owner is changed
        if (sourceTerritory.getOwner() == this.owner) {
            String checkAttack = myAC.checkAttackRule(this.owner, this.sourceTerritory, this.targetTerritory, hitUnits);
            if(checkAttack == null){
                sourceTerritory.updateUnits(-hitUnits);
                targetTerritory.updateUnits(hitUnits);
            }

            if (targetTerritory.getUnit() < 0) {
                checkAttack = "Owner Changed";
            }
            return checkAttack;
        }
        return "Attack failed because of owner switch!";
    }
}

package ece651.RISC;

public class AttackAction extends Action {

    public AttackAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits) {
        super(sourceTerritory, targetTerritory, hitUnits);
    }

    /**
     * attack the territory from the chosen territory
     */
    public String attackTerritory() {
        // check adjacent and not owned
        String checkAttack = myAC.checkAttackRule(this.sourceTerritory, this.targetTerritory, hitUnits);
        if(checkAttack == null){
            sourceTerritory.updateUnits(-hitUnits);
            targetTerritory.updateUnits(hitUnits);
        }
        return checkAttack;
    }
}

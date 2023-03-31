package ece651.RISC;

public class AttackAction extends Action {

    public AttackAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits) {
        super(sourceTerritory, targetTerritory, hitUnits);
    }

    /**
     * attack the territory from the chosen territory
     */
    public void attackTerritory() {
        // check adjacent and not owned
        sourceTerritory.updateUnits(-hitUnits);
        targetTerritory.updateUnits(hitUnits);
    }
}

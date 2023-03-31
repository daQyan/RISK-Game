package ece651.RISC;

public class MoveAction extends Action {


    public MoveAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits) {
        super(sourceTerritory, targetTerritory, hitUnits);
    }

    /**
     * move the units from one territory to the target one
     */
    public void moveTerritory() {
        // check adjacent and owned
        sourceTerritory.updateUnits(-hitUnits);
        targetTerritory.updateUnits(hitUnits);
    }
}

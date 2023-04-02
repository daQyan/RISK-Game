package ece651.RISC;

public class MoveAction extends Action {
    public MoveAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits) {
        super(sourceTerritory, targetTerritory, hitUnits);
    }

    /**
     * move the units from one territory to the target one
     */
    public String moveTerritory() {
        // check adjacent and owned
        String checkMove = myAC.checkMoveRule(this.sourceTerritory, this.targetTerritory, hitUnits);
        if(checkMove == null){
            sourceTerritory.updateUnits(-hitUnits);
            targetTerritory.updateUnits(hitUnits);
        }
        return checkMove;
    }
}

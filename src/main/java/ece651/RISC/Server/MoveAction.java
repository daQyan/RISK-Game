package ece651.RISC.Server;

import ece651.RISC.Status;
import ece651.RISC.shared.Action;
import ece651.RISC.shared.Territory;

public class MoveAction extends Action {
    public MoveAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits, Status.actionStatus type, Player owner) {
        super(sourceTerritory, targetTerritory, hitUnits, type, owner);
    }

    /**
     * move the units from one territory to the target one
     */
    public String moveTerritory() {
        // check adjacent and owned
        String checkMove = myAC.checkMoveRule(this.owner, this.sourceTerritory, this.targetTerritory, hitUnits);
        if(checkMove == null){
            sourceTerritory.updateUnits(-hitUnits);
            targetTerritory.updateUnits(hitUnits);
        }
        return checkMove;
    }
}

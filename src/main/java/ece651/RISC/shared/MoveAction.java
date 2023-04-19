package ece651.RISC.shared;

import ece651.RISC.shared.Status;
import ece651.RISC.shared.Action;
import ece651.RISC.shared.Territory;
import ece651.RISC.shared.Player;

public class MoveAction extends Action {
    public MoveAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits, Status.actionStatus type, Player owner) {
        super(sourceTerritory, targetTerritory, hitUnits, type, owner);
    }
    public void moveOut(){
        sourceTerritory.updateUnits(-hitUnits);
        targetTerritory.updateUnits(hitUnits);
    }

    /**
     * move the units from one territory to the target one
     */
    public String moveTerritory(GameMap gameMap, int sourceTerritoryId, int targetTerritoryId) {
        // check adjacent and owned
        String checkMove = myAC.checkMoveRule(this.owner, this.sourceTerritory, this.targetTerritory, hitUnits);
        if(checkMove == null){
            gameMap.getTerritory(sourceTerritoryId).updateUnits(-hitUnits);
            gameMap.getTerritory(targetTerritoryId).updateUnits(hitUnits);
        }
        return checkMove;
    }
}

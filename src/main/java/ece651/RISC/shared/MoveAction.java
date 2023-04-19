package ece651.RISC.shared;

import ece651.RISC.shared.Status;
import ece651.RISC.shared.Action;
import ece651.RISC.shared.Territory;
import ece651.RISC.shared.Player;

import java.util.ArrayList;

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
            //for evo 2: by default moving out from the highest level of units
            ArrayList<Integer> moved = gameMap.getTerritory(sourceTerritoryId).deployMyUnits(this.hitUnits);
            gameMap.getTerritory(targetTerritoryId).updateUnits(hitUnits);
            for(int i = 0; i < moved.size(); ++i){
                gameMap.getTerritory(targetTerritoryId).updateMyUnits(i, moved.get(i));
            }
        }
        return checkMove;
    }
}

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
            //moving the player's own units
            if(this.owner.getId() == gameMap.getTerritory(sourceTerritoryId).getOwner().getId()){
                gameMap.getTerritory(sourceTerritoryId).updateUnits(-hitUnits);
                //for evo 2: by default moving out from the highest level of units
                ArrayList<Integer> moved = gameMap.getTerritory(sourceTerritoryId).deployMyUnits(this.hitUnits);
                //moving to owner's own territory
                targetTerritoryReceive(gameMap, targetTerritoryId, moved);
            }
            //moving the ally player's units
            else if(gameMap.getTerritory(sourceTerritoryId).getAllyOwner() != null && this.owner.getId() == gameMap.getTerritory(sourceTerritoryId).getAllyOwner().getId()){
                gameMap.getTerritory(sourceTerritoryId).updateAllyUnitsNum(-hitUnits);
                //for evo 2: by default moving out from the highest level of units
                ArrayList<Integer> moved = gameMap.getTerritory(sourceTerritoryId).deployAllyUnits(this.hitUnits);
                //moving to owner's own territory
                targetTerritoryReceive(gameMap, targetTerritoryId, moved);
            }
        }
        return checkMove;
    }

    private void targetTerritoryReceive(GameMap gameMap, int targetTerritoryId, ArrayList<Integer> moved) {
        if(this.owner.getId() == gameMap.getTerritory(targetTerritoryId).getOwner().getId()){
            gameMap.getTerritory(targetTerritoryId).updateUnits(hitUnits);
            for(int i = 0; i < moved.size(); ++i){
                gameMap.getTerritory(targetTerritoryId).updateMyUnits(i, moved.get(i));
            }
        }
        //moving to ally's territory
        else if(gameMap.getTerritory(targetTerritoryId).getAllyOwner() != null && this.owner.getId() == gameMap.getTerritory(targetTerritoryId).getAllyOwner().getId()){
            gameMap.getTerritory(targetTerritoryId).updateAllyUnitsNum(hitUnits);
            for(int i = 0; i < moved.size(); ++i){
                gameMap.getTerritory(targetTerritoryId).updateAllyUnits(i, moved.get(i));
            }
        }
    }
}

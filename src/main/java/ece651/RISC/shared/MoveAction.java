package ece651.RISC.shared;

import java.util.ArrayList;
/**
 * The MoveAction class extends the Action class and represents a move action in the game.
 */
public class MoveAction extends Action {
    /**
     * Constructor for a MoveAction object
     *
     * @param sourceTerritory the territory that the units are moving from
     * @param targetTerritory the territory that the units are moving to
     * @param hitUnits        the number of units being moved
     * @param type            the type of the action
     * @param owner           the player who is performing the action
     */
    public MoveAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits, Status.actionStatus type, Player owner) {
        super(sourceTerritory, targetTerritory, hitUnits, type, owner);
    }

    public void moveOut() {
        sourceTerritory.updateUnits(-hitUnits);
        targetTerritory.updateUnits(hitUnits);
    }


    /**
     * Method to move the units from one territory to another
     *
     * @param gameMap           the game map containing the territories
     * @param sourceTerritoryId the id of the territory that the units are moving from
     * @param targetTerritoryId the id of the territory that the units are moving to
     * @return an error message if there was an issue with the move, null otherwise
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
            else if (this.owner.getId() == gameMap.getTerritory(sourceTerritoryId).getAllyOwner().getId()) {
                gameMap.getTerritory(sourceTerritoryId).updateAllyUnitsNum(-hitUnits);
                //for evo 2: by default moving out from the highest level of units
                ArrayList<Integer> moved = gameMap.getTerritory(sourceTerritoryId).deployAllyUnits(this.hitUnits);
                //moving to owner's own territory
                targetTerritoryReceive(gameMap, targetTerritoryId, moved);
            }
        }
        return checkMove;
    }

    /**
     * Helper method to move units to the target territory
     *
     * @param gameMap           the game map containing the territories
     * @param targetTerritoryId the id of the territory that the units are moving to
     * @param moved             the list of units that are being moved
     */

    private void targetTerritoryReceive(GameMap gameMap, int targetTerritoryId, ArrayList<Integer> moved) {
        if (this.owner.getId() == gameMap.getTerritory(targetTerritoryId).getOwner().getId()) {
            gameMap.getTerritory(targetTerritoryId).updateUnits(hitUnits);
            for (int i = 0; i < moved.size(); ++i) {
                gameMap.getTerritory(targetTerritoryId).updateMyUnits(i, moved.get(i));
            }
        }
        //moving to ally's territory
        else if (this.owner.getId() == gameMap.getTerritory(targetTerritoryId).getAllyOwner().getId()){
            gameMap.getTerritory(targetTerritoryId).updateAllyUnitsNum(hitUnits);
            for(int i = 0; i < moved.size(); ++i){
                gameMap.getTerritory(targetTerritoryId).updateAllyUnits(i, moved.get(i));
            }
        }
    }
}

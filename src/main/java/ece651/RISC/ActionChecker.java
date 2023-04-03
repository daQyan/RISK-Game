package ece651.RISC;

public class ActionChecker {
    public String checkAttackRule(Player owner, Territory sourceTerritory, Territory targetTerritory, int Units) {
        //if the attacking units are larger than the source territory's units, return error message
        if(Units > sourceTerritory.getUnit()){
            return("The attack action is invalid: there's not enough soldier in " + sourceTerritory.getName() +" to deploy!");
        }
        //if there's no valid path from the source territory to the target territory, return error message
        else if(!sourceTerritory.getAdjacents().contains(targetTerritory)){
            return("The attack action is invalid: unable to attack directly from " + sourceTerritory.getName() + " to " +targetTerritory.getName() + "!");
        }
        //if the player is trying to attack its own territory, return error message
        else if(sourceTerritory.getOwner().equals(targetTerritory.getOwner())){
            return("The attack action is invalid: cannot attack your own territory!");
        }
        return null;
    }
    public String checkMoveRule(Player owner, Territory sourceTerritory, Territory targetTerritory, int Units) {
        if(!owner.equals(sourceTerritory.getOwner())){
            return ("The move action is not valid: " + owner.getName() +  " does not own " + sourceTerritory.getName() + "!");
        }
        //if the soldiers the player tries to move exceed the existing number, return error message
        else if(sourceTerritory.getUnit() < Units){
            return ("The move action is not valid: there's not enough soldiers in the " + sourceTerritory.getName() + "!");
        }
        //if there's no valid path between the source territory and target territory, return error message
        else if(!sourceTerritory.getAccessibls().contains(targetTerritory)){
            return("The move action is not valid: there's no valid path from " + sourceTerritory.getName() + " to " +  targetTerritory.getName() + "!");
        }
        return null;
    }

}



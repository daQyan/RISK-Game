package ece651.RISC.shared;

public class ActionChecker {
    public String checkAttackRule(Player owner, Territory sourceTerritory, Territory targetTerritory, int Units) {
        //if the attacking units are larger than the source territory's units, return error message
        if(Units > sourceTerritory.getNumUnits()) {
            return ("The attack action is invalid: there's not enough soldier in " + sourceTerritory.getName() + " to deploy!");
        }
        else if(Units < 0){
            return("The attack action is invalid: you should at least deploy 1 soldier to attack!");
        }
        //if there's no valid path from the source territory to the target territory, return error message
        else if(!sourceTerritory.getAdjacents().contains(targetTerritory)){
            return("The attack action is invalid: unable to attack directly from " + sourceTerritory.getName() + " to " +targetTerritory.getName() + "!");
        }
        //if the player is trying to attack its own territory, return error message
        else if(sourceTerritory.getOwner().equals(targetTerritory.getOwner())){
            return("The attack action is invalid: cannot attack your own territory!");
        }
//        // if the owner of the territory is changed
//        else if(!sourceTerritory.getOwner().equals(owner)) {
//            return ("The attack action is invalid: attack failed because owner of the source territory has changed!");
//        }
        return null;
    }
    public String checkMoveRule(Player owner, Territory sourceTerritory, Territory targetTerritory, int Units) {
        System.out.println("checkMoveRule"+ owner.getName()+ owner.getId() + "," +sourceTerritory.getOwner().getName() + sourceTerritory.getOwner().getId());
        if(owner == null || !owner.equals(sourceTerritory.getOwner())){
            return ("The move action is not valid: " + owner.getName() +  " does not own " + sourceTerritory.getName() + "!");
        }
        //if the soldiers the player tries to move exceed the existing number, return error message
        else if(sourceTerritory.getNumUnits() < Units){
            return ("The move action is not valid: there's not enough soldiers in the " + sourceTerritory.getName() + "!");
        }
        //if there's no valid path between the source territory and target territory, return error message
        else if(!sourceTerritory.getAccessibles().contains(targetTerritory)){
            return("The move action is not valid: there's no valid path from " + sourceTerritory.getName() + " to " +  targetTerritory.getName() + "!");
        }
        return null;
    }

}



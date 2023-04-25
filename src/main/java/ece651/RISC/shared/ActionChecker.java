package ece651.RISC.shared;

public class ActionChecker {
    public String checkAttackRule(Player owner, Territory sourceTerritory, Territory targetTerritory, int units) {

        if (checkSource(owner, sourceTerritory, "attack") != null) return checkSource(owner, sourceTerritory, "attack");
        if (checkUnits(sourceTerritory, units, "attack") != null) return checkUnits(sourceTerritory, units, "attack");
        if (checkAtkTarget(sourceTerritory, targetTerritory) != null) return checkAtkTarget(sourceTerritory, targetTerritory);
        if(checkFoodResource(sourceTerritory, targetTerritory, units, "attack") != null) return checkFoodResource(sourceTerritory, targetTerritory, units, "attack");

//        //if the attacking units are larger than the source territory's units, return error message
//        if(Units > sourceTerritory.getNumUnits()) {
//            return ("The attack action is invalid: there's not enough soldier in " + sourceTerritory.getName() + " to deploy!");
//        }
//        else if(Units < 0){
//            return("The attack action is invalid: you should at least deploy 1 soldier to attack!");
//        }
//        //if there's no valid path from the source territory to the target territory, return error message
//        else if(!sourceTerritory.getAdjacents().contains(targetTerritory)){
//            return("The attack action is invalid: unable to attack directly from " + sourceTerritory.getName() + " to " +targetTerritory.getName() + "!");
//        }
//        //if the player is trying to attack its own territory, return error message
//        else if(sourceTerritory.getOwner().equals(targetTerritory.getOwner())){
//            return("The attack action is invalid: cannot attack your own territory!");
//        }
//        // if the owner of the territory is changed
//        else if(!sourceTerritory.getOwner().equals(owner)) {
//            return ("The attack action is invalid: attack failed because owner of the source territory has changed!");
//        }
        return null;
    }


    public String checkMoveRule(Player owner, Territory sourceTerritory, Territory targetTerritory, int units) {
        System.out.println("checkMoveRule "+ owner.getName() + owner.getId() + ", " +sourceTerritory.getOwner().getName() + sourceTerritory.getOwner().getId());

        if (checkSource(owner, sourceTerritory, "move") != null) return checkSource(owner, sourceTerritory, "move");
        if (checkUnits(sourceTerritory, units, "move") != null) return checkUnits(sourceTerritory, units, "move");
        if (checkAccess(sourceTerritory, targetTerritory) != null) return checkAccess(sourceTerritory, targetTerritory);
        if(checkFoodResource(sourceTerritory, targetTerritory, units, "move") != null) return checkFoodResource(sourceTerritory, targetTerritory, units, "move");

//        if(owner == null || !owner.equals(sourceTerritory.getOwner())){
//            return ("The move action is not valid: " + owner.getName() +  " does not own " + sourceTerritory.getName() + "!");
//        }
//        else if(sourceTerritory.getNumUnits() < units){
//            return ("The move action is not valid: there's not enough soldiers in the " + sourceTerritory.getName() + "!");
//        }
//        //if there's no valid path between the source territory and target territory, return error message
//        else if(!sourceTerritory.getAccessibles().contains(targetTerritory)){
//            return("The move action is not valid: there's no valid path from " + sourceTerritory.getName() + " to " +  targetTerritory.getName() + "!");
//        }
        return null;
    }

    // Check if owner has access to source territory
    public String checkSource(Player owner, Territory sourceTerritory, String actionType) {
        if (sourceTerritory == null) {
            return ("The " + actionType + " action is not valid: this is not a valid Territory name !");
        }
        if(owner == null || !owner.equals(sourceTerritory.getOwner())){
            return ("The " + actionType + " action is not valid: " + owner.getName() +  " does not own " + sourceTerritory.getName() + "!");
        }
        return null;
    }

    // Check if the soldiers the player tries to move exceed the existing number, return error message
    public String checkUnits(Territory sourceTerritory, int units, String actionType) {
        if(sourceTerritory.getNumUnits() < units){
            return ("The " + actionType + " action is not valid: there's not enough soldiers in the " + sourceTerritory.getName() + "!");
        }
        if(sourceTerritory.getNumUnits() <= 0){
            return ("The " + actionType + " action is not valid: you should at least deploy 1 soldier !");
        }
        return null;
    }

    // Check if source territory has access to target territory
    public String checkAccess(Territory sourceTerritory, Territory targetTerritory) {
        if (targetTerritory == null) {
            return ("This is not a valid Territory name !");
        }
        if(! sourceTerritory.getAccessibles().containsKey(targetTerritory)){
            return("The move action is not valid: there's no valid path from " + sourceTerritory.getName() + " to " +  targetTerritory.getName() + "!");
        }
        return null;
    }

    // Check if attack target territory is adjacent and valid
    public String checkAtkTarget(Territory sourceTerritory, Territory targetTerritory) {
        if (targetTerritory == null) {
            return ("This is not a valid Territory name !");
        }
        if(! sourceTerritory.getAdjacents().contains(targetTerritory)){
            return("The attack action is invalid: unable to attack directly from " + sourceTerritory.getName() + " to " +targetTerritory.getName() + "!");
        }
        //if the player is trying to attack its own territory, return error message
        else if(sourceTerritory.getOwner().equals(targetTerritory.getOwner())){
            return("The attack action is invalid: cannot attack your own territory!");
        }
        return null;
    }

    public String checkFoodResource(Territory sourceTerritory, Territory targetTerritory, int units, String actionType){
        if(actionType.equals("attack")){
            if(sourceTerritory.getOwner().getFoodResource() < units){
                return("The attack action is invalid: there's not enough food resource!");
            }
        }
        else if(actionType.equals("move")){
            if(sourceTerritory.getOwner().getFoodResource() < sourceTerritory.getAccessibles().get(targetTerritory) * units){
                return("The move action is invalid: there's not enough food resource!");
            }

        }
        return null;
    }


}



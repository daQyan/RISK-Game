package ece651.RISC.shared;

/**
 * This class contains methods for checking the validity of player actions such as attack and move.
 */
public class ActionChecker {

    /**
     * This method checks if an attack action is valid according to the following rules:
     * The player is the owner of the source territory.
     * The number of units in the source territory is at least as much as the number of units that the player wants to attack with.
     * The target territory is adjacent to the source territory and is not owned by the player.
     * The player has enough food resource to carry out the attack.
     * If any of the above rules is violated, this method will return an error message.
     *
     * @param owner           The player who initiates the attack action.
     * @param sourceTerritory The territory where the attack is initiated from.
     * @param targetTerritory The territory where the attack is targeted at.
     * @param units           The number of units the player wants to attack with.
     * @return An error message if the attack action is invalid, otherwise null.
     */
    public String checkAttackRule(Player owner, Territory sourceTerritory, Territory targetTerritory, int units) {

        if (checkSource(owner, sourceTerritory, "attack") != null) return checkSource(owner, sourceTerritory, "attack");
        if (checkUnits(sourceTerritory, units, "attack") != null) return checkUnits(sourceTerritory, units, "attack");
        if (checkAtkTarget(sourceTerritory, targetTerritory) != null)
            return checkAtkTarget(sourceTerritory, targetTerritory);
        if (checkFoodResource(owner, sourceTerritory, targetTerritory, units, "attack") != null)
            return checkFoodResource(owner, sourceTerritory, targetTerritory, units, "attack");

        return null;
    }

    /**
     * This method checks the validity of a move action based on the game rules.
     * It takes in the owner of the source territory, the source territory itself, the target territory, and the number of units to move.
     * It returns a string error message if the action is invalid, or null if the action is valid.
     *
     * @param owner           the player who owns the source territory
     * @param sourceTerritory the territory where the units are being moved from
     * @param targetTerritory the territory where the units are being moved to
     * @param units           the number of units being moved
     * @return a string error message if the action is invalid, or null if the action is valid
     */
    public String checkMoveRule(Player owner, Territory sourceTerritory, Territory targetTerritory, int units) {
        System.out.println("checkMoveRule " + owner.getName() + owner.getId() + ", " + sourceTerritory.getOwner().getName() + sourceTerritory.getOwner().getId());
        Status.moveSourceStatus getSource = getMoveSource(owner, sourceTerritory);
        if (getSource == Status.moveSourceStatus.OWNED) {
            if (sourceTerritory.getNumUnits() < units) {
                return ("The move action is not valid: there's not enough soldiers in the " + sourceTerritory.getName() + "!");
            }
//            if(sourceTerritory.getNumUnits() < 0){
//                return ("The move action is not valid: you should at least deploy 1 soldier !");
//            }
        } else if (getSource == Status.moveSourceStatus.ALLY) {
            if (sourceTerritory.getNumAllyUnits() < units) {
                return ("The move action is not valid: the source territory does not have enough units !");
            }
        } else return "The move action is not valid: this is not a valid Territory name or owner name.";

        if (checkAccess(sourceTerritory, targetTerritory) != null) return checkAccess(sourceTerritory, targetTerritory);
        if (checkFoodResource(owner, sourceTerritory, targetTerritory, units, "move") != null)
            return checkFoodResource(owner, sourceTerritory, targetTerritory, units, "move");
        return null;
    }

    /**
     * Determines if the source territory is owned by the player or an ally.
     *
     * @param owner           the player that wants to move the units
     * @param sourceTerritory the source territory that the units are moving from
     * @return the status of the source territory in relation to the owner (INVALID, OWNED, ALLY)
     */

    // check if the source territory is owned by the player or the ally
    private Status.moveSourceStatus getMoveSource(Player owner, Territory sourceTerritory) {
        if (sourceTerritory == null) {
            return Status.moveSourceStatus.INVALID;
        }
        if (owner == null) {
            return Status.moveSourceStatus.INVALID;
        }
        if (owner.equals(sourceTerritory.getOwner())) {
            return Status.moveSourceStatus.OWNED;
        }
        if (owner.equals(sourceTerritory.getAllyOwner())) {
            return Status.moveSourceStatus.ALLY;
        }
        return Status.moveSourceStatus.INVALID;
    }

    /**
     * Check if the source territory belongs to the given owner
     *
     * @param owner           the owner of the territory
     * @param sourceTerritory the source territory to check
     * @param actionType      the type of action (move or attack)
     * @return null if the source territory belongs to the owner, error message otherwise
     */
    // Check if owner has access to source territory
    public String checkSource(Player owner, Territory sourceTerritory, String actionType) {
        if (sourceTerritory == null) {
            return ("The " + actionType + " action is not valid: this is not a valid Territory name !");
        }
        if (owner == null) {
            return ("The " + actionType + " action is not valid: this is not a valid Player name !");
        }
        if (!owner.equals(sourceTerritory.getOwner())) {
            return ("The " + actionType + " action is not valid: " + owner.getId() + " does not own " + sourceTerritory.getName() + "!");
        }
        return null;
    }

    /**
     * Checks if the number of units specified by the player for the given source territory in the given action type is valid.
     *
     * @param sourceTerritory The source territory for the move or attack action.
     * @param units           The number of units specified by the player.
     * @param actionType      The type of action - move or attack.
     * @return Null if the number of units is valid, otherwise an error message.
     */

    // Check if the soldiers the player tries to move exceed the existing number, return error message
    public String checkUnits(Territory sourceTerritory, int units, String actionType) {
        if (sourceTerritory.getNumUnits() < units) {
            return ("The " + actionType + " action is not valid: there's not enough soldiers in the " + sourceTerritory.getName() + "!");
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
        if (!sourceTerritory.getAdjacents().contains(targetTerritory)) {
            return ("The attack action is invalid: unable to attack directly from " + sourceTerritory.getName() + " to " + targetTerritory.getName() + "!");
        }
        //if the player is trying to attack its own territory, return error message
        else if (sourceTerritory.getOwner().equals(targetTerritory.getOwner())) {
            return ("The attack action is invalid: cannot attack your own territory!");
        }
        return null;
    }

    /**
     * This method checks if the player has enough food resources to perform a move or attack action.
     * If actionType is "attack", it checks if the player has enough food resources to attack with the given number of units.
     * If actionType is "move", it checks if the player has enough food resources to move the given number of units from the source territory to the target territory.
     *
     * @param owner           the player who initiates the action
     * @param sourceTerritory the source territory where the units come from
     * @param targetTerritory the target territory where the units are moving to
     * @param units           the number of units involved in the action
     * @param actionType      the type of the action being performed, either "attack" or "move"
     * @return a String message indicating if the player has enough food resources to perform the action or not, or null if the player has enough food resources
     */

    public String checkFoodResource(Player owner, Territory sourceTerritory, Territory targetTerritory, int units, String actionType) {
        if (actionType.equals("attack")) {
            if (owner.getFoodResource() < units) {
                return ("The attack action is invalid: there's not enough food resource!");
            }
        } else if (actionType.equals("move")) {
            if (owner.getFoodResource() < sourceTerritory.getAccessibles().get(targetTerritory) * units) {
                return ("The move action is invalid: there's not enough food resource!");
            }

        }
        return null;
    }

}



package ece651.RISC;

public class MoveActionChecker<T> extends ActionChecker<T> {
    public MoveActionChecker(ActionChecker<T> next) {
        super(next);
    }
    @Override
    protected String checkMyRule(Territory sourceTerritory, Territory targetTerritory, int Units) {
        //if the soldiers the player tries to move exceed the existing number, return error message
        if(sourceTerritory.getUnit() < Units){
            return ("The move action is not valid: there's not enough soldiers in the " + sourceTerritory.getName() + " !");
        }
        //if there's no valid path between the source territory and target territory, return error message
        else if(!sourceTerritory.getAccessibls().contains(targetTerritory)){
            return("The move action is not valid: there's no valid path from " + sourceTerritory.getName() + " to " +  targetTerritory.getName() + " !");
        }
        return null;
    }
}

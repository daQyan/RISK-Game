package ece651.RISC;

public class AttackActionChecker<T> extends ActionChecker<T> {
    @Override
    protected String checkMyRule(Territory sourceTerritory, Territory targetTerritory, int Units) {
        //if the attacking units are larger than the source territory's units, return error message
        if(Units > sourceTerritory.getUnit()){
            return("The attack action is invalid: there's not enough soldier " + sourceTerritory.getName() +" to deploy!");
        }
        //if there's no valid path from the source territory to the target territory, return error message
        else if(!sourceTerritory.getAdjacents().contains(targetTerritory)){
            return("The attack action is invalid: unable to attack directly from " + sourceTerritory.getName() + " to " +targetTerritory.getName() + " !");
        }
        return null;
    }
}

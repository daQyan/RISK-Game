package ece651.RISC.shared;

public class Action {
    public Territory sourceTerritory;
    public Territory targetTerritory;
    protected int hitUnits;
    protected ActionChecker myAC;

    public Action(Territory sourceTerritory, Territory targetTerritory, int hitUnits) {
        this.sourceTerritory = sourceTerritory;
        this.targetTerritory = targetTerritory;
        this.hitUnits = hitUnits;
        this.myAC = new ActionChecker();
    }



//    public void doNothing() {
//        return;
//    }


}

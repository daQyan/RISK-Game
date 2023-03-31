package ece651.RISC;

public class Action {
    protected Territory sourceTerritory;
    protected Territory targetTerritory;
    protected int hitUnits;

    public Action(Territory sourceTerritory, Territory targetTerritory, int hitUnits) {
        this.sourceTerritory = sourceTerritory;
        this.targetTerritory = targetTerritory;
        this.hitUnits = hitUnits;
    }



//    public void doNothing() {
//        return;
//    }


}

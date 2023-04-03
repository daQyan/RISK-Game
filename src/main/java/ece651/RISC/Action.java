package ece651.RISC;

public abstract class Action {
    protected Territory sourceTerritory;
    protected Territory targetTerritory;
    protected int hitUnits;
    protected ActionChecker myAC;
    protected String type;
    protected Player owner;

    public Action(Territory sourceTerritory, Territory targetTerritory, int hitUnits, String type, Player owner) {
        this.sourceTerritory = sourceTerritory;
        this.targetTerritory = targetTerritory;
        this.hitUnits = hitUnits;
        this.type = type;
        this.owner = owner;
        this.myAC = new ActionChecker();
    }




}

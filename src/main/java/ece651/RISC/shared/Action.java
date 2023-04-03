package ece651.RISC.shared;

import ece651.RISC.Server.Player;
import ece651.RISC.Status;

public abstract class Action {
    public Territory sourceTerritory;
    public Territory targetTerritory;
    protected int hitUnits;
    protected ActionChecker myAC;
    protected Status.actionStatus type;
    protected Player owner;

    public Action(Territory sourceTerritory, Territory targetTerritory, int hitUnits, Status.actionStatus type, Player owner) {
        this.sourceTerritory = sourceTerritory;
        this.targetTerritory = targetTerritory;
        this.hitUnits = hitUnits;
        this.type = type;
        this.owner = owner;
        this.myAC = new ActionChecker();
    }




}

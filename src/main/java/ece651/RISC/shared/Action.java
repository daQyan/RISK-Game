package ece651.RISC.shared;

import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.shared.Player;
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

    public String toJSON() {
        JSONObject json = new JSONObject();
        json.put("sourceTerritory", sourceTerritory.getId());
        json.put("targetTerritory", targetTerritory.getId());
        json.put("hitUnits", hitUnits);
        json.put("type", type);
        json.put("player", owner.getId());
        return json.toJSONString();
    }
}

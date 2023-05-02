package ece651.RISC.shared;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class Action {
    @JSONField(serializeUsing = Territory.class)
    protected Territory sourceTerritory;
    @JSONField(serializeUsing = Territory.class)
    protected Territory targetTerritory;
    protected int hitUnits;
    //For EVO 2

    public void setSourceTerritory(Territory sourceTerritory) {
        this.sourceTerritory = sourceTerritory;
    }

    public void setTargetTerritory(Territory targetTerritory) {
        this.targetTerritory = targetTerritory;
    }

    public Territory getSourceTerritory() {
        return sourceTerritory;
    }

    public Territory getTargetTerritory() {
        return targetTerritory;
    }

    @JSONField(serialize = false, deserialize = false)
    protected ActionChecker myAC;
    protected Status.actionStatus type;
    @JSONField(serializeUsing = Player.class)
    protected Player owner;

    public Action(Territory sourceTerritory, Territory targetTerritory, int hitUnits, Status.actionStatus type, Player owner) {
        this.sourceTerritory = sourceTerritory;
        this.targetTerritory = targetTerritory;
        this.hitUnits = hitUnits;
        this.type = type;
        this.owner = owner;
        this.myAC = new ActionChecker();
    }
    

    public int getHitUnits(){
        return hitUnits;
    }

    public Player getOwner() {
        return owner;
    }

    public ActionChecker getMyAC() {
        return myAC;
    }
}

package ece651.RISC.shared;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

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

    public static void main(String[] args) {
        Territory t1 = new Territory(0, "t1");
        Territory t2 = new Territory(1, "t2");
        ArrayList<Territory> territories = new ArrayList<>();
        territories.add(t1);
        territories.add(t2);
        ArrayList<Integer> territoriesId = new ArrayList<>();
        territoriesId.add(0);
        territoriesId.add(1);
        Player p = new Player(0, "player1", territories,territoriesId, 999, 999);
        Action action = new Action(t1, t2, 3, Status.actionStatus.MOVE, p);
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(action);
        actions.add(action);
        String json = JSON.toJSONString(actions);
        System.out.println(json);
        List<Action> actions2 = JSON.parseArray(json, Action.class);
        System.out.println(actions.get(0).sourceTerritory.toJSON());
        System.out.println(JSON.toJSONString(actions2));
    }
}

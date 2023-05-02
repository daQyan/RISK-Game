package ece651.RISC.shared;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ActionTest {
    // test action
    @Test
    public void test() {
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

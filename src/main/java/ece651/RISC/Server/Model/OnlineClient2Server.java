package ece651.RISC.Server.Model;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.shared.*;
import java.util.ArrayList;

public class OnlineClient2Server {

    public String actionsMsg(Player clientPlayer, ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) {
        System.out.println("sendActions:" + moveActions.size());
        String playerJSON = JSON.toJSONString(clientPlayer);
        String moveActionsJSON = JSON.toJSONString(moveActions);
        String attackActionsJSON = JSON.toJSONString(attackActions);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("player", playerJSON);
        jsonObject.put("moveActions", moveActionsJSON);
        jsonObject.put("attackActions", attackActionsJSON);
        System.out.println("sendActions:" + jsonObject.toJSONString());

        return jsonObject.toJSONString();
    }

    public String nameMsg(Player clientPlayer) {
        String playerJSON = JSON.toJSONString(clientPlayer);
        return playerJSON;
    }

    public String allocationMsg(Player clientPlayer, ArrayList<Territory> territories) {
        String playerJSON = JSON.toJSONString(clientPlayer);
        String territoriesJSON = JSON.toJSONString(territories);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("player", playerJSON);
        jsonObject.put("territories", territoriesJSON);
        return jsonObject.toJSONString();
    }

}

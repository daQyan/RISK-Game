package ece651.RISC.Server.Model;

import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.shared.*;
import org.springframework.stereotype.Component;

@Component
public class OnlineServer2Client  {

    public String initializationMsg(GameMap map, int initUnit, int playerId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("initUnits", initUnit);
        jsonObject.put("map", JSONConvertor.map2JSON(map));
        jsonObject.put("playerId", playerId);
        String jsonString = jsonObject.toJSONString();
        System.out.println("initializationMsg"+jsonString);
        return jsonString;
    }

    public String allocationMsg(GameMap map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("map", JSONConvertor.map2JSON(map));
        String jsonString = jsonObject.toJSONString();
        System.out.println("allocationMsg"+jsonString);
        return jsonString;
    }

    public String oneTurnMsg(GameMap map, Status.playerStatus playerStatus, Status.gameStatus gameStatus) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playerStatus", playerStatus);
        jsonObject.put("playerStatus", gameStatus);
        jsonObject.put("map", JSONConvertor.map2JSON(map));
        String jsonString = jsonObject.toJSONString();
        System.out.println("allocationMsg"+jsonString);
        return jsonObject.toJSONString();
    }
}

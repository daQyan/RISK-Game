package ece651.RISC.Server.Model;

import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.shared.*;
import org.springframework.stereotype.Component;

@Component
public class OnlineServer2Client  {

    public String playerIdMsg(int playerId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("player_id", playerId);
        String jsonString = jsonObject.toJSONString();
        System.out.println("playerIdMsg "+jsonString);
        return jsonString;
    }

    public String gameMapMsg(GameMap map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("map", JSONConvertor.map2JSON(map));
        String jsonString = jsonObject.toJSONString();
        System.out.println("gameMapMsg "+jsonString);
        return jsonString;
    }

    public String unitNumMsg(int initUnit) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("init_units", initUnit);
        String jsonString = jsonObject.toJSONString();
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
        jsonObject.put("player_status", playerStatus);
        jsonObject.put("game_status", gameStatus);
        jsonObject.put("map", JSONConvertor.map2JSON(map));
        String jsonString = jsonObject.toJSONString();
        System.out.println("playingMsg"+jsonString);
        return jsonObject.toJSONString();
    }
}

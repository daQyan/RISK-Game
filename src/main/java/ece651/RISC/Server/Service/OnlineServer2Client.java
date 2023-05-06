package ece651.RISC.Server.Service;

import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.JSONConvertor;
import ece651.RISC.shared.Status;
import org.springframework.stereotype.Component;

/**
 * This class handles the conversion of server-side messages to JSON format
 * for transmission to clients.
 */
@Component
public class OnlineServer2Client {
    /**
     * Generates a JSON string message for a player ID.
     *
     * @param playerId the player ID to include in the message
     * @return the generated JSON string message
     */
    public String playerIdMsg(int playerId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("player_id", playerId);
        String jsonString = jsonObject.toJSONString();
        System.out.println("playerIdMsg " + jsonString);
        return jsonString;
    }

    /**
     * Generates a JSON string message for a game map.
     *
     * @param map the game map to include in the message
     * @return the generated JSON string message
     */
    public String gameMapMsg(GameMap map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("map", JSONConvertor.map2JSON(map));
        String jsonString = jsonObject.toJSONString();
        System.out.println("gameMapMsg " + jsonString);
        return jsonString;
    }

    /**
     * Generates a JSON string message for the initial number of units.
     *
     * @param initUnit the initial number of units to include in the message
     * @return the generated JSON string message
     */
    public String unitNumMsg(int initUnit) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("init_units", initUnit);
        String jsonString = jsonObject.toJSONString();
        return jsonString;
    }

    /**
     * Generates a JSON string message for territory allocation.
     *
     * @param map the game map to include in the message
     * @return the generated JSON string message
     */
    public String allocationMsg(GameMap map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("map", JSONConvertor.map2JSON(map));
        String jsonString = jsonObject.toJSONString();
        System.out.println("allocationMsg"+jsonString);
        return jsonString;
    }

    /**     * Generates a JSON string message for a single turn.
     * @param map the game map to include in the message
     * @param playerStatus the player status to include in the message
     * @param gameStatus the game status to include in the message
     * @return the generated JSON string message*/
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

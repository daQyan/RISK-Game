package ece651.RISC.Server.Model;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.shared.AttackAction;
import ece651.RISC.shared.MoveAction;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Territory;

import java.util.ArrayList;

/**
 * This class provides methods for constructing messages to be sent from client to server.
 */
public class OnlineClient2Server {
    /**
     * Constructs a message containing player move and attack actions.
     *
     * @param clientPlayer the player performing the actions
     * @param moveActions  the move actions to perform
     * @param attackActions  the attack actions to perform
     * @return the JSON formatted message to be sent to the server
     */
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

    /**
     * Constructs a message containing a player's name.
     *
     * @param clientPlayer the player to get the name of
     * @return the JSON formatted message to be sent to the server
     */
    public String nameMsg(Player clientPlayer) {
        String playerJSON = JSON.toJSONString(clientPlayer);
        return playerJSON;
    }

    /**
     * Constructs a message containing a player's territory allocations.
     *
     * @param clientPlayer the player performing the allocations
     * @param territories  the territories to allocate to the player
     * @return the JSON formatted message to be sent to the server
     */

    public String allocationMsg(Player clientPlayer, ArrayList<Territory> territories) {
        String playerJSON = JSON.toJSONString(clientPlayer);
        String territoriesJSON = JSON.toJSONString(territories);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("player", playerJSON);
        jsonObject.put("territories", territoriesJSON);
        return jsonObject.toJSONString();
    }

}

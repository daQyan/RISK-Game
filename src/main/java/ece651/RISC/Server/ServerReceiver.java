package ece651.RISC.Server;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.shared.AttackAction;
import ece651.RISC.shared.MoveAction;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Territory;

import java.util.ArrayList;
import java.util.List;

public class ServerReceiver {
    private ServerGame serverGame;
    public ServerReceiver(ServerGame serverGame){
        this.serverGame = serverGame;
    }

    public void receivePlayerName(String playerJSON){
        Player player = JSON.parseObject(playerJSON, Player.class);
        System.out.println("receivePlayerName" + player.toJSON());
        serverGame.addPlayer(player);
    }

    public void receiveAllocation(String allocationJSON){
        JSONObject jsonObject = JSON.parseObject(allocationJSON);
        String playerJSON = jsonObject.getString("player");
        String territoriesJSON = jsonObject.getString("territories");
        Player player = JSON.parseObject(playerJSON, Player.class);
        List<Territory> territories = JSON.parseArray(territoriesJSON, Territory.class);
        serverGame.playerAllocate(player, (ArrayList<Territory>) territories);
    }

    public void receiveActions(String actionsJSON) {
        System.out.println("receiveActions" + actionsJSON);
        JSONObject jsonObject = JSON.parseObject(actionsJSON);
        String playerJSON = jsonObject.getString("player");
        Player player = JSON.parseObject(playerJSON, Player.class);
        String moveActionsJSON = jsonObject.getString("moveActions");
        String attackActionsJSON = jsonObject.getString("attackActions");
        List<MoveAction> moveActions = JSON.parseArray(moveActionsJSON, MoveAction.class);
        List<AttackAction> attackActions = JSON.parseArray(attackActionsJSON, AttackAction.class);
        serverGame.playerOneTurn(player, (ArrayList<MoveAction>) moveActions, (ArrayList<AttackAction>) attackActions);
    }
}

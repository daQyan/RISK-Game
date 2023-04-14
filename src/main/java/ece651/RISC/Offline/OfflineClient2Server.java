package ece651.RISC.Offline;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Client.ClientPlayer;
import ece651.RISC.Server.ServerReceiver;
import ece651.RISC.shared.*;

import java.util.ArrayList;

public class OfflineClient2Server implements Client2Server {
    private ClientPlayer clientPlayer;

    private ServerReceiver serverReceiver;

    public OfflineClient2Server(ClientPlayer clientPlayer, ServerReceiver serverReceiver) {
        this.clientPlayer = clientPlayer;
        this.serverReceiver = serverReceiver;
    }

    //    private void setActionTerritories(Action receivedAction) {
//        Territory serverSourceTerritory = serverGame.getMyMap().getTerritory(receivedAction.getSourceTerritory().getId());
//        receivedAction.setSourceTerritory(serverSourceTerritory);
//        Territory serverTargetTerritory = serverGame.getMyMap().getTerritory(receivedAction.getTargetTerritory().getId());
//        receivedAction.setTargetTerritory(serverTargetTerritory);
//    }
    @Override
    public void sendActions(ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) {
        System.out.println("sendActions:" + moveActions.size());
        String playerJSON = JSON.toJSONString(clientPlayer);
        String moveActionsJSON = JSON.toJSONString(moveActions);
        String attackActionsJSON = JSON.toJSONString(attackActions);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("player", playerJSON);
        jsonObject.put("moveActions", moveActionsJSON);
        jsonObject.put("attackActions", attackActionsJSON);
        serverReceiver.receiveActions(jsonObject.toJSONString());
        //serverGame.playerOneTurn(clientPlayer, moveActions, attackActions);
    }

    @Override
    public void sendName() {
        String playerJSON = JSON.toJSONString(clientPlayer);
        serverReceiver.receivePlayerName(playerJSON);
    }

    @Override
    public void sendAllocation(ArrayList<Territory> territories) {
        String playerJSON = JSON.toJSONString(clientPlayer);
        String territoriesJSON = JSON.toJSONString(territories);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("player", playerJSON);
        jsonObject.put("territories", territoriesJSON);
        serverReceiver.receiveAllocation(JSON.toJSONString(jsonObject));
        //serverGame.playerAllocate(clientPlayer, territories);
    }
}

package ece651.RISC.Offline;

import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Client.ClientReceiver;
import ece651.RISC.shared.*;

import java.util.ArrayList;

public class OfflineServer2Client implements Server2Client {
    private ArrayList<ClientReceiver> receivers;

    OfflineServer2Client(ArrayList<ClientReceiver> receivers){
        this.receivers = receivers;
    }

    @Override
    public void sendOneTurn(Player to, GameMap map, Status.playerStatus status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status);
        jsonObject.put("map", JSONConvertor.map2JSON(map));
        for(ClientReceiver receiver: receivers) {
            if(receiver.getPlayer().equals(to)){
                receiver.receiveOneTurn(jsonObject.toJSONString());
                break;
            }
        }
    }

    @Override
    public void sendAllocation(Player to, ArrayList<Player> allPlayers, GameMap map, int initUnit) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("initUnits", initUnit);
        jsonObject.put("map", JSONConvertor.map2JSON(map));
        String jsonString = jsonObject.toJSONString();
        System.out.println("sendAllocation"+jsonString);
        for(ClientReceiver receiver: receivers) {
            System.out.println(receiver.getPlayer().getId() +"," + to.getId());
            if(receiver.getPlayer().equals(to)){
                receiver.receiveAllocation(jsonString);
                break;
            }
        }
    }

    // TODO to fix
    @Override
    public void sendId(Player to, int id) {
        for(ClientReceiver receiver: receivers) {
            if(receiver.getPlayer().getName().equals(to.getName()) ){
                receiver.receiveId(id);
                break;
            }
        }
    }

    public void sendInitialization(Player to, ArrayList<Player> allPlayers, GameMap map, int initUnit, int id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("initUnits", initUnit);
        jsonObject.put("map", JSONConvertor.map2JSON(map));
        String jsonString = jsonObject.toJSONString();
        System.out.println("sendAllocation"+jsonString);
        for(ClientReceiver receiver: receivers) {
            System.out.println(receiver.getPlayer().getId() +"," + to.getId());
            if(receiver.getPlayer().equals(to)){
                receiver.receiveAllocation(jsonString);
                break;
            }
        }
    }
}

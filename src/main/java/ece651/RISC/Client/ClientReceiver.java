package ece651.RISC.Client;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.JSONConvertor;
import ece651.RISC.shared.Status;

public class ClientReceiver {
    ClientPlayer player;

    public ClientPlayer getPlayer() {
        return player;
    }

    public ClientReceiver(ClientPlayer player){
        this.player = player;
    }

    // {
    // status: Status.playerStatus
    // map: {
    //      territories: Territory,
    //      adjacents: [TerritoryRelation],
    //      accessibles: [TerritoryRelation],
    // }
    // }

    public void receiveMap(String mapJSON) {
        System.out.println("receiveMap" + mapJSON);
        JSONObject mapObj = JSON.parseObject(mapJSON);
        String territoriesJSON = mapObj.getString("territories");
        GameMap newMap = JSONConvertor.convertTerritories(territoriesJSON);
        String adjacentsJSON = mapObj.getString("adjacents");
        JSONConvertor.setRelations(adjacentsJSON, newMap, true);
        String accessiblesJSON = mapObj.getString("accessibles");
        JSONConvertor.setRelations(accessiblesJSON, newMap, false);
        System.out.println("newMap" + newMap.getTerritories().size());
        player.setMap(newMap);
    }
    public void receiveOneTurn(String oneTurnJSON){
        System.out.println("receiveOneTurn" + oneTurnJSON);
        JSONObject jsonObj = JSON.parseObject(oneTurnJSON);
        String statusStr = jsonObj.getString("status");
        Status.playerStatus status = Status.playerStatus.valueOf(statusStr);
        player.setStatus(status);
        System.out.println(status + "," + player.getStatus());
        String map = jsonObj.getString("map");
        receiveMap(map);
        player.playOneTurn();
    }
    //{
    // map: {
    //      territories: Territory,
    //      adjacents: [TerritoryRelation],
    //      accessibles: [TerritoryRelation],
    // },
    // initUnits: int
    // }
    //
    public void receiveAllocation(String allocationJSON) {
        System.out.println("receiveAllocation");
        JSONObject jsonObj = JSON.parseObject(allocationJSON);
        int initUnits = jsonObj.getInteger("initUnits");
        player.setInitUnits(initUnits);
        String map = jsonObj.getString("map");
        receiveMap(map);
        player.initUnitPlacement();
    }
    // todo to fix
    public void receiveId(int id){
        player.setId(id);
    }
}

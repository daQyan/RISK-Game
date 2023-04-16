package ece651.RISC.shared;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Client.ClientPlayer;
import ece651.RISC.Client.ClientReceiver;
import ece651.RISC.Offline.OfflineClient2Server;
import ece651.RISC.Offline.OfflineServer2Client;
import ece651.RISC.Server.MapFactory;
import ece651.RISC.Server.ServerGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class JSONConvertor {
    public static GameMap convertTerritories(String json) {
        List<Territory> territories= JSON.parseArray(json, Territory.class);
        GameMap toReturn = new GameMap((ArrayList<Territory>) territories);
        return toReturn;
    }
    public static void setRelations(String json, GameMap gm, boolean adjacent) {
        List<TerritoryRelation> relations= JSON.parseArray(json, TerritoryRelation.class);
        for(TerritoryRelation relation: relations) {
            int selfId =  relation.getSelfId();
            Territory selfTerritory = gm.getTerritory(selfId);
            for(int id: relation.getRelatedIds()){
                Territory relatedTerritory = gm.getTerritory(id);
                if(adjacent) {
                    selfTerritory.addAdjacent(relatedTerritory);
                } else {
                    //need change here
                    selfTerritory.addAccessible(relatedTerritory, 0);
                }
            }
        }
    }

    public static String map2JSON(GameMap gm) {
        JSONObject jsonObject = new JSONObject();
        String territoriesJSON = JSON.toJSONString(gm.getTerritories());
        jsonObject.put("territories", territoriesJSON);
        ArrayList<TerritoryRelation> adjacentRelations = new ArrayList<>();
        ArrayList<TerritoryRelation> accessibleRelations = new ArrayList<>();
        for(Territory territory: gm.getTerritories()){
            TerritoryRelation adjacentRelation = new TerritoryRelation(territory.getId());
            TerritoryRelation accessibleRelation = new TerritoryRelation(territory.getId());
            for(Territory related: territory.getAdjacents()){
                adjacentRelation.addRelated(related.getId());
            }
            for(Territory related: territory.getAccessibles().keySet()){
                accessibleRelation.addRelated(related.getId());
            }
            adjacentRelations.add(adjacentRelation);
            accessibleRelations.add(accessibleRelation);
        }
        String adjacentRelationsJSON = JSON.toJSONString(adjacentRelations);
        String accessibleRelationsJSON = JSON.toJSONString(accessibleRelations);
        jsonObject.put("adjacents", adjacentRelationsJSON);
        jsonObject.put("accessibles", accessibleRelationsJSON);
        return jsonObject.toJSONString();
    }

    public static void main(String[] args) throws IOException {
        MapFactory mp = new MapFactory();
        GameMap gameMap = mp.createMap(3);
        Player player1 = new Player(0, "player1");
        Player player2 = new Player(1, "player2");
        Player player3 = new Player(2, "player3");
        for(int i = 0; i < 3; i++){
            player1.addTerritories(gameMap.getTerritory(i));
            gameMap.getTerritory(i).setOwner(player1);
            player2.addTerritories(gameMap.getTerritory(i+3));
            gameMap.getTerritory(i+3).setOwner(player2);
            player3.addTerritories(gameMap.getTerritory(i+6));
            gameMap.getTerritory(i+6).setOwner(player3);
        }
        gameMap.updateAccessible();
        String mapJSON = map2JSON(gameMap);
        System.out.println(mapJSON);
        JSONObject mapObj = JSON.parseObject(mapJSON);
        String territoriesJSON = mapObj.getString("territories");
        GameMap newMap = JSONConvertor.convertTerritories(territoriesJSON);
        String adjacentsJSON = mapObj.getString("adjacents");
        System.out.println("adjacents" + adjacentsJSON);
        JSONConvertor.setRelations(adjacentsJSON, newMap, true);
        String accessiblesJSON = mapObj.getString("accessibles");
        JSONConvertor.setRelations(accessiblesJSON, newMap, false);
        System.out.println(JSON.toJSONString(newMap));
        String mapJSON2 = map2JSON(newMap);
        System.out.println(mapJSON2.equals(mapJSON));
    }

}



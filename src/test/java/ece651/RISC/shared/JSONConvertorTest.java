package ece651.RISC.shared;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Server.MapFactory;
import org.junit.jupiter.api.Test;

import static ece651.RISC.shared.JSONConvertor.map2JSON;
import static org.junit.jupiter.api.Assertions.*;

class JSONConvertorTest {
    @Test
    public void test() {
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
        String mapJSON = map2JSON(gameMap);
        JSONObject mapObj = JSON.parseObject(mapJSON);
        String territoriesJSON = mapObj.getString("territories");
        GameMap newMap = JSONConvertor.convertTerritories(territoriesJSON);
        String adjacentsJSON = mapObj.getString("adjacents");
        JSONConvertor.setRelations(adjacentsJSON, newMap, true);
        String accessiblesJSON = mapObj.getString("accessibles");
        JSONConvertor.setRelations(accessiblesJSON, newMap, false);
        String mapJSON2 = map2JSON(newMap);
        assertEquals(mapJSON, mapJSON2);
    }
}
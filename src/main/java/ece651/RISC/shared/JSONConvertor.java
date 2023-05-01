package ece651.RISC.shared;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JSONConvertor {
    public static GameMap convertTerritories(String json) {
        List<Territory> territories = JSON.parseArray(json, Territory.class);
        return new GameMap((ArrayList<Territory>) territories);
    }
    public static void setRelations(String json, GameMap gm, boolean adjacent) {
        List<TerritoryRelation> relations = JSON.parseArray(json, TerritoryRelation.class);
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
//        ArrayList<TerritoryRelation> adjacentRelations = new ArrayList<>();
//        ArrayList<TerritoryRelation> accessibleRelations = new ArrayList<>();
//        for(Territory territory: gm.getTerritories()){
//            TerritoryRelation adjacentRelation = new TerritoryRelation(territory.getId());
//            TerritoryRelation accessibleRelation = new TerritoryRelation(territory.getId());
//            for(Territory related: territory.getAdjacents()){
//                adjacentRelation.addRelated(related.getId());
//            }
//            for(Territory related: territory.getAccessibles().keySet()){
//                accessibleRelation.addRelated(related.getId());
//            }
//            adjacentRelations.add(adjacentRelation);
//            accessibleRelations.add(accessibleRelation);
//        }
//        String adjacentRelationsJSON = JSON.toJSONString(adjacentRelations);
//        String accessibleRelationsJSON = JSON.toJSONString(accessibleRelations);
//        jsonObject.put("adjacents", adjacentRelationsJSON);
//        jsonObject.put("accessibles", accessibleRelationsJSON);
        return jsonObject.toJSONString();
    }
}



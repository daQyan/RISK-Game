package ece651.RISC.shared;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.shared.Territory;

import java.util.ArrayList;
import java.util.List;

public class JSONConvertor {
    public TerritoryMap convertTerritories(String json) {
        TerritoryMap res = new TerritoryMap();
        List<Territory> territories= JSON.parseArray(json, Territory.class);
        for(Territory territory: territories) {
            int id = territory.getId();
            res.addTerritory(id, territory);
        }
        return res;
    }

    public void addAdjacents(String json, TerritoryMap map) {
        List<TerritoryRelation> adjacentRelations= JSON.parseArray(json, TerritoryRelation.class);
        for(TerritoryRelation adjacentRelation: adjacentRelations) {
            int selfId =  adjacentRelation.getSelfId();
            Territory selfTerritory = map.getTerritoryById(selfId);
            for(int id: adjacentRelation.getRelatedIds()){
                Territory relatedTerritory = map.getTerritoryById(id);
                selfTerritory.addAdjacent(relatedTerritory);
            }
        }
    }

    public void addAccessibles(String json, TerritoryMap map) {
        List<TerritoryRelation> adjacentRelations= JSON.parseArray(json, TerritoryRelation.class);
        for(TerritoryRelation adjacentRelation: adjacentRelations) {
            int selfId =  adjacentRelation.getSelfId();
            Territory selfTerritory = map.getTerritoryById(selfId);
            for(int id: adjacentRelation.getRelatedIds()){
                Territory relatedTerritory = map.getTerritoryById(id);
                selfTerritory.addAccessible(relatedTerritory);
            }
        }
    }
}



package ece651.RISC.shared;

import com.alibaba.fastjson2.JSON;
import java.util.List;

public class JSONConvertor {
    public void convertTerritories(GameMap gm, String json) {
        List<Territory> territories= JSON.parseArray(json, Territory.class);
        for(Territory territory: territories) {
            int id = territory.getId();
            gm.setArea(id, territory);
        }
    }

    public void setRelations(String json, GameMap gm, boolean adjacent) {
        List<TerritoryRelation> relations= JSON.parseArray(json, TerritoryRelation.class);
        for(TerritoryRelation relation: relations) {
            int selfId =  relation.getSelfId();
            Territory selfTerritory = gm.getArea(selfId);
            for(int id: relation.getRelatedIds()){
                Territory relatedTerritory = gm.getArea(selfId);
                if(adjacent) {
                    selfTerritory.addAdjacent(relatedTerritory);
                } else {
                    selfTerritory.addAccessible(relatedTerritory);
                }

            }
        }
    }

}



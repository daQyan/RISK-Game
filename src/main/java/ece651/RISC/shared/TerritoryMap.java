package ece651.RISC.shared;

import java.util.HashMap;

public class TerritoryMap {
    private HashMap<Integer, Territory> territories;
    public void addTerritory(int id, Territory t){
        territories.put(id, t);
    }
    public Territory getTerritoryById(int id) {
        return territories.get(id);
    }
}

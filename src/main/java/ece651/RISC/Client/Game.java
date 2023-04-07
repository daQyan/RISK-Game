package ece651.RISC.Client;

import ece651.RISC.shared.Territory;

import java.util.HashMap;

public class Game {
    private HashMap<Integer, Territory> territories;

    public void Game(){
        this.territories = new HashMap<>();
    }
    public void setClientMap(HashMap<Integer, Territory> clientMap) {
        territories = clientMap;
    }

    public Territory getTerritoryById(int id) {
        return territories.get(id);
    }
}

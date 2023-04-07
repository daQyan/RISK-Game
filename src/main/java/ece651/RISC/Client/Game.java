package ece651.RISC.Client;

import ece651.RISC.shared.Territory;

import java.util.HashMap;

public class Game {
    private HashMap<Integer, Territory> clientMap;

    public void Game(){
        this.clientMap = new HashMap<>();
    }
    public void setClientMap(HashMap<Integer, Territory> clientMap) {
        clientMap = clientMap;
    }

    public Territory getTerritoryById(int id) {
        return clientMap.get(id);
    }
}

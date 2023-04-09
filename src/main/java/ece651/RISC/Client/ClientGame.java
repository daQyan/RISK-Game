package ece651.RISC.Client;

import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.Territory;

public class ClientGame {
    private GameMap map;

    private ClientPlayer player;

    public ClientPlayer getPlayer() {
        return player;
    }

    public ClientGame( ClientPlayer player) {
        this.player = player;
    }
    public void setClientMap(GameMap map) {
        this.map = map;
    }

    public Territory getTerritoryById(int id) {
        return map.getTerritory(id);
    }

    public void playOneTurn(){
        player.playOneTurn();
    }
}

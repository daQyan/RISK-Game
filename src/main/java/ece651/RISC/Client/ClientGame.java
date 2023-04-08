package ece651.RISC.Client;

import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.Territory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientGame {
    private GameMap map;

    private ClientPlayer player;

    public ClientPlayer getPlayer() {
        return player;
    }

    public void Game(GameMap map, ClientPlayer player){
        this.map = map;
        this.player = player;
    }
    public void setClientMap(GameMap map) {
        this.map = map;
    }

    public Territory getTerritoryById(int id) {
        return map.getArea(id);
    }

    public void playOneTurn() throws IOException {
        player.playOneTurn();
    }
}

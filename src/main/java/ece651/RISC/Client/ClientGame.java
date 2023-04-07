package ece651.RISC.Client;

import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.Territory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientGame {
    private GameMap map;

    private ArrayList<ClientPlayer> players;

    public void Game(GameMap map, ArrayList<ClientPlayer> players){
        this.map = map;
        this.players = players;
    }
    public void setClientMap(GameMap map) {
        this.map = map;
    }

    public Territory getTerritoryById(int id) {
        return map.getArea(id);
    }

    public void playOneTurn() throws IOException {
        for(ClientPlayer player: players) {
            player.playOneTurn();
        }
    }
}

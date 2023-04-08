package ece651.RISC.Offline;

import ece651.RISC.Client.ClientGame;
import ece651.RISC.Client.ClientPlayer;
import ece651.RISC.shared.*;

import java.io.IOException;
import java.util.ArrayList;

public class OfflineServer2Client implements Server2Client {
    private ArrayList<ClientPlayer> players;

    OfflineServer2Client(ArrayList<ClientPlayer> players){
        this.players = players;
    }

    @Override
    public void sendOneTurn(Player to, GameMap map, Status.playerStatus status) {
        for(ClientPlayer player: players) {
            if(player.equals(to)) {
                player.setStatus(status);
                player.setMap(map);
                player.playOneTurn();
            }
        }
    }

    @Override
    public void sendAllocation(Player to, ArrayList<Player> allPlayers, GameMap map, int initUnit){
        for(ClientPlayer player: players) {
            if(player.equals(to)) {
                player.setInitUnits(initUnit);
                player.setMap(map);
                player.initUnitPlacement();
            }
        }
    }
}

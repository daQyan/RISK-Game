package ece651.RISC.Offline;

import ece651.RISC.Client.ClientGame;
import ece651.RISC.Client.ClientPlayer;
import ece651.RISC.shared.*;

import java.io.IOException;
import java.util.ArrayList;

public class OfflineServer2Client implements Server2Client {
    private ArrayList<ClientGame> games;

    OfflineServer2Client(ArrayList<ClientGame> games){
        this.games = games;
    }

    @Override
    public void sendOneTurn(Player player, GameMap map, Status.playerStatus status) {
        for(ClientGame game: games) {
            if(game.getPlayer().equals(player)) {
                game.getPlayer().setStatus(status);
                game.setClientMap(map);
                game.playOneTurn();
            }
        }
    }

    @Override
    public void sendAllocation(Player player, ArrayList<Player> players, GameMap map, int initUnit){
        for(ClientGame game: games) {
            if(game.getPlayer().equals(player)) {
                game.getPlayer().setInitUnits(initUnit);
                game.getPlayer().initUnitPlacement();
            }
        }
    }

}

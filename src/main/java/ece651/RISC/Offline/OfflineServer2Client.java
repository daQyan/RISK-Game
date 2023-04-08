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
    public void sendMap(Player player, GameMap map) throws IOException {
        for(ClientGame game: games) {
            if(game.getPlayer().equals(player)) {
                game.setClientMap(map);
                game.playOneTurn();
            }
        }
    }

    @Override
    public void sendInitUnit() {

    }
}

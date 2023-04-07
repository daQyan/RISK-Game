package ece651.RISC.Offline;

import ece651.RISC.Client.ClientGame;
import ece651.RISC.shared.*;

import java.io.IOException;
import java.util.ArrayList;

public class OfflineServer2Client implements Server2Client {
    private ClientGame game;

    OfflineServer2Client(ClientGame game){
        this.game = game;
    }

    @Override
    public void sendMap(GameMap map) throws IOException {
        game.setClientMap(map);
        game.playOneTurn();
    }

    @Override
    public void sendInitUnit() {

    }
}

package ece651.RISC.Online;

import ece651.RISC.Client.ClientPlayer;
import ece651.RISC.Server.ServerGame;
import ece651.RISC.shared.*;

import java.io.IOException;
import java.util.ArrayList;

public class OnlineClient2Server implements Client2Server {
    private ServerGame serverGame;
    private ClientPlayer clientPlayer;

    public OnlineClient2Server(ServerGame serverGame, ClientPlayer clientPlayer) {
        this.serverGame = serverGame;
        this.clientPlayer = clientPlayer;
    }
    @Override
    public void sendActions(ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) {
        serverGame.playerOneTurn(clientPlayer, moveActions, attackActions);
    }

    @Override
    public void sendName() throws IOException {
        serverGame.addPlayer(clientPlayer);
    }

    @Override
    public void sendAllocation(ArrayList<Territory> territories) {
        serverGame.playerAllocate(clientPlayer, territories);
    }
}

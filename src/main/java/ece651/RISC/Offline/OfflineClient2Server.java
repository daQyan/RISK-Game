package ece651.RISC.Offline;

import ece651.RISC.Client.ClientGame;
import ece651.RISC.Client.ClientPlayer;
import ece651.RISC.Server.ServerGame;
import ece651.RISC.shared.*;

import java.io.IOException;
import java.util.ArrayList;

public class OfflineClient2Server implements Client2Server {
    private ServerGame serverGame;
    private ClientPlayer clientPlayer;

    public OfflineClient2Server(ServerGame serverGame, ClientPlayer clientPlayer) {
        this.serverGame = serverGame;
        this.clientPlayer = clientPlayer;
    }
    @Override
    public void sendActions(ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) throws IOException {
        serverGame.playerOneTurn(clientPlayer, moveActions, attackActions);
    }

    @Override
    public int sendName(Player player) throws IOException {
        int playerId = serverGame.addPlayer(player);
        return playerId;
    }

    @Override
    public void sendAllocation(ArrayList<Territory> territories) throws IOException {
        serverGame.playerAllocate(clientPlayer, territories);
    }
}

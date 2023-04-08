package ece651.RISC.Offline;

import ece651.RISC.Client.ClientGame;
import ece651.RISC.Server.ServerGame;
import ece651.RISC.shared.*;

import java.io.IOException;
import java.util.ArrayList;

public class OfflineClient2Server implements Client2Server {
    private ServerGame serverGame;
    private ClientGame clientGame;

    private int curTimes = 0;

    ArrayList<MoveAction> moveActions;
    ArrayList<AttackAction> attackActions;
    public OfflineClient2Server(ServerGame serverGame, ClientGame clientGame) {
        this.serverGame = serverGame;
        this.clientGame = clientGame;
    }
    @Override
    public void sendActions(ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) throws IOException {
        Player player = clientGame.getPlayer();
        serverGame.playerOneTurn(player, moveActions, attackActions);
    }

    @Override
    public int sendName(String name) {
        return 0;
    }

    @Override
    public void sendAllocation(ArrayList<Territory> moveActions) {

    }
}

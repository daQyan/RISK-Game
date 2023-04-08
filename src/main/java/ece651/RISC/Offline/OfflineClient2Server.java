package ece651.RISC.Offline;

import ece651.RISC.Server.ServerGame;
import ece651.RISC.shared.AttackAction;
import ece651.RISC.shared.MoveAction;
import ece651.RISC.shared.Client2Server;
import ece651.RISC.shared.Territory;

import java.io.IOException;
import java.util.ArrayList;

public class OfflineClient2Server implements Client2Server {
    private ServerGame game;
    private int playerSize;

    private int curTimes = 0;

    ArrayList<MoveAction> moveActions;
    ArrayList<AttackAction> attackActions;
    public OfflineClient2Server(ServerGame game, int playerSize) {
        this.game = game;
        this.playerSize = playerSize;
        this.attackActions = new ArrayList<>();
        this.moveActions = new ArrayList<>();
    }
    @Override
    public void sendActions(ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) throws IOException {
        curTimes++;
        this.attackActions.addAll(attackActions);
        this.moveActions.addAll(moveActions);
        if(curTimes == playerSize) {
            game.playOneTurn(this.moveActions, this.attackActions);
            curTimes = 0;
            this.attackActions.clear();
            this.moveActions.clear();
        }
    }

    @Override
    public int sendName(String name) {
        return 0;
    }

    @Override
    public void sendAllocation(ArrayList<Territory> moveActions) {

    }
}

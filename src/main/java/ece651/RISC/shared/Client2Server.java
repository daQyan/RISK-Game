package ece651.RISC.shared;

import java.io.IOException;
import java.util.ArrayList;

public interface Client2Server {
    void sendActions(ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions);
    // return player_id
    int sendName(Player player) throws IOException;
    //
    void sendAllocation(ArrayList<Territory> territories);
}

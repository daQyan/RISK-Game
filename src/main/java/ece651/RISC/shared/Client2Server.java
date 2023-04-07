package ece651.RISC.shared;

import java.util.ArrayList;

public interface Client2Server {
    void sendActions(ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions);
    // return player_id
    int sendName(String name);
    //
    void sendAllocation(ArrayList<Territory> moveActions);
}

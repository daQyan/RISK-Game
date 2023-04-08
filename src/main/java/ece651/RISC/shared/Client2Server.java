package ece651.RISC.shared;

import java.util.ArrayList;

public interface Client2Server {
    void sendActions(ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions);
    // return player_id
    int sendName(Player player);
    //
    void sendAllocation(ArrayList<Territory> territories);
}

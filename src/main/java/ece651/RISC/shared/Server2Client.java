package ece651.RISC.shared;

import java.io.IOException;
import java.util.ArrayList;

public interface Server2Client {
    void sendOneTurn(Player player, GameMap map, Status.playerStatus status);
    void sendAllocation(Player player, ArrayList<Player> players, GameMap map, int initUnit);
    void sendId(Player to, int id);
}

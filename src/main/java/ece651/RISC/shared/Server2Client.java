package ece651.RISC.shared;

import java.io.IOException;

public interface Server2Client {
    void sendMap(Player player, GameMap map) throws IOException;
    void sendInitUnit();
}

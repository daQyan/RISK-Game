package ece651.RISC.shared;

import java.io.IOException;

public interface Server2Client {
    void sendMap(GameMap map) throws IOException;
    void sendInitUnit();
}

package ece651.RISC.Server.Model;

import ece651.RISC.shared.Status;
import lombok.Data;

@Data
public class GameInfo {
    private long gameId;
    private int roomSize;
    private int initialUnit;
    private Status.gameStatus myStatus;

    public GameInfo(long gameId, int roomSize, int initialUnit, Status.gameStatus myStatus) {
        this.gameId = gameId;
        this.roomSize = roomSize;
        this.initialUnit = initialUnit;
        this.myStatus = myStatus;
    }

    // getters and setters
}
package ece651.RISC.Server.Model;

import ece651.RISC.shared.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GameInfo {
    private String GameMapString;
    private String GamePlayersString;
    private long gameId;
    private int roomSize;
    private int currentSize;
    private int initialUnit;
    private Status.gameStatus myStatus;
}
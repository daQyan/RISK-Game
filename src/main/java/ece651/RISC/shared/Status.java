package ece651.RISC.shared;

public class Status {
    public enum playerStatus{
        PLAYING,
        WIN,
        LOSE
    }
    public enum actionStatus{
        MOVE,
        ATTACK
    }
    public enum gameStatus{
        WAITINGPLAYER,
        WAITINGPLAYERALLOCATE,
        PLAYING,
        FINISHED
    }

    public enum moveSourceStatus{
        OWNED,
        ALLY,
        INVALID
    }
}

package ece651.RISC.shared;

public class Status {
    public enum playerStatus{
        WIN,
        PLAYING,
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
}

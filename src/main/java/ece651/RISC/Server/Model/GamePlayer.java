package ece651.RISC.Server.Model;
import ece651.RISC.shared.Status;
import lombok.Data;

@Data
public class GamePlayer {

    protected int playerIndex;

    protected int userId;

    protected String name;

    protected Status.playerStatus status;

    public void setStatus(Status.playerStatus status) {
        this.status = status;
    }

    public Status.playerStatus getStatus() {
        return status;
    }
}

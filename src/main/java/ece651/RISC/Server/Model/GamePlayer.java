package ece651.RISC.Server.Model;
import ece651.RISC.shared.Status;
import lombok.Data;

@Data
public class GamePlayer {

    protected int playerIndex;

    protected long userId;

    protected String name;

    protected Status.playerStatus status;

    protected int techResource;

    protected int foodResource;

    protected int techLevel;

    public GamePlayer(long userId, String userName) {
        this.userId = userId;
        this.name = userName;
    }

}

package ece651.RISC;

import java.util.ArrayList;
import java.util.Set;

public class Player {
    //should implement socket here
    Game game;
    private int name;
    private Status.playerStatus myStatus;

    private Set<Territory> myTerritory;

    public Player(int name, Set<Territory> myTerritory) {
        this.name = name;
        this.myTerritory = myTerritory;
        this.myStatus = Status.playerStatus.PLAYING;
    }

    public Set<Territory> getMyTerritory() {
        return myTerritory;
    }

    public int getName() {
        return name;
    }

    public void changeStatus(Status.playerStatus s){
        myStatus = s;
    }

    // checkIsMyTerritory

}

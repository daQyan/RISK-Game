package ece651.RISC.Server;

import ece651.RISC.Status;
import ece651.RISC.shared.Territory;

import java.util.ArrayList;
import java.util.Set;

public class Player {
    //should implement socket here
    private final String name;
    private Status.playerStatus myStatus;
    private Set<Territory> myTerritory;
    private ArrayList<MoveAction> myMoveOrder;
    private ArrayList<AttackAction> myAttackOrder;

    public Player(String name, Set<Territory> myTerritory) {
        this.name = name;
        this.myTerritory = myTerritory;
        this.myStatus = Status.playerStatus.PLAYING;
    }

    public Set<Territory> getMyTerritory() {
        return myTerritory;
    }

    public String getName() {
        return name;
    }

    public void changeStatus(Status.playerStatus s){
        myStatus = s;
    }

    public void parseMoveAction(String order){

    }
    // checkIsMyTerritory

}

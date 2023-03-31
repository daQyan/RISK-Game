package ece651.RISC;

import java.util.ArrayList;

public class Player {
    //should implement socket here
    private int name;
    private ArrayList<Territory> myTerritory;

    public Player(int name, ArrayList<Territory> myTerritory) {
        this.name = name;
        this.myTerritory = myTerritory;
    }

    public ArrayList<Territory> getMyTerritory() {
        return myTerritory;
    }

    public int getName() {
        return name;
    }

    // checkIsMyTerritory

}

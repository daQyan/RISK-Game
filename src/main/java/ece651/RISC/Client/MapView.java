package ece651.RISC.Client;

import ece651.RISC.shared.GameMap;

public abstract class MapView {
    protected GameMap myMap;

    public MapView(GameMap myMap) {
        this.myMap = myMap;
    }

    public abstract void displayMap();
}

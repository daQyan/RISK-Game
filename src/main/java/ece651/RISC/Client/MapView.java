package ece651.RISC.Client;

import ece651.RISC.shared.GameMap;
/**
 * This is the abstract MapView class that provides the basic functionality for displaying the game map.
 * It has a protected GameMap field that stores the game map.
 * It has a constructor that initializes the GameMap field.
 * It has an abstract method displayMap that subclasses must implement to display the game map.
 */
public abstract class MapView {
    // A protected GameMap field that stores the game map
    protected GameMap myMap;

    /**
     * The constructor initializes the GameMap field.
     */
    public MapView(GameMap myMap) {
        this.myMap = myMap;
    }

    /**
     * An abstract method that subclasses must implement to display the game map.
     */
    public abstract void displayMap();
}

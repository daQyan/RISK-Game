package ece651.RISC.Client;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Territory {

    private final String name;
    private int unit;

    private Player owner;
    private Set<Territory> adjacents;

    public String getName() {
        return name;
    }

    public int getUnit() {
        return unit;
    }

    public Set<Territory> getAdjacents() {
        return adjacents;
    }

    public Territory(String name, int unit) {
        this.name = name;
        this.unit = unit;
        this.adjacents = new HashSet<>();
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void addAdjacent(Territory adjacent) {
        this.adjacents.add(adjacent);
    }

    @Override
    public String toString() {
        String adjacentString = "";
        Iterator<Territory> it = adjacents.iterator();
        while(it.hasNext()){
            adjacentString += it.next().getName();
            if(it.hasNext()){
                adjacentString += ", ";
            }
        }
        return unit + " units in " + name +" (next to: " + adjacentString + ")";
    }

}

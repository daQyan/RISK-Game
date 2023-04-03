package ece651.RISC.Client;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Terriory {

    private final String name;
    private int unit;

    private Player owner;
    private Set<Terriory> adjacents;

    public String getName() {
        return name;
    }

    public int getUnit() {
        return unit;
    }

    public Set<Terriory> getAdjacents() {
        return adjacents;
    }

    public Terriory(String name, int unit, Player owner) {
        this.name = name;
        this.unit = unit;
        this.owner = owner;
        this.adjacents = new HashSet<>();
    }

    public void addAdjacent(Terriory adjacent) {
        this.adjacents.add(adjacent);
    }

    @Override
    public String toString() {
        String adjacentString = "";
        Iterator<Terriory> it = adjacents.iterator();
        while(it.hasNext()){
            adjacentString += it.next().getName();
            if(it.hasNext()){
                adjacentString += ", ";
            }
        }
        return unit + " units in " + name +" (next to: " + adjacentString + ")";
    }
}

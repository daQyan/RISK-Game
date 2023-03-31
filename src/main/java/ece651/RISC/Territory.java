package ece651.RISC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Territory {
    private final String name;
    private Player owner;
    private int numUnits;
    private Set<Territory> adjacents;

    public Territory(String name, Player owner, int numUnits, Set<Territory> adjacents) {
        this.name = name;
        this.owner = owner;
        this.numUnits = numUnits;
        this.adjacents = adjacents;
    }


    public Set<Territory> getAdjacents() {
        return adjacents;
    }


    public void addAdjacent(Territory adjacent) {
        this.adjacents.add(adjacent);
    }



    //update numUnits

    public void updateUnits(int unitChanged) {
        this.numUnits = numUnits + unitChanged;
    }


    public String getName() {
        return name;
    }

    public int getUnit() {
        return numUnits;
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
        return numUnits + " units in " + name +" (next to: " + adjacentString + ")";
    }


    //change owner


    //plus 1 unit every round

}

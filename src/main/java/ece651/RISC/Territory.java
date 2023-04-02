package ece651.RISC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Territory {
    private final String name;
    private Player owner;
    private int numUnits;
    private Set<Territory> adjacents;
    private Set<Territory> accessibles;

    public Territory(String name, Player owner, int numUnits, Set<Territory> adjacents, Set<Territory> accessibles) {
        this.name = name;
        this.owner = owner;
        this.numUnits = numUnits;
        this.adjacents = adjacents;
        this.accessibles = accessibles;
    }


    public Set<Territory> getAdjacents() {
        return adjacents;
    }


    public void addAdjacent(Territory adjacent) {
        this.adjacents.add(adjacent);
    }


    public Set<Territory> getAccessibls(){
        return accessibles;
    }

    public void addAccessible(Territory accessible) {
        this.accessibles.add(accessible);
    }
    //update numUnits

    public void updateUnits(int unitChanged) {
        this.numUnits = numUnits + unitChanged;
    }


    public String getName() {
        return name;
    }

    public Player getOwner(){ return owner; }

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

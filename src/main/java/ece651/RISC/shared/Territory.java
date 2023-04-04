package ece651.RISC.shared;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Server.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Territory {
    private final int id;

    public int getId() {
        return id;
    }

    private final String name;
    private Player owner;
    private int numUnits;
    private Set<Territory> adjacents;
    private Set<Territory> accessibles;

    public Territory(int id, String name, Player owner, int numUnits, Set<Territory> adjacents, Set<Territory> accessibles) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.numUnits = numUnits;
        this.adjacents = adjacents;
        this.accessibles = accessibles;
    }

    public Territory(int id, String name, int numUnits){
        this(id, name, null, numUnits, new HashSet<>(), new HashSet<>());
    }
    public void changeOwner(int newUnits, Player newOwner) {
        owner = newOwner;
        numUnits = newUnits;
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
        numUnits += unitChanged;
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

    public String toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("numUnits", numUnits);
        if(owner != null){
            json.put("player", owner.getName());
        }
        JSONArray adjacentsId = new JSONArray();
        for(Territory adjacent: adjacents){
            adjacentsId.add(adjacent.getId());
        }
        json.put("adjacents", adjacentsId.toJSONString());
        JSONArray accessiblesId = new JSONArray();
        for(Territory accessible: accessibles){
            accessiblesId.add(accessible.getId());
        }
        json.put("accessibles", accessiblesId.toJSONString());
        return json.toJSONString();
    }
}

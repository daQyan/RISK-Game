package ece651.RISC.shared;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;

public class Territory {
    @JSONField(name = "id")
    private int id;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "num_units")
    private int numUnits;

    @JSONField(name = "owner")
    private Player owner;

    @JSONField(serialize = false, deserialize = false)
    private ArrayList<Territory> adjacents = new ArrayList<>();

    @JSONField(serialize = false, deserialize = false)
    private ArrayList<Territory> accessibles = new ArrayList<>();

    public Territory() {}
    public Territory(int id, String name, int unit, Player owner, ArrayList<Territory> adjacents, ArrayList<Territory> accessibles) {
        this.id = id;
        this.name = name;
        this.numUnits = unit;
        this.owner = owner;
        this.accessibles = accessibles;
        this.adjacents = adjacents;
    }

    public Territory(int id, String name, int unit, Player owner) {
        this(id, name, unit, owner, new ArrayList<>(), new ArrayList<>());
    }

    public Territory(int id, String name) {
        this(id, name, 0, new Player());
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumUnits() {
        return numUnits;
    }

    public void setNumUnits(int unit) {
        this.numUnits = unit;
    }

    public void updateUnits(int unit) {
        this.numUnits += unit;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player ownerPlayer) {
        this.owner = ownerPlayer;
    }

    public ArrayList<Territory> getAdjacents() {
        return adjacents;
    }

    public void setAdjacents(ArrayList<Territory> adjacents) {
        this.adjacents = adjacents;
    }

    public void addAdjacent(Territory adjacent) {
        this.adjacents.add(adjacent);
    }

    public ArrayList<Territory> getAccessibles() {
        return accessibles;
    }

    public void setAccessibles(ArrayList<Territory> accessibles) {
        this.accessibles = accessibles;
    }

    public void addAccessible(Territory accessible) {
        this.accessibles.add(accessible);
    }

    public int getUnit(){ return this.numUnits;}

    public void changeOwner(int newUnits, Player newOwner){
        this.numUnits = newUnits;
        this.owner = newOwner;
    }

    public String toJSON(){
        String json = JSON.toJSONString(this);
        return json;
    }

    public static void main(String[] args) throws URISyntaxException {
        Player cp = new Player(1, "cp");
        Territory t = new Territory(1, "test", 10, cp);
        String json = t.toJSON();
        System.out.println(json);
        Territory t2 = JSON.parseObject(json, Territory.class);
        System.out.println(t2.toJSON());
    }
}



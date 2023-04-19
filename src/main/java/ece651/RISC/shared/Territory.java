package ece651.RISC.shared;

import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.*;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;

public class Territory {
    @JSONField(name = "id")
    private int id;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "num_units")
    private int numUnits;
    //for evo 2: use index as unit's level, from low to high
    private ArrayList<Integer> myUnits = new ArrayList<>(Collections.nCopies(7, 0));

    @JSONField(name = "owner")
    private Player owner;

    @JSONField(name = "fee")
    private int fee;

    @JSONField(serialize = false, deserialize = false)
    private ArrayList<Territory> adjacents = new ArrayList<>();

    @JSONField(serialize = false, deserialize = false)
    //private ArrayList<Territory> accessibles = new ArrayList<>();
    //for evo 2: change to linkedHashmap
    private LinkedHashMap<Territory, Integer> accessibles = new LinkedHashMap<>();

    @JSONField(name = "size")
    private int size;


    public Territory() {}
    public Territory(int id, String name, int unit, Player owner, ArrayList<Territory> adjacents, LinkedHashMap<Territory, Integer> accessibles, ArrayList<Integer> myUnits) {
        this.id = id;
        this.name = name;
        this.numUnits = unit;
        this.owner = owner;
        this.accessibles = accessibles;
        this.adjacents = adjacents;
        //fixed size of cost
        this.size = 2;
        this.myUnits = myUnits;
    }

    public Territory(int id, String name, int unit, Player owner) {
        this(id, name, unit, owner, new ArrayList<>(), new LinkedHashMap<>(), new ArrayList<>(Collections.nCopies(7, 0)));
    }

    public Territory(int id, String name) {
        this(id, name, 0, new Player());
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getFee() {
        return fee;
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
    public Boolean equals(Territory rhs){
        return this.getId() == rhs.getId();
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

    public LinkedHashMap<Territory, Integer> getAccessibles() {
        return accessibles;
    }

    public void setAccessibles(LinkedHashMap<Territory, Integer> accessibles) {
        this.accessibles = accessibles;
    }

    public void addAccessible(Territory accessible, int cost) {
        this.accessibles.put(accessible, cost);
        //need to call updateAccessible() in the GameMap after using this function
    }

    public ArrayList<Integer> getMyUnits(){ return this.myUnits;}

    //only for moving and attacking other territory; upgrade should call another overloading function
    public ArrayList<Integer> deployMyUnits(int num){
        int index = 6;
        ArrayList<Integer> deploy = new ArrayList<>(Collections.nCopies(7, 0));
        while(num > 0){
            if(myUnits.get(index) > 0){
                myUnits.set(index, myUnits.get(index) - 1);
                deploy.set(index, deploy.get(index) + 1);
            }
            else{
                --index;
                continue;
            }
            --num;
        }
        return deploy;
    }

    public void updateMyUnits(int unitIndex, int numUnits){
        this.myUnits.set(unitIndex, myUnits.get(unitIndex) + numUnits);
    }

    public void setMyUnits(ArrayList<Integer> newUnits){
        this.myUnits = newUnits;
    }

    public void changeOwner(int newUnits, Player newOwner){
        this.numUnits = newUnits;
        this.owner = newOwner;
    }

    public String toJSON(){
        String json = JSON.toJSONString(this);
        return json;
    }

    public static void main(String[] args) throws URISyntaxException {
        Player cp = new Player();
        Territory t = new Territory(1, "test", 10, cp);
        String json = t.toJSON();
        System.out.println(json);
        Territory t2 = JSON.parseObject(json, Territory.class);
        System.out.println(t2.toJSON());
    }

    @Override
    public String toString() {

        return name;
    }
}



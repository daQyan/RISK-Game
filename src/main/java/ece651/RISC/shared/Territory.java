package ece651.RISC.shared;

import java.util.*;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class Territory {
    @JSONField(name = "id")
    private int id;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "num_units")
    private int numUnits;
    //for evo 2: use index as unit's level, from low to high
    private ArrayList<Integer> myUnits = new ArrayList<>(Collections.nCopies(7, 0));

    @JSONField(serialize = false, deserialize = false)
    private Player owner;

    @JSONField(name = "ownerId")
    private int ownerId;

    @JSONField(name = "fee")
    private int fee;
    // TODO change to using ID
    @JSONField(serialize = false, deserialize = false)
    private ArrayList<Territory> adjacents = new ArrayList<>();

    @JSONField(name = "adjacentsID")
    private ArrayList<Integer> adjacentIds = new ArrayList<>();
    @JSONField(serialize = false, deserialize = false)
    private LinkedHashMap<Territory, Integer> accessibles = new LinkedHashMap<>();
    @JSONField(name = "accessiblesID")
    private LinkedHashMap<Integer, Integer> accessibleIds = new LinkedHashMap<>();
    @JSONField(name = "size")
    private int size;
    @JSONField(name = "foodResourceGrow")
    private int foodResourceGrow;

    @JSONField(name = "techResourceGrow")
    private int techResourceGrow;

    public Territory() {}
    public Territory(int id, String name, int unit, Player owner,
                     ArrayList<Territory> adjacents, ArrayList<Integer> adjacentIds, LinkedHashMap<Territory, Integer> accessibles,
                     LinkedHashMap<Integer, Integer> accessibleIds, ArrayList<Integer> myUnits) {
        this.id = id;
        this.name = name;
        this.numUnits = unit;
        this.owner = owner;
        this.accessibles = accessibles;
        this.accessibleIds = accessibleIds;
        this.adjacents = adjacents;
        this.adjacentIds = adjacentIds;
        //fixed size of cost
        this.size = 2;
        this.foodResourceGrow = 5;
        this.techResourceGrow = 5;
        this.myUnits = myUnits;
        updateMyUnits(0, numUnits);
    }

    public Territory(int id, String name, int unit, Player owner) {
        this(id, name, unit, owner, new ArrayList<>(), new ArrayList<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new ArrayList<>(Collections.nCopies(7, 0)));
    }

    public Territory(int id, String name) {
        this(id, name, 0, new Player());
    }

    public void setAccessibleIdsFromItsAccessible(HashMap<Territory, Integer> accessibles) {
        // set the accessibleIds from its accessibles
        for (Map.Entry<Territory, Integer> entry : accessibles.entrySet()) {
            this.accessibleIds.put(entry.getKey().getId(), entry.getValue());
        }
    }

    public void setAdjacentIdsFromItsAdjacent(ArrayList<Territory> adjacents) {
        // set the adjacentIds from its adjacents
        for (Territory adjacent : adjacents) {
            this.adjacentIds.add(adjacent.getId());
        }
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

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean equals(Territory rhs){
        return this.getId() == rhs.getId();
    }
    public ArrayList<Territory> getAdjacents() {
        return adjacents;
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

    public String toJSON(){
        return JSON.toJSONString(this);
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Territory otherTerritory = (Territory) obj;
        return id == otherTerritory.getId();
    }

    public static void main(String[] args) {
        Player cp = new Player();
        Territory t = new Territory(1, "test", 10, cp);
        LinkedHashMap<Integer, Integer> lmp = new LinkedHashMap<>();
        lmp.put(1, 1);
        t.setAccessibleIds(lmp);
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



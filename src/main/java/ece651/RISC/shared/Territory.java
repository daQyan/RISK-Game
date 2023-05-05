package ece651.RISC.shared;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.*;

/**
 * This class represents a territory in the game map.
 * It contains information about the territory's ID, name, owner, number of units,
 * adjacent territories, accessible territories and ally owner.
 * It also has methods for updating units, setting adjacents and accessibles,
 * and deploying units to move or attack.
 */
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

    private ArrayList<Integer> allyUnits = new ArrayList<>(Collections.nCopies(7, 0));

    @JSONField(serialize = false, deserialize = false)
    private Player owner;

    @JSONField(name = "ownerId")
    private int ownerId;

    @JSONField(serialize = false, deserialize = false)
    private ArrayList<Territory> adjacents = new ArrayList<>();

    @JSONField(name = "adjacentsID")
    private ArrayList<Integer> adjacentIds = new ArrayList<>();
    @JSONField(serialize = false, deserialize = false)
    private LinkedHashMap<Territory, Integer> accessibles = new LinkedHashMap<>();
    @JSONField(name = "accessiblesID")
    private LinkedHashMap<String, Integer> accessibleIds = new LinkedHashMap<>();
    @JSONField(name = "size")
    private int size;

    @JSONField(name = "foodResourceGrow")
    private int foodResourceGrow;

    @JSONField(name = "techResourceGrow")
    private int techResourceGrow;

    //initialized as null, need to check if it's applicable
    @JSONField(name = "allyOwner")
    private Player allyOwner;

    private int numAllyUnits;

    public Territory() {
    }

    /**
     * Constructor for Territory class.
     *
     * @param id            ID of the territory.
     * @param name          Name of the territory.
     * @param unit          Number of units in the territory.
     * @param owner         Player object that owns the territory.
     * @param adjacents     ArrayList of adjacent Territory objects.
     * @param adjacentIds   ArrayList of IDs of adjacent territories.
     * @param accessibles   LinkedHashMap of accessible Territory objects with their costs.
     * @param accessibleIds LinkedHashMap of IDs of accessible territories with their costs.
     * @param myUnits       ArrayList of integers representing the number of units in each level.
     * @param allyPlayer    Player object representing the ally owner of the territory.
     * @param numAllyUnits  Number of ally units in the territory.
     */
    public Territory(int id, String name, int unit, Player owner,
                     ArrayList<Territory> adjacents, ArrayList<Integer> adjacentIds, LinkedHashMap<Territory, Integer> accessibles,
                     LinkedHashMap<String, Integer> accessibleIds, ArrayList<Integer> myUnits, Player allyPlayer, int numAllyUnits) {
        this.id = id;
        this.name = name;
        this.numUnits = unit;
        this.owner = owner;
        this.accessibles = accessibles;
        this.accessibleIds = accessibleIds;
        this.adjacents = adjacents;
        this.adjacentIds = adjacentIds;
        this.size = 2;
        this.foodResourceGrow = 5;
        this.techResourceGrow = 5;
        this.myUnits = myUnits;
        this.allyOwner = allyPlayer;
        this.numAllyUnits = numAllyUnits;
        updateAllyUnits(0,numAllyUnits);
        updateMyUnits(0, numUnits);
    }

    public Territory(int id, String name, int unit, Player owner) {
        this(id, name, unit, owner, new ArrayList<>(), new ArrayList<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), new ArrayList<>(Collections.nCopies(7, 0)), null, 0);
    }

    public Territory(int id, String name) {
        this(id, name, 0, new Player());
    }

    public void setAccessibleIdsFromItsAccessible(HashMap<Territory, Integer> accessibles) {
        // set the accessibleIds from its accessibles
        for (Map.Entry<Territory, Integer> entry : accessibles.entrySet()) {
            // put the territory id as the key and the cost as the value
            this.accessibleIds.put(String.valueOf(entry.getKey().getId()), entry.getValue());
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

    public void updateAllyUnitsNum(int unit){this.numAllyUnits += unit;}

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player ownerPlayer) {
        this.owner = ownerPlayer;
    }

    public void setAllyOwner(Player allyOwner){this.allyOwner = allyOwner;}

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

    public Player getAllyOwner() {
        return allyOwner;
    }

    public ArrayList<Integer> getAllyUnits() {
        return allyUnits;
    }

    public void addAccessible(Territory accessible, int cost) {
        this.accessibles.put(accessible, cost);
        //need to call updateAccessible() in the GameMap after using this function
    }

    public ArrayList<Integer> getMyUnits(){ return this.myUnits;
    }

    /**

     Deploy units from the given units arraylist, using the given number of units.
     @param num The number of units to deploy.
     @param units The arraylist of units to deploy from.
     @return An arraylist of the deployed units.
     */
    private ArrayList<Integer> deployUnits(int num, ArrayList<Integer> units){
        int index = 6;
        ArrayList<Integer> deploy = new ArrayList<>(Collections.nCopies(7, 0));
        while(num > 0){
            if(units.get(index) > 0){
                units.set(index, units.get(index) - 1);
                deploy.set(index, deploy.get(index) + 1);
            } else{
                --index;
                continue;
            }
            --num;
        }
        return deploy;
    }

    //only for moving and attacking other territory; upgrade should call another overloading function
    public ArrayList<Integer> deployMyUnits(int num){
        return deployUnits(num, this.myUnits);
    }

    public ArrayList<Integer> deployAllyUnits(int num){
        return deployUnits(num, this.allyUnits);
    }

    public void updateMyUnits(int unitIndex, int numUnits){
        this.myUnits.set(unitIndex, myUnits.get(unitIndex) + numUnits);
    }

    public void updateAllyUnits(int unitIndex, int numUnits){
        this.allyUnits.set(unitIndex, allyUnits.get(unitIndex) + numUnits);
    }

    public void setMyUnits(ArrayList<Integer> newUnits){
        this.myUnits = newUnits;
    }

    public void setAllyUnits(ArrayList<Integer> newUnits){
        this.allyUnits = newUnits;
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

    public String toJSON(){
        return JSON.toJSONString(this);
    }

}



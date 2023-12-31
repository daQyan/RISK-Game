package ece651.RISC.shared;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import ece651.RISC.Client.MapTextView;
import lombok.Data;

import java.util.ArrayList;

/**
 * This class represent a player in the game.
 */
@Data
public class Player {

    @JSONField(name = "id")
    protected int id;

    @JSONField(name = "name")
    protected String name;

    @JSONField(serialize = false, deserialize = false)
    protected ArrayList<Territory> territories;

    @JSONField(name = "territoriesID")
    protected ArrayList<Integer> territoriesId;

    @JSONField(name = "status")
    protected Status.playerStatus status;

    @JSONField(name = "techResource")
    protected int techResource;

    @JSONField(name = "foodResource")
    protected int foodResource;

    @JSONField(name = "techLevel")
    protected int techLevel;

    @JSONField(name = "allyPlayer")
    protected int allyPlayer = -1;


    public void setStatus(Status.playerStatus status) {
        this.status = status;
    }

    public Status.playerStatus getStatus() {
        return status;
    }

    @JSONField(serialize = false, deserialize = false)
    protected GameMap map;
    @JSONField(serialize = false, deserialize = false)
    protected MapTextView view = new MapTextView(map);

    public Player(int id, String name, ArrayList<Territory> territories, ArrayList<Integer> territoriesId, int techResource, int foodResource) {
        this.id = id;
        this.name = name;
        this.territories = territories;
        this.territoriesId = territoriesId;
        this.status = Status.playerStatus.PLAYING;
        this.techLevel = 1;
        this.techResource = techResource;
        this.foodResource = foodResource;
    }

    public Player(int id, String name, ArrayList<Territory> territories, GameMap map) {
        this.id = id;
        this.name = name;
        this.territories = territories;
        this.status = Status.playerStatus.PLAYING;
        this.map = map;
        this.view = new MapTextView(this.map);
    }

    public Player(int id, String name) {
        this(id, name, new ArrayList<>(), new ArrayList<>(), 1000, 1000);
    }

    public Player(String name) {
        this(-1, name);
    }

    public Player() {
        this(-1, "");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTerritories(ArrayList<Territory> territories) {
        this.territories = territories;
    }

    public void addTerritories(Territory t){
        territories.add(t);
    }

    public void updateTechResource(int num) {
        this.techResource += num;
    }

    public void updateFoodResource(int num) {
        this.foodResource += num;
    }

    public int getTechResource() {
        return techResource;
    }

    public int getFoodResource() {
        return foodResource;
    }

    public int getTechLevel() {
        return techLevel;
    }

    public int getAllyPlayer() {
        return allyPlayer;
    }

//    public void formAlliance(Player ally){
//        this.allyPlayer = ally;
//        for(Territory t: territories){
//            t.setAllyOwner(ally);
//        }
//    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(name + ":" + System.lineSeparator() +
                "-------------" + System.lineSeparator());
        for(Territory t: territories){
            res.append(t.toString()).append(System.lineSeparator());
        }
        return res.toString();
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
        // 'equals()' should check the class of its parameter
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Player objPlayer = (Player)obj;
        return id == objPlayer.getId();
    }


    public String toJSON(){
        return JSON.toJSONString(this);
    }


    public void upgradeTechLevel() {
        int cost = costTechUpgrade(this.techLevel);
        checkTechUpgrade(cost);
        // update resource, follow --> table
        this.updateTechResource(-cost);
        this.techLevel++;
    }

    private void checkTechUpgrade(int cost) {
        // check max
        if (this.techLevel == 6) {
            throw new IllegalArgumentException("You have reached max level! Cannot upgrade anymore !");
        }
        // check resource
        if (this.techResource < cost) {
            throw new IllegalArgumentException("Upgrade Failed, you do not have enough tech resources !");
        }
    }

    public int costTechUpgrade(int level) {
        if (level == 1) return 50;
        return costTechUpgrade(level - 1) + 25 * (level - 1);
    }

    public static void main(String[] args) {
        Player p = new Player();
        p.updateTechResource(300);
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource() + "\n");

        p.upgradeTechLevel();
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource());
        p.upgradeTechLevel();
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource());
        p.upgradeTechLevel();
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource());
        p.upgradeTechLevel();
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource());
        p.upgradeTechLevel();
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource());

    }

    public void addAlly(Player targetPlayer, ArrayList<Territory> updatedTerritories) {
        this.allyPlayer = targetPlayer.getId();
        for(Territory t: updatedTerritories){
            t.setAllyOwner(targetPlayer.getId());
        }
    }
}

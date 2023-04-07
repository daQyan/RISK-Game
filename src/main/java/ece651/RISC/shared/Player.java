package ece651.RISC.shared;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;

import java.util.HashSet;
import java.util.Set;

public class Player {
    @JSONField(name = "id")
    protected int id;
    @JSONField(name = "name")
    protected String name;
    @JSONField(serialize = false, deserialize = false)
    protected Set<Territory> territories;

    public Player(int id, String name, Set<Territory> territories) {
        this.id = id;
        this.name = name;
        this.territories = territories;
    }

    public Player(int id, String name) {
        this(id, name, new HashSet<>());
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Territory> getTerritories() {
        return territories;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTerritories(Set<Territory> territories) {
        this.territories = territories;
    }

    public void addTerriories(Territory t){
        territories.add(t);
    }

    @Override
    public String toString() {
        String res = name + ":" + System.lineSeparator() +
                "-------------" + System.lineSeparator();
        for(Territory t: territories){
            res += (t.toString() + System.lineSeparator());
        }
        return res;
    }

    public boolean equals(Player p) {
        return id == p.getId() && name == p.getName();
    }

    public String toJSON(){
        String json = JSON.toJSONString(this);
        return json;
    }
}

package ece651.RISC.shared;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {


    @Test
    void testGetName() {
        // Create a territory with a name
        Territory territory = new Territory(0, "Territory 1", 0, null);

        // Check that the territory's name matches the original name
        assertEquals("Territory 1", territory.getName());
    }

    @Test
    void testGetOwner() {
        // Create a player and a territory with that player as the owner
        Player owner = new Player(0, "Player 1");
        Territory territory = new Territory(0, "Territory 1", 0, owner);

        // Check that the territory's owner matches the original player
        assertEquals(owner, territory.getOwner());
    }

    @Test
    void testGetUnit() {
        // Create a territory with a number of units
        Territory territory = new Territory(0, "Territory 1", 5, null);

        // Check that the territory's number of units matches the original number
        assertEquals(5, territory.getNumUnits());
    }

    @Test
    public void testParseJSON() {
        Player cp = new Player();
        Territory t = new Territory(1, "test", 10, cp);
        LinkedHashMap<String, Integer> lmp = new LinkedHashMap<>();
        lmp.put("1", 1);
        t.setAccessibleIds(lmp);
        String json = t.toJSON();
        System.out.println(json);
        Territory t2 = JSON.parseObject(json, Territory.class);
        System.out.println(t2.toJSON());
    }
//    @Test
//    void testGetAdjacents() {
//        // Create some territories and set up their adjacencies
//        Territory t1 = new Territory(0, "Territory 1", 0);
//        Territory t2 = new Territory(1, "Territory 2", 0);
//        Territory t3 = new Territory(2, "Territory 3", 0);
//        Set<Territory> adjacents = new HashSet<>();
//        adjacents.add(t2);
//        adjacents.add(t3);
//        t1.addAdjacent(t2);
//        t1.addAdjacent(t3);
//
//        // Check that the first territory's adjacents match the original set
//        assertEquals(adjacents, t1.getAdjacents());
//    }

//    @Test
//    void testGetAccessibles() {
//        // Create some territories and set up their accessibilities
//        Territory t1 = new Territory(0, "Territory 1", 0);
//        Territory t2 = new Territory(1, "Territory 2", 0);
//        Territory t3 = new Territory(2, "Territory 3", 0);
//        Set<Territory> accessibles = new HashSet<>();
//        accessibles.add(t2);
//        accessibles.add(t3);
//        t1.addAccessible(t2);
//        t1.addAccessible(t3);
//
//        // Check that the first territory's accessibles match the original set
//        assertEquals(accessibles, t1.getAccessibles());
//    }

//    @Test
//    void testUpdateUnits() {
//        // Create a territory with a number of units and update the number
//        Territory territory = new Territory(0, "Territory 1",5);
//        territory.updateUnits(3);
//
//        // Check that the territory's number of units has been updated
//        assertEquals(8, territory.getNumUnits());
//    }

//    @Test
//    void testToJSON(){
//        Territory t1 = new Territory(0, "Territory 1", 0);
//        assertEquals("{\"id\":0,\"name\":\"Territory 1\",\"num_units\":0}", t1.toJSON());
//        Player p = new Player(1, "player");
//        t1.setOwner(p);
//        assertEquals("{\"id\":0,\"name\":\"Territory 1\",\"num_units\":0,\"owner\":{\"id\":1,\"name\":\"player\"}}", t1.toJSON());
//    }
}

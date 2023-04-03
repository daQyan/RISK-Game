package ece651.RISC;

import ece651.RISC.Server.Player;
import ece651.RISC.shared.Territory;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {


    @Test
    void testGetName() {
        // Create a territory with a name
        Territory territory = new Territory(0, "Territory 1", null, 0, new HashSet<>(), new HashSet<>());

        // Check that the territory's name matches the original name
        assertEquals("Territory 1", territory.getName());
    }

    @Test
    void testGetOwner() {
        // Create a player and a territory with that player as the owner
        Player owner = new Player(0, "Player 1", new HashSet<>());
        Territory territory = new Territory(0, "Territory 1", owner, 0, new HashSet<>(), new HashSet<>());

        // Check that the territory's owner matches the original player
        assertEquals(owner, territory.getOwner());
    }

    @Test
    void testGetUnit() {
        // Create a territory with a number of units
        Territory territory = new Territory(0, "Territory 1", null, 5, new HashSet<>(), new HashSet<>());

        // Check that the territory's number of units matches the original number
        assertEquals(5, territory.getUnit());
    }

    @Test
    void testGetAdjacents() {
        // Create some territories and set up their adjacencies
        Territory t1 = new Territory(0, "Territory 1", null, 0, new HashSet<>(), new HashSet<>());
        Territory t2 = new Territory(1, "Territory 2", null, 0, new HashSet<>(), new HashSet<>());
        Territory t3 = new Territory(2, "Territory 3", null, 0, new HashSet<>(), new HashSet<>());
        Set<Territory> adjacents = new HashSet<>();
        adjacents.add(t2);
        adjacents.add(t3);
        t1.addAdjacent(t2);
        t1.addAdjacent(t3);

        // Check that the first territory's adjacents match the original set
        assertEquals(adjacents, t1.getAdjacents());
    }

    @Test
    void testGetAccessibles() {
        // Create some territories and set up their accessibilities
        Territory t1 = new Territory(0, "Territory 1", null, 0, new HashSet<>(), new HashSet<>());
        Territory t2 = new Territory(1, "Territory 2", null, 0, new HashSet<>(), new HashSet<>());
        Territory t3 = new Territory(2, "Territory 3", null, 0, new HashSet<>(), new HashSet<>());
        Set<Territory> accessibles = new HashSet<>();
        accessibles.add(t2);
        accessibles.add(t3);
        t1.addAccessible(t2);
        t1.addAccessible(t3);

        // Check that the first territory's accessibles match the original set
        assertEquals(accessibles, t1.getAccessibls());
    }

    @Test
    void testUpdateUnits() {
        // Create a territory with a number of units and update the number
        Territory territory = new Territory(0, "Territory 1", null, 5, new HashSet<>(), new HashSet<>());
        territory.updateUnits(3);

        // Check that the territory's number of units has been updated
        assertEquals(8, territory.getUnit());
    }

    @Test
    void testToJSON(){
        Territory t1 = new Territory(0, "Territory 1", 0);
        Territory t2 = new Territory(1, "Territory 2", null, 0, new HashSet<>(), new HashSet<>());
        Territory t3 = new Territory(2, "Territory 3", null, 0, new HashSet<>(), new HashSet<>());
        assertEquals("{\"id\":0,\"name\":\"Territory 1\",\"numUnits\":0,\"adjacents\":\"[]\",\"accessibles\":\"[]\"}", t1.toJSON());
        t1.addAdjacent(t2);
        t1.addAccessible(t2);
        t1.addAccessible(t3);
        assertEquals("{\"id\":0,\"name\":\"Territory 1\",\"numUnits\":0,\"adjacents\":\"[1]\",\"accessibles\":\"[2,1]\"}", t1.toJSON());
    }
}

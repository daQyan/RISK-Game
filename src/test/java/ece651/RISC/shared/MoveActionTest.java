package ece651.RISC.shared;

import ece651.RISC.Server.MapFactory;
import org.junit.jupiter.api.Test;

public class MoveActionTest {
    @Test
    public void testMoveTerritory(){
        GameMap mp = new MapFactory().createMap(3);
        Player p0 = new Player("0");
        Player p1 = new Player("1");
        Player p2 = new Player("2");
        mp.getTerritory(0).setOwner(p0);
        mp.getTerritory(1).setOwner(p0);
        mp.getTerritory(2).setOwner(p0);
        mp.getTerritory(3).setOwner(p1);
        mp.getTerritory(4).setOwner(p1);
        mp.getTerritory(5).setOwner(p1);
        mp.getTerritory(6).setOwner(p2);
        mp.getTerritory(7).setOwner(p2);
        mp.getTerritory(8).setOwner(p2);
        for(Territory t: mp.getTerritories()){
            t.setNumUnits(5);
            t.updateMyUnits(0, 5);
        }
        mp.updateAccessible();
        mp.setAccessibleIdsFromAccessible();
        MoveAction mv0 = new MoveAction(mp.getTerritory(0), mp.getTerritory(1), 3, Status.actionStatus.MOVE, p0);
        mv0.moveTerritory(mp, 0, 1);

    }
}

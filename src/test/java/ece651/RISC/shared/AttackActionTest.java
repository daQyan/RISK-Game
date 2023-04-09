package ece651.RISC.shared;

import ece651.RISC.Server.MapFactory;
import org.junit.jupiter.api.Test;

public class AttackActionTest {
    @Test
    public void testAttackAction(){
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
        }
        mp.updateAccessible();
        //attack self
        AttackAction aa0 = new AttackAction(mp.getTerritory(0), mp.getTerritory(1),2, Status.actionStatus.ATTACK, p0);
        System.out.println(aa0.attackTerritory());
        mp.getTerritory(2).setNumUnits(10);
        AttackAction aa1 = new AttackAction(mp.getTerritory(2), mp.getTerritory(4), 6, Status.actionStatus.ATTACK, p0);
        System.out.println(aa1.attackTerritory());
        System.out.println(mp.getTerritory(2).getNumUnits());
        System.out.println(mp.getTerritory(2).getOwner());
        System.out.println(mp.getTerritory(3).getNumUnits());
        System.out.println(mp.getTerritory(3).getOwner());
    }

}

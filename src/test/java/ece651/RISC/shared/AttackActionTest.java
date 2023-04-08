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
        mp.getArea(0).setOwner(p0);
        mp.getArea(1).setOwner(p0);
        mp.getArea(2).setOwner(p0);
        mp.getArea(3).setOwner(p1);
        mp.getArea(4).setOwner(p1);
        mp.getArea(5).setOwner(p1);
        mp.getArea(6).setOwner(p2);
        mp.getArea(7).setOwner(p2);
        mp.getArea(8).setOwner(p2);
        for(Territory t: mp.getAllAreas()){
            t.setNumUnits(5);
        }
        mp.updateAccessible();
        //attack self
        AttackAction aa0 = new AttackAction(mp.getArea(0), mp.getArea(1),2, Status.actionStatus.ATTACK, p0);
        System.out.println(aa0.attackTerritory());
        mp.getArea(2).setNumUnits(10);
        AttackAction aa1 = new AttackAction(mp.getArea(2), mp.getArea(4), 6, Status.actionStatus.ATTACK, p0);
        System.out.println(aa1.attackTerritory());
        System.out.println(mp.getArea(2).getNumUnits());
        System.out.println(mp.getArea(2).getOwner());
        System.out.println(mp.getArea(3).getNumUnits());
        System.out.println(mp.getArea(3).getOwner());
    }

}

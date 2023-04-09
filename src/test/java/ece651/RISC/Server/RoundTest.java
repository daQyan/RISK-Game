package ece651.RISC.Server;

import ece651.RISC.shared.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class RoundTest {
    @Test
    public void testAttackExecution(){
        GameMap mp = new MapFactory().createMap(3);
        Player p0 = new Player("0");
        Player p1 = new Player("1");
        Player p2 = new Player("2");
        ArrayList<Player> players = new ArrayList<>();
        players.add(p0);
        players.add(p1);
        players.add(p2);
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
        Round r = new Round(players, mp, null);
        AttackAction aa0 = new AttackAction(mp.getTerritory(1), mp.getTerritory(7),3, Status.actionStatus.ATTACK, p0);
        AttackAction aa1 = new AttackAction(mp.getTerritory(4), mp.getTerritory(7), 3, Status.actionStatus.ATTACK, p1);
        ArrayList<AttackAction> tests1 = new ArrayList<>();
        tests1.add(aa0);
        tests1.add(aa1);
        r.executeAttacks(tests1);

        System.out.println(mp.getTerritory(1).getNumUnits());
        System.out.println(mp.getTerritory(1).getOwner());
        System.out.println(mp.getTerritory(4).getNumUnits());
        System.out.println(mp.getTerritory(4).getOwner());
        System.out.println(mp.getTerritory(7).getNumUnits());
        System.out.println(mp.getTerritory(7).getOwner());

        System.out.println("---------------------------------------------");
        mp.updateAccessible();
        System.out.println(mp.getTerritory(0).getNumUnits());
        System.out.println(mp.getTerritory(0).getOwner());
        System.out.println(mp.getTerritory(6).getNumUnits());
        System.out.println(mp.getTerritory(6).getOwner());
        AttackAction aa2 = new AttackAction(mp.getTerritory(0), mp.getTerritory(6),5, Status.actionStatus.ATTACK, p0);
        AttackAction aa3 = new AttackAction(mp.getTerritory(6), mp.getTerritory(0), 5, Status.actionStatus.ATTACK, p2);
        ArrayList<AttackAction> tests2 = new ArrayList<>();
        tests2.add(aa2);
        tests2.add(aa3);
        r.executeAttacks(tests2);
        //System.out.println(aa2.attackTerritory());
        System.out.println(mp.getTerritory(0).getNumUnits());
        System.out.println(mp.getTerritory(0).getOwner());
        System.out.println(mp.getTerritory(6).getNumUnits());
        System.out.println(mp.getTerritory(6).getOwner());
    }
}

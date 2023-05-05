package ece651.RISC.Server;

import ece651.RISC.Server.Service.Round;
import ece651.RISC.shared.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class RoundTest {
    @Test
    public void testAttackExecution(){
        GameMap mp = new MapFactory().createMap(3);
        Player p0 = new Player(0,"0");
        Player p1 = new Player(1,"1");
        Player p2 = new Player(2,"2");
        Player p4 = new Player(4, "4");
        ArrayList<Player> players = new ArrayList<>();
        players.add(p0);
        players.add(p1);
        players.add(p2);
        mp.getTerritory(0).setOwner(p0);
        mp.getTerritory(1).setOwner(p0);
        mp.getTerritory(2).setOwner(p0);
        ArrayList<Territory> tp0 = new ArrayList<>();
        tp0.add(mp.getTerritory(0));
        tp0.add(mp.getTerritory(1));
        tp0.add(mp.getTerritory(2));
        p0.setTerritories(tp0);
        mp.getTerritory(3).setOwner(p1);
        mp.getTerritory(4).setOwner(p1);
        mp.getTerritory(5).setOwner(p1);
        ArrayList<Territory> tp1 = new ArrayList<>();
        tp1.add(mp.getTerritory(3));
        tp1.add(mp.getTerritory(4));
        tp1.add(mp.getTerritory(5));
        p1.setTerritories(tp1);
        mp.getTerritory(6).setOwner(p2);
        mp.getTerritory(7).setOwner(p2);
        mp.getTerritory(8).setOwner(p2);
        ArrayList<Territory> tp2 = new ArrayList<>();
        tp2.add(mp.getTerritory(6));
        tp2.add(mp.getTerritory(7));
        tp2.add(mp.getTerritory(8));
        p2.setTerritories(tp2);
        for(Territory t: mp.getTerritories()){
            t.setNumUnits(10);
            t.updateMyUnits(0, 10);
        }
        mp.updateAccessible();
        Round r = new Round(players, mp, 5);
        mp.getTerritory(2).setAllyOwner(p4);
        mp.getTerritory(4).setNumUnits(5);
        ArrayList<Integer> t2Units = new ArrayList<>(Arrays.asList(0, 0, 0, 2, 3, 0, 5));
        ArrayList<Integer> t4Units = new ArrayList<>(Arrays.asList(0, 0, 0, 2, 3, 0, 0));
        ArrayList<Integer> t4AllyUnits = new ArrayList<>(Arrays.asList(0, 0, 0, 2, 3, 0, 0));
        mp.getTerritory(4).setAllyUnits(t4AllyUnits);
        mp.getTerritory(4).setNumAllyUnits(5);
        mp.getTerritory(2).setMyUnits(t2Units);
        mp.getTerritory(4).setMyUnits(t4Units);
        mp.getTerritory(4).setAllyOwner(p2);
        //ArrayList<Integer> deploy = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 3, 0, 0));
        //AttackAction aa0 = new AttackAction(mp.getTerritory(1), mp.getTerritory(7),3, Status.actionStatus.ATTACK, p0);
        AttackAction aa1 = new AttackAction(mp.getTerritory(2), mp.getTerritory(4), 3, Status.actionStatus.ATTACK, p0);
        ArrayList<AttackAction> tests1 = new ArrayList<>();
        //tests1.add(aa0);
        tests1.add(aa1);
        r.executeAttacks(tests1);

        //System.out.println(mp.getTerritory(1).getOwner().getName() + " " +  mp.getTerritory(1).getNumUnits());
        System.out.println(mp.getTerritory(2).getOwner().getName() + " " +  mp.getTerritory(2).getNumUnits());
        System.out.println(mp.getTerritory(4).getOwner().getName() + " " +  mp.getTerritory(4).getNumUnits());


        System.out.println("---------------------------------------------");
        mp.updateAccessible();
        System.out.println(mp.getTerritory(0).getOwner().getName() + " " +  mp.getTerritory(0).getNumUnits());
        System.out.println(mp.getTerritory(6).getOwner().getName() + " " +  mp.getTerritory(6).getNumUnits());
        ArrayList<Integer> t0Units = new ArrayList<>(Arrays.asList(0, 0, 0, 2, 3, 0, 5));
        ArrayList<Integer> t6Units = new ArrayList<>(Arrays.asList(0, 0, 0, 2, 3, 0, 5));
        mp.getTerritory(0).setMyUnits(t0Units);
        mp.getTerritory(6).setMyUnits(t6Units);
        mp.getTerritory(6).setAllyOwner(p0);
        mp.getTerritory(0).setAllyOwner(p2);
        p0.setAllyPlayer(p2);
        p2.setAllyPlayer(p0);
        //ArrayList<Integer> deploy1 = new ArrayList<>(Arrays.asList(0, 0, 0, 2, 3, 0, 5));
        //ArrayList<Integer> deploy2 = new ArrayList<>(Arrays.asList(0, 0, 0, 2, 3, 0, 5));
        AttackAction aa2 = new AttackAction(mp.getTerritory(0), mp.getTerritory(6),10, Status.actionStatus.ATTACK, p0);
        AttackAction aa3 = new AttackAction(mp.getTerritory(6), mp.getTerritory(0), 10, Status.actionStatus.ATTACK, p2);
        ArrayList<AttackAction> tests2 = new ArrayList<>();
        tests2.add(aa2);
        tests2.add(aa3);
        r.executeAttacks(tests2);
        System.out.println(mp.getTerritory(0).getOwner().getName() + " " +  mp.getTerritory(0).getNumUnits());
        System.out.println(mp.getTerritory(6).getOwner().getName() + " " +  mp.getTerritory(6).getNumUnits());

        //multiple territories from one player attack the same target territory
        System.out.println("---------------------------------------------");
        mp.updateAccessible();
        ArrayList<AttackAction> tests3 = new ArrayList<>();
        ArrayList<Integer> t1Units = new ArrayList<>(Arrays.asList(0, 0, 0, 2, 3, 0, 5));
        ArrayList<Integer> t7Units = new ArrayList<>(Arrays.asList(0, 0, 0, 2, 3, 0, 5));
        mp.getTerritory(1).setMyUnits(t1Units);
        mp.getTerritory(7).setMyUnits(t7Units);

        //ArrayList<Integer> deploy3 = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 5));
        //ArrayList<Integer> deploy4 = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 5));
        AttackAction aa4 = new AttackAction(mp.getTerritory(1), mp.getTerritory(7),5, Status.actionStatus.ATTACK, p0);
        AttackAction aa5 = new AttackAction(mp.getTerritory(2), mp.getTerritory(7), 5, Status.actionStatus.ATTACK, p0);
        tests3.add(aa4);
        tests3.add(aa5);
        r.executeAttacks(tests3);
        System.out.println(mp.getTerritory(1).getOwner().getName() + " " +  mp.getTerritory(1).getNumUnits());
        System.out.println(mp.getTerritory(2).getOwner().getName() + " " +  mp.getTerritory(2).getNumUnits());
        System.out.println(mp.getTerritory(7).getOwner().getName() + " " +  mp.getTerritory(7).getNumUnits());
    }
    @Test
    public void testMoveExecution(){
        GameMap mp = new MapFactory().createMap(3);
        Player p0 = new Player(0,"0");
        Player p1 = new Player(1,"1");
        Player p2 = new Player(2,"2");
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
            t.setNumUnits(10);
        }
        mp.updateAccessible();
//        Round r = new Round(players, mp, null);
//        ArrayList<Integer> t0Units = new ArrayList<>(Arrays.asList(0, 0, 0, 2, 3, 0, 5));
//        ArrayList<Integer> t1Units = new ArrayList<>(Arrays.asList(0, 0, 0, 2, 3, 0, 5));
//        mp.getTerritory(0).setMyUnits(t0Units);
//        mp.getTerritory(1).setMyUnits(t1Units);
//        MoveAction ma = new MoveAction(mp.getTerritory(0), mp.getTerritory(1), 5, Status.actionStatus.MOVE, p0);
//        ArrayList<MoveAction> tests1 = new ArrayList<>();
//        tests1.add(ma);
//        r.executeMoves(tests1);
    }
}

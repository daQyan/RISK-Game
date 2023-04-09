package ece651.RISC.shared;

import ece651.RISC.Server.MapFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameMapTest {
    @Test
    public void testSearchAccessible(){
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
        mp.updateAccessible();
        for(Territory t: mp.getTerritories()){
            ArrayList<Territory> a = t.getAccessibles();
            for(int i = 0; i < a.size(); ++i){
                System.out.print(a.get(i).getName());
                System.out.print(" ");
            }
            System.out.print("\n");
        }
        mp.getTerritory(8).setOwner(p0);
        mp.updateAccessible();
        System.out.print("\n");
        for(Territory t: mp.getTerritories()){
            ArrayList<Territory> a = t.getAccessibles();
            for(int i = 0; i < a.size(); ++i){
                System.out.print(a.get(i).getName());
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }
}

package ece651.RISC.Client;

import ece651.RISC.Server.MapFactory;
import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.Player;
import org.junit.jupiter.api.Test;

public class MapTextViewTest {
    @Test
    public void testMapTextView(){
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
        MapTextView mtv = new MapTextView(mp);
        mtv.displayMap();
    }
}

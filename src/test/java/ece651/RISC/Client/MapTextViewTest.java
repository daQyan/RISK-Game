package ece651.RISC.Client;

import ece651.RISC.Server.MapFactory;
import ece651.RISC.shared.GameMap;
import org.junit.jupiter.api.Test;

public class MapTextViewTest {
    @Test
    public void testMapTextView(){
        GameMap mp = new MapFactory().createMap(3);
        MapTextView mtv = new MapTextView(mp);
        mtv.displayMap();
    }
}

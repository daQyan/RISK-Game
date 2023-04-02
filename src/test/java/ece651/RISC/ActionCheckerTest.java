package ece651.RISC;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ActionCheckerTest {
    private ArrayList<Territory> createTestHelper(){
        Player p0 = new Player(0, null);
        Player p1 = new Player(1, null);
        ArrayList<Territory> t = new ArrayList<>();
        Territory t0 = new Territory("t0", p0, 5, null, null);
        Territory t1 = new Territory("t1", p1, 4, null, null);
        Territory t2 = new Territory("t2", p0, 3, null, null);
        t.add(t0);
        t.add(t1);
        t.add(t2);
        return t;
    }
    @Test
    public void testMoveRule(){


    }
    @Test
    public void testAttackRule(){

    }


}

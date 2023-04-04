package ece651.RISC.Client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {
@Test
    void test_toString(){
    Territory t1 = new Territory("Narnia", 10);
    Territory t2 = new Territory("Midkemia", 10);
    t1.addAdjacent(t2);
    assertEquals(t1.toString(), "10 units in Narnia (next to: Midkemia)");
    Territory t3 = new Territory("Elantris", 10);
    t1.addAdjacent(t3);
    assertEquals(t1.toString(), "10 units in Narnia (next to: Elantris, Midkemia)");
}
}
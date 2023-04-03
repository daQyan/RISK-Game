package ece651.RISC.Client;

import ece651.RISC.Client.Terriory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerrioryTest {
@Test
    void test_toString(){
    Terriory t1 = new Terriory("Narnia", 10, null);
    Terriory t2 = new Terriory("Midkemia", 10, null);
    t1.addAdjacent(t2);
    assertEquals(t1.toString(), "10 units in Narnia (next to: Midkemia)");
    Terriory t3 = new Terriory("Elantris", 10, null);
    t1.addAdjacent(t3);
    assertEquals(t1.toString(), "10 units in Narnia (next to: Elantris, Midkemia)");
}
}
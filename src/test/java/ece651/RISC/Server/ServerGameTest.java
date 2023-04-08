package ece651.RISC.Server;

import ece651.RISC.shared.*;
import org.apache.catalina.Server;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class ServerGameTest {
    @Test
    public void testExecuteMoves(){
        MapFactory mf = new MapFactory();
        GameMap testMap = mf.createMap(2);
        Player p0 = new Player("p0");
        Player p1 = new Player("p1");
        ServerGame testSG = new ServerGame(2, 8, null);

        ArrayList<MoveAction> testMove = new ArrayList<>();
        //testSG.
        Territory t0 = testMap.getArea(0);
        Territory t1 = testMap.getArea(1);
        t0.setNumUnits(2);
        t1.setNumUnits(1);
        t0.setOwner(p0);
        t1.setOwner(p0);
        MoveAction mv0 = new MoveAction(testMap.getArea(0), testMap.getArea(1), 1, Status.actionStatus.MOVE, p0);
        testMove.add(mv0);
        assertEquals();
        t1.setOwner(p1);
        //assert
        //assert
        MoveAction mv1 = new MoveAction(testMap.getArea(0), testMap.getArea(1), 1, Status.actionStatus.MOVE, p0);

    }
    @Test
    public void testExecuteAttacks(){
        ServerGame testSG = new ServerGame();
        ArrayList<AttackAction> testAttack = new ArrayList<>();

    }
}

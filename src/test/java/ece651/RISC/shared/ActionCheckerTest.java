package ece651.RISC.shared;

import ece651.RISC.shared.AttackAction;
import ece651.RISC.shared.MoveAction;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Status;
import ece651.RISC.shared.Territory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class ActionCheckerTest {
    private ArrayList<Territory> createTestHelper(){
//        Player p0 = new Player(0, "0", null);
//        Player p1 = new Player(1, "1", null);
//        ArrayList<Territory> t = new ArrayList<>();
//        Territory t0 = new Territory(0, "t0", 5, p0);
//        Territory t1 = new Territory(1, "t1", 4,p1);
//        Territory t2 = new Territory(2, "t2", 3, p0);
//        t.add(t0);
//        t.add(t1);
//        t.add(t2);
        return null;
    }

    @Test
    public void testMoveRule(){
        ArrayList<Territory> temp = createTestHelper();
        MoveAction mv0 = new MoveAction(temp.get(0), temp.get(1), 1, Status.actionStatus.MOVE, null);
        MoveAction mv1 = new MoveAction(temp.get(0), temp.get(1), 6, Status.actionStatus.MOVE, null);
        assertEquals(mv0.moveTerritory(), "The move action is not valid: there's no valid path from t0 to t1!");
        temp.get(0).addAccessible(temp.get(1), 2);
        assertEquals(mv1.moveTerritory(), "The move action is not valid: there's not enough soldiers in the t0!");
        assertNull(mv0.moveTerritory());
    }
    @Test
    public void testAttackRule(){
        ArrayList<Territory> temp = createTestHelper();
        AttackAction a0 = new AttackAction(temp.get(0), temp.get(1), 2, Status.actionStatus.ATTACK, null);
        AttackAction a1 = new AttackAction(temp.get(0), temp.get(2), 2, Status.actionStatus.ATTACK, null);
        AttackAction a2 = new AttackAction(temp.get(0), temp.get(1), 10, Status.actionStatus.ATTACK, null);
        assertEquals(a0.attackTerritory(), "The attack action is invalid: unable to attack directly from t0 to t1!");
        temp.get(0).addAdjacent(temp.get(1));
        assertEquals(a2.attackTerritory(), "The attack action is invalid: there's not enough soldier in t0 to deploy!");
        temp.get(0).addAdjacent(temp.get(2));
        assertEquals(a1.attackTerritory(), "The attack action is invalid: cannot attack your own territory!");
    }


}

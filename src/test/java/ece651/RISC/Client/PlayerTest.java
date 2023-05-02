package ece651.RISC.Client;

import ece651.RISC.shared.Player;
import org.junit.jupiter.api.Test;

class PlayerTest {
    @Test
    void testToString() {
        Player p = new Player();
        p.updateTechResource(300);
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource() + "\n");

        p.upgradeTechLevel();
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource());
        p.upgradeTechLevel();
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource());
        p.upgradeTechLevel();
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource());
        p.upgradeTechLevel();
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource());
        p.upgradeTechLevel();
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource());
    }
}
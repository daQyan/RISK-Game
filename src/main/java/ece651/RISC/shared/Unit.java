package ece651.RISC.shared;

import java.util.ArrayList;

public class Unit {
    private int myType;
    private int[] diction;

    public Unit() {
        this.myType = 0;
        diction = new int[] {0, 3, 11, 30, 55, 90, 140};
    }
    public Unit(int Type) {
        this.myType = Type;
        diction = new int[] {0, 3, 11, 30, 55, 90, 140};
    }

    // upgrade the unit based on the given new level
    public void upgradeUnit(int newType, Player owner){
        int cost = getUnitUpgradeCost(newType, owner);
        //for checker:
        //use checker to check if the resource is enough to upgrade
        //check if upgraded unit has higher precedence than the territory does
        //check if upgraded unit will have precedence larger than 6(0 at first)
        myType = newType;
        owner.updateTechResource(-cost);
    }

    private int getUnitUpgradeCost(int newType, Player owner) {
        // check tech level
        if (newType <= this.myType) {
            throw new IllegalArgumentException("cannot upgrade to a lower level !");
        }
        if (newType > 6) {
            throw new IllegalArgumentException("Upgrade failed, you have reached max Unit Level !");
        }
        if (owner.techLevel < newType) {
            throw new IllegalArgumentException("Upgrade failed, your chosen level is beyond your tech Level: !" + owner.getTechLevel());
        }
        // check resource
        int cost = diction[newType] - diction[this.myType];
        if (cost > owner.getTechResource()) {
            throw new IllegalArgumentException("Upgrade failed, your resource: " +
                    owner.getTechResource() + " is not enough, you need " + cost + "resources !");
        }
        return cost;
    }

    public static void main(String[] args) {
        Player p = new Player();
        p.updateTechResource(300);
        Unit u1 = new Unit();
        Unit u2= new Unit();
        Unit u3 = new Unit(2);

        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource() + ", unit1: " + u1.myType);
        u1.upgradeUnit(1, p);
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource() + ", unit2: " + u1.myType);
        p.upgradeTechLevel();
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource() + ", unit2: " + u2.myType);
        u2.upgradeUnit(2, p);
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource() + ", unit2: " + u2.myType);
        p.upgradeTechLevel();
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource() + ", unit3: " + u3.myType);
        u3.upgradeUnit(3, p);
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource() + ", unit3: " + u3.myType);

    }

}

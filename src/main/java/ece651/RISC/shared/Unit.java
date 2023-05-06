package ece651.RISC.shared;

/**
 * Unit class represents the level of military units that a player can have in a territory.
 * It also handles the upgrading of the units to a higher level by a player.
 */
public class Unit {
    private int myType;
    private int[] diction;
    private int[] bonus;

    public int[] getDiction() {
        return diction;
    }

    public int[] getBonus() {
        return bonus;
    }

    public Unit() {
        this.myType = 0;
        diction = new int[]{0, 3, 11, 30, 55, 90, 140};
        bonus = new int[]{0, 1, 3, 5, 8, 11, 15};
    }

    /**
     * Constructor that initializes the unit with the given level
     *
     * @param Type the level of the unit
     */
    public Unit(int Type) {
        this.myType = Type;
        diction = new int[]{0, 3, 11, 30, 55, 90, 140};
        bonus = new int[]{0, 1, 3, 5, 8, 11, 15};
    }

    /**
     * Method to upgrade the unit to a higher level
     *
     * @param newType the level to which the unit needs to be upgraded
     * @param owner   the player who is upgrading the unit
     */
    public void upgradeUnit(int newType, Player owner) {
        int cost = getUnitUpgradeCost(newType, owner);
        //for checker:
        //use checker to check if the resource is enough to upgrade
        //check if upgraded unit has higher precedence than the territory does
        //check if upgraded unit will have precedence larger than 6(0 at first)
        myType = newType;
        owner.updateTechResource(-cost);
    }

    public int getMyType() {
        return myType;
    }

    public int getBonusByType(int type) {
        return bonus[type];
    }

    /**
     * Method to calculate the cost of upgrading a unit to a higher level
     *
     * @param newType the level to which the unit needs to be upgraded
     * @param owner   the player who is upgrading the unit
     * @return the cost of the unit upgrade
     */
    private int getUnitUpgradeCost(int newType, Player owner) {
        // check tech level
        if (newType <= this.myType) {
            throw new IllegalArgumentException("cannot upgrade to a lower level !");
        }
        if (newType > 6) {
            throw new IllegalArgumentException("Upgrade failed, you have reached max Unit Level !");
        }
//        if (owner.techLevel < newType) {
//            throw new IllegalArgumentException("Upgrade failed, your chosen level is beyond your tech Level: !" + owner.getTechLevel());
//        }
        // check resource
        int cost = diction[newType] - diction[this.myType];
        if (cost > owner.getTechResource()) {
            throw new IllegalArgumentException("Upgrade failed, your resource: " +
                    owner.getTechResource() + " is not enough, you need " + cost + "resources !");
        }
        return cost;
    }

    /**
     * Method to check if two units are equal
     *
     * @param rhs the other unit to be compared with
     * @return true if the units are equal, false otherwise
     */
    public Boolean equals(Unit rhs) {
        return this.myType == rhs.myType;
    }

    public static void main(String[] args) {
        Player p = new Player();
        p.updateTechResource(300);
        Unit u1 = new Unit();
        Unit u2 = new Unit();
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

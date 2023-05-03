package ece651.RISC.shared;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class UpgradeUnitAction {
    private int oldType;
    private int newType;
    private int unitNum;

    @JSONField(serializeUsing = Territory.class)
    private Territory territory;

    public UpgradeUnitAction(int oldType, int newType, int unitNum, Territory territory) {
        this.oldType = oldType;
        this.newType = newType;
        this.unitNum = unitNum;
        this.territory = territory;
    }

    public void upgradeUnitLevel(){
        int cost = getUnitUpgradeCost(oldType, newType, territory.getOwner(), unitNum);
        territory.getOwner().updateTechResource(-cost);
        territory.updateMyUnits(oldType, -unitNum);
        territory.updateMyUnits(newType, unitNum);
    }


    private int getUnitUpgradeCost(int oldType, int newType, Player owner, int unitNum) {
        // check tech level
        Unit unitReference = new Unit();
        int[] diction = unitReference.getDiction();
        int oldunits = territory.getMyUnits().get(oldType);
        if (oldunits < unitNum) {
            throw new IllegalArgumentException("cannot upgrade! you need " + unitNum + " of " + oldType + " but you only have " +
                  + oldunits +  " !" );
        }
        if (newType <= oldType) {
            throw new IllegalArgumentException("cannot upgrade to a lower level !");
        }
        if (newType > 6) {
            throw new IllegalArgumentException("Upgrade failed, you have reached max Unit Level !");
        }
        if (owner.techLevel < newType) {
            throw new IllegalArgumentException("Upgrade failed, your chosen level is beyond your tech Level: !" + owner.getTechLevel());
        }
        // check resource
        int cost = unitNum * (diction[newType] - diction[oldType]);
        if (cost > owner.getTechResource()) {
            throw new IllegalArgumentException("Upgrade failed, your resource: " +
                    owner.getTechResource() + " is not enough, you need " + cost + "resources !");
        }
        return cost;
    }

    public static void main(String[] args) {
        Player p = new Player();
        p.updateTechResource(500);
        p.upgradeTechLevel();
        p.upgradeTechLevel();
        p.upgradeTechLevel();
        p.upgradeTechLevel();

        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource());
        Territory t = new Territory(1, "haha", 8, p);

        System.out.println(t.getMyUnits());
        UpgradeUnitAction upg = new UpgradeUnitAction(0, 1, 4, t);
        upg.upgradeUnitLevel();

        System.out.println(t.getMyUnits());
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource());

        UpgradeUnitAction upg2 = new UpgradeUnitAction(1, 2, 3, t);
        UpgradeUnitAction upg3 = new UpgradeUnitAction(2, 5, 2, t);
        upg2.upgradeUnitLevel();
        System.out.println(t.getMyUnits());
        upg3.upgradeUnitLevel();
        System.out.println(t.getMyUnits());
        System.out.println("tech level: " + p.getTechLevel() + ", techResource: " + p.getTechResource());

    }

}

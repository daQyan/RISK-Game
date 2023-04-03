package ece651.RISC.Server;

import ece651.RISC.Server.Player;
import ece651.RISC.shared.Action;
import ece651.RISC.shared.Territory;

public class AttackAction extends Action {

    private Player attacker;
    public AttackAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits) {
        super(sourceTerritory, targetTerritory, hitUnits);
        attacker = sourceTerritory.getOwner();
    }

    /**
     * attack the territory from the chosen territory
     */
    public String attackTerritory() {
        // check adjacent and not owned
        // see if the owner is changed
        if (sourceTerritory.getOwner() == attacker) {
            String checkAttack = myAC.checkAttackRule(this.sourceTerritory, this.targetTerritory, hitUnits);
            if(checkAttack == null){
                sourceTerritory.updateUnits(-hitUnits);
                targetTerritory.updateUnits(hitUnits);
            }

            if (targetTerritory.getUnit() < 0) {
                checkAttack = "Owner Changed";
            }
            return checkAttack;
        }
        return "Attack failed because of owner switch!";
    }
}

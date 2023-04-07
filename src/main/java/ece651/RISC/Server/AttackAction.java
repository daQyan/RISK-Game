package ece651.RISC.Server;

import ece651.RISC.Combat;
import ece651.RISC.Status;
import ece651.RISC.shared.Action;
import ece651.RISC.shared.Territory;
import ece651.RISC.shared.Player;

public class AttackAction extends Action {
    private Combat myCombat;
    public AttackAction(Territory sourceTerritory, Territory targetTerritory, int hitUnits, Status.actionStatus type, Player owner) {
        super(sourceTerritory, targetTerritory, hitUnits, type, owner);
    }

    /**
     * attack the territory from the chosen territory
     */
    public String attackTerritory() {
        String checkAttack = myAC.checkAttackRule(this.owner, this.sourceTerritory, this.targetTerritory, hitUnits);
        if(checkAttack == null){
            while(hitUnits > 0 && targetTerritory.getNumUnits() >= 0){
                if(myCombat.rollCombatDice() == true){
                    targetTerritory.updateUnits(-1);
                }
                else{
                    --hitUnits;
                    sourceTerritory.updateUnits(-1);
                }
            }
            // if the attack wins
            if (targetTerritory.getNumUnits() < 0) {
                targetTerritory.setOwner(this.owner);
                targetTerritory.setNumUnits(hitUnits);
                checkAttack = this.owner.getName() + " has taken over " + targetTerritory.getName() + "!";
            }
        }
        return checkAttack;
    }
}

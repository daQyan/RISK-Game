package ece651.RISC.shared.Model;

import java.util.List;

public class ActionsRequest {
    private List<MoveAction> moveActions;
    private List<AttackAction> attackActions;
    private List<UpgradeAction> upgradeActions;

    public List<MoveAction> getMoveActions() {
        return moveActions;
    }

    public void setMoveActions(List<MoveAction> moveActions) {
        this.moveActions = moveActions;
    }

    public List<AttackAction> getAttackActions() {
        return attackActions;
    }

    public void setAttackActions(List<AttackAction> attackActions) {
        this.attackActions = attackActions;
    }

    public List<UpgradeAction> getUpgradeActions() {
        return upgradeActions;
    }

    public void setUpgradeActions(List<UpgradeAction> upgradeActions) {
        this.upgradeActions = upgradeActions;
    }
}



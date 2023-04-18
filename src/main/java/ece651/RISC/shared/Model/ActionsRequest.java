package ece651.RISC.shared.Model;

import java.util.List;

public class ActionsRequest {
    private List<RequestMoveAction> moveActions;
    private List<RequestAttackAction> attackActions;
    private List<ReqeustUpgradeAction> upgradeActions;

    public List<RequestMoveAction> getMoveActions() {
        return moveActions;
    }

    public void setMoveActions(List<RequestMoveAction> moveActions) {
        this.moveActions = moveActions;
    }

    public List<RequestAttackAction> getAttackActions() {
        return attackActions;
    }

    public void setAttackActions(List<RequestAttackAction> attackActions) {
        this.attackActions = attackActions;
    }

    public List<ReqeustUpgradeAction> getUpgradeActions() {
        return upgradeActions;
    }

    public void setUpgradeActions(List<ReqeustUpgradeAction> upgradeActions) {
        this.upgradeActions = upgradeActions;
    }
}



package ece651.RISC.shared;

import com.alibaba.fastjson2.annotation.JSONField;

public class UpgradeTechAction {
    @JSONField(name = "playerID")
    private int PlayerID;

    public UpgradeTechAction(int playerID) {
        PlayerID = playerID;
    }

    public void upgradeTechLevel(Player p){
        p.upgradeTechLevel();
    }

    public int getPlayerID() {
        return PlayerID;
    }
}

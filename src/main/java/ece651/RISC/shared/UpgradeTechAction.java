package ece651.RISC.shared;

public class UpgradeTechAction {
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

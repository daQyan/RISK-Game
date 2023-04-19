package ece651.RISC.shared.Model;

public class RequestMoveAction {
    private String sourceTerritoryId;
    private String destinationTerritoryId;
    private int unitCount;

    public String getSourceTerritoryId() {
        return sourceTerritoryId;
    }

    public void setSourceTerritoryId(String sourceTerritoryId) {
        this.sourceTerritoryId = sourceTerritoryId;
    }

    public String getDestinationTerritoryId() {
        return destinationTerritoryId;
    }

    public void setDestinationTerritoryId(String destinationTerritoryId) {
        this.destinationTerritoryId = destinationTerritoryId;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }
}
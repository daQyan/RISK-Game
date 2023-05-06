package ece651.RISC.shared;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.ArrayList;

/**
 * TerritoryRelation represents a single territory and its related territories.
 * It stores the ID of the territory and an ArrayList of related territory IDs.
 */
public class TerritoryRelation {
    @JSONField(name = "id")
    private int selfId;
    @JSONField(name = "relates")
    private ArrayList<Integer> relatedIds = new ArrayList<>();

    public int getSelfId() {
        return selfId;
    }

    public void setSelfId(int selfId) {
        this.selfId = selfId;
    }

    public void setRelatedIds(ArrayList<Integer> relatedIds) {
        this.relatedIds = relatedIds;
    }

    public ArrayList<Integer> getRelatedIds() {
        return relatedIds;
    }

    /**
     * Constructor for TerritoryRelation with only selfId initialized.
     *
     * @param selfId the ID of the territory itself
     */

    public TerritoryRelation(int selfId) {
        this.selfId = selfId;
        this.relatedIds = new ArrayList<>();
    }

    /**
     * Add a related territory ID to the list of relatedIds.
     *
     * @param relatedId the ID of the related territory
     */

    public void addRelated(int relatedId) {
        relatedIds.add(relatedId);
    }
}

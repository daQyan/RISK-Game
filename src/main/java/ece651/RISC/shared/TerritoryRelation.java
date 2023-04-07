package ece651.RISC.shared;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.ArrayList;

public class TerritoryRelation {
    @JSONField(name = "id")
    private int selfId;
    @JSONField(name = "relates")
    private ArrayList<Integer> relatedIds;

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

    public TerritoryRelation(int selfId) {
        this.selfId = selfId;
    }

    public void addRelated(int relatedId) {
        relatedIds.add(relatedId);
    }
}

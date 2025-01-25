package com.example.model;

public class Zone {
    private int zoneOrder;
    private String zoneKey;
    public Zone() {}
    public Zone(int zoneOrder, String zoneKey) {
        this.zoneOrder = zoneOrder;
        this.zoneKey = zoneKey;
    }
    public int getZoneOrder() {
        return zoneOrder;
    }
    public void setZoneOrder(int zoneOrder) {
        this.zoneOrder = zoneOrder;
    }
    public String getZoneKey() {
        return zoneKey;
    }
    public void setZoneKey(String zoneKey) {
        this.zoneKey = zoneKey;
    }
}
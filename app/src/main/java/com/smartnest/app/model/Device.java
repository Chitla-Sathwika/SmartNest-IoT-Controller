package com.smartnest.app.model;

public class Device {
    private String id;
    private String name;
    private String icon;
    private boolean isOn;
    private long lastUpdated;

    public Device() {}

    public Device(String id, String name, String icon, boolean isOn) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.isOn = isOn;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public boolean isOn() { return isOn; }
    public void setOn(boolean on) { isOn = on; }

    public long getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
}
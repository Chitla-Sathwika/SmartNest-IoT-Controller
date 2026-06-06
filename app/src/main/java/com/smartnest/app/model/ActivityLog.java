package com.smartnest.app.model;

public class ActivityLog {
    private String id;
    private String deviceName;
    private String action;
    private long timestamp;

    public ActivityLog() {}

    public ActivityLog(String id, String deviceName, String action, long timestamp) {
        this.id = id;
        this.deviceName = deviceName;
        this.action = action;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
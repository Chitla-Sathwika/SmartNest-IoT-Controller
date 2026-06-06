package com.smartnest.app.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity_logs")
public class ActivityLogEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String deviceName;
    public String action;
    public long timestamp;

    public ActivityLogEntity(String deviceName, String action, long timestamp) {
        this.deviceName = deviceName;
        this.action = action;
        this.timestamp = timestamp;
    }
}
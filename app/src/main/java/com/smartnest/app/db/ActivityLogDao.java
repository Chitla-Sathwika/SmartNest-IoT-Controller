package com.smartnest.app.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ActivityLogDao {

    @Insert
    void insert(ActivityLogEntity log);

    @Query("SELECT * FROM activity_logs ORDER BY timestamp DESC")
    LiveData<List<ActivityLogEntity>> getAllLogs();

    @Query("DELETE FROM activity_logs")
    void deleteAll();
}
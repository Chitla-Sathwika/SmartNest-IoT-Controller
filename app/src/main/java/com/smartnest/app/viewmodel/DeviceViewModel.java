package com.smartnest.app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smartnest.app.db.ActivityLogEntity;
import com.smartnest.app.model.ActivityLog;
import com.smartnest.app.model.Device;
import com.smartnest.app.repository.DeviceRepository;

import java.util.List;

public class DeviceViewModel extends AndroidViewModel {

    private final DeviceRepository repository;

    public DeviceViewModel(@NonNull Application application) {
        super(application);
        repository = new DeviceRepository(application);
    }

    public LiveData<List<Device>> getDevices() {
        return repository.getDevicesLiveData();
    }

    public LiveData<List<ActivityLog>> getFirebaseLogs() {
        return repository.getFirebaseLogsLiveData();
    }

    public LiveData<List<ActivityLogEntity>> getLocalLogs() {
        return repository.getLocalLogs();
    }

    public void toggleDevice(Device device) {
        repository.toggleDevice(device);
    }

    public void initDevices() {
        repository.initDevices();
    }
}
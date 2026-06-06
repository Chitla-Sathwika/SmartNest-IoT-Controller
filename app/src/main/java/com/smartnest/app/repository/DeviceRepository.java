package com.smartnest.app.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smartnest.app.db.ActivityLogDao;
import com.smartnest.app.db.ActivityLogEntity;
import com.smartnest.app.db.AppDatabase;
import com.smartnest.app.model.ActivityLog;
import com.smartnest.app.model.Device;
import com.smartnest.app.utils.SmartNestFirebaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeviceRepository {

    private final Application application;
    private final DatabaseReference devicesRef;
    private final DatabaseReference logsRef;
    private final ActivityLogDao logDao;
    private final ExecutorService executor;
    private final MutableLiveData<List<Device>> devicesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<ActivityLog>> firebaseLogsLiveData = new MutableLiveData<>();

    public DeviceRepository(Application application) {
        this.application = application;
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        devicesRef = userRef.child("devices");
        logsRef = userRef.child("logs");

        AppDatabase db = AppDatabase.getInstance(application);
        logDao = db.activityLogDao();
        executor = Executors.newSingleThreadExecutor();

        listenToDevices();
        listenToLogs();
    }

    private void listenToDevices() {
        devicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Device> list = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Device device = child.getValue(Device.class);
                    if (device != null) list.add(device);
                }
                devicesLiveData.postValue(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void listenToLogs() {
        logsRef.limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ActivityLog> list = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    ActivityLog log = child.getValue(ActivityLog.class);
                    if (log != null) list.add(0, log);
                }
                firebaseLogsLiveData.postValue(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    public void toggleDevice(Device device) {
        device.setOn(!device.isOn());
        device.setLastUpdated(System.currentTimeMillis());
        devicesRef.child(device.getId()).setValue(device);

        String action = device.isOn() ? "ON" : "OFF";
        String logId = logsRef.push().getKey();
        ActivityLog log = new ActivityLog(logId, device.getName(), action, System.currentTimeMillis());
        logsRef.child(logId).setValue(log);

        executor.execute(() -> logDao.insert(
                new ActivityLogEntity(device.getName(), action, System.currentTimeMillis())
        ));

        SmartNestFirebaseService.sendLocalNotification(
                application.getApplicationContext(),
                device.getName(),
                action
        );
    }

    public void initDevices() {
        devicesRef.get().addOnSuccessListener(snapshot -> {
            if (!snapshot.exists()) {
                String[][] defaults = {
                        {"ac", "AC Unit", "❄️"},
                        {"fan", "Bedroom Fan", "🌀"},
                        {"light", "Living Room Light", "💡"},
                        {"door", "Door Lock", "🔒"}
                };
                for (String[] d : defaults) {
                    Device device = new Device(d[0], d[1], d[2], false);
                    devicesRef.child(d[0]).setValue(device);
                }
            }
        });
    }

    public MutableLiveData<List<Device>> getDevicesLiveData() { return devicesLiveData; }
    public MutableLiveData<List<ActivityLog>> getFirebaseLogsLiveData() { return firebaseLogsLiveData; }
    public LiveData<List<ActivityLogEntity>> getLocalLogs() { return logDao.getAllLogs(); }
}
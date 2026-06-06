package com.smartnest.app.view.dashboard;
import android.os.Build;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smartnest.app.databinding.ActivityDashboardBinding;
import com.smartnest.app.view.auth.LoginActivity;
import com.smartnest.app.viewmodel.DeviceViewModel;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private DeviceViewModel viewModel;
    private DeviceAdapter deviceAdapter;
    private LogAdapter logAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(DeviceViewModel.class);

        setupRecyclerViews();
        observeData();
        setupListeners();

        viewModel.initDevices();
        requestNotificationPermission();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }
    }

    private void setupRecyclerViews() {
        deviceAdapter = new DeviceAdapter(device -> viewModel.toggleDevice(device));
        binding.rvDevices.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDevices.setAdapter(deviceAdapter);

        logAdapter = new LogAdapter();
        binding.rvLogs.setLayoutManager(new LinearLayoutManager(this));
        binding.rvLogs.setAdapter(logAdapter);
    }

    private void observeData() {
        viewModel.getDevices().observe(this, devices -> {
            deviceAdapter.setDevices(devices);
            long activeCount = devices.stream().filter(d -> d.isOn()).count();
            binding.tvActiveCount.setText(activeCount + " device" +
                    (activeCount == 1 ? "" : "s") + " active");
        });

        viewModel.getFirebaseLogs().observe(this, logs -> {
            logAdapter.setLogs(logs);
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String name = email != null ? email.split("@")[0] : "User";
            binding.tvWelcome.setText("Welcome, " + name + " 👋");
        }
    }

    private void setupListeners() {
        binding.btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        binding.btnStats.setOnClickListener(v -> {
            startActivity(new Intent(this, StatsActivity.class));
        });

        binding.btnTheme.setOnClickListener(v -> toggleTheme());
    }

    private void toggleTheme() {
        int currentMode = getResources().getConfiguration().uiMode
                & android.content.res.Configuration.UI_MODE_NIGHT_MASK;

        if (currentMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
                    androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
            binding.btnTheme.setText("🌙");
        } else {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
                    androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES);
            binding.btnTheme.setText("☀️");
        }
    }
}
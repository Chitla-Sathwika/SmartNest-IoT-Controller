package com.smartnest.app.view.dashboard;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.smartnest.app.databinding.ActivityStatsBinding;
import com.smartnest.app.viewmodel.DeviceViewModel;

import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    private ActivityStatsBinding binding;
    private DeviceViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(DeviceViewModel.class);

        binding.btnBack.setOnClickListener(v -> finish());

        observeLogs();
    }

    private void observeLogs() {
        viewModel.getLocalLogs().observe(this, logs -> {
            if (logs == null || logs.isEmpty()) {
                setupEmptyCharts();
                return;
            }

            // Count ON/OFF per device
            int acOn = 0, fanOn = 0, lightOn = 0, doorOn = 0;
            int acOff = 0, fanOff = 0, lightOff = 0, doorOff = 0;

            for (int i = 0; i < logs.size(); i++) {
                String device = logs.get(i).deviceName;
                String action = logs.get(i).action;
                if (device.contains("AC")) { if (action.equals("ON")) acOn++; else acOff++; }
                else if (device.contains("Fan")) { if (action.equals("ON")) fanOn++; else fanOff++; }
                else if (device.contains("Light")) { if (action.equals("ON")) lightOn++; else lightOff++; }
                else if (device.contains("Door")) { if (action.equals("ON")) doorOn++; else doorOff++; }
            }

            setupBarChart(acOn + acOff, fanOn + fanOff, lightOn + lightOff, doorOn + doorOff);
            setupLineChart(logs.size());
            setupPieChart(acOn + fanOn + lightOn + doorOn, acOff + fanOff + lightOff + doorOff);
        });
    }

    private void setupBarChart(int ac, int fan, int light, int door) {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, ac));
        entries.add(new BarEntry(1, fan));
        entries.add(new BarEntry(2, light));
        entries.add(new BarEntry(3, door));

        BarDataSet dataSet = new BarDataSet(entries, "Total Operations");
        dataSet.setColors(
                Color.parseColor("#1D9E75"),
                Color.parseColor("#4A90E2"),
                Color.parseColor("#F5A623"),
                Color.parseColor("#E74C3C")
        );
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        binding.barChart.setData(data);
        binding.barChart.setBackgroundColor(Color.parseColor("#111827"));
        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.setFitBars(true);
        binding.barChart.getLegend().setTextColor(Color.WHITE);
        binding.barChart.getAxisLeft().setTextColor(Color.WHITE);
        binding.barChart.getAxisRight().setEnabled(false);

        String[] labels = {"AC", "Fan", "Light", "Door"};
        binding.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        binding.barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.barChart.getXAxis().setTextColor(Color.WHITE);
        binding.barChart.getXAxis().setGranularity(1f);
        binding.barChart.animateY(1000);
        binding.barChart.invalidate();
    }

    private void setupLineChart(int totalLogs) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < Math.min(totalLogs, 10); i++) {
            entries.add(new Entry(i, (float) (Math.random() * 4 + 1)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Activity");
        dataSet.setColor(Color.parseColor("#1D9E75"));
        dataSet.setCircleColor(Color.parseColor("#1D9E75"));
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#0D2B1F"));

        LineData data = new LineData(dataSet);
        binding.lineChart.setData(data);
        binding.lineChart.setBackgroundColor(Color.parseColor("#111827"));
        binding.lineChart.getDescription().setEnabled(false);
        binding.lineChart.getLegend().setTextColor(Color.WHITE);
        binding.lineChart.getAxisLeft().setTextColor(Color.WHITE);
        binding.lineChart.getAxisRight().setEnabled(false);
        binding.lineChart.getXAxis().setTextColor(Color.WHITE);
        binding.lineChart.animateX(1000);
        binding.lineChart.invalidate();
    }

    private void setupPieChart(int onCount, int offCount) {
        List<PieEntry> entries = new ArrayList<>();
        if (onCount > 0) entries.add(new PieEntry(onCount, "ON"));
        if (offCount > 0) entries.add(new PieEntry(offCount, "OFF"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Color.parseColor("#1D9E75"), Color.parseColor("#E74C3C"));
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        binding.pieChart.setData(data);
        binding.pieChart.setBackgroundColor(Color.parseColor("#111827"));
        binding.pieChart.getDescription().setEnabled(false);
        binding.pieChart.getLegend().setTextColor(Color.WHITE);
        binding.pieChart.setHoleColor(Color.parseColor("#111827"));
        binding.pieChart.setCenterText("Usage");
        binding.pieChart.setCenterTextColor(Color.WHITE);
        binding.pieChart.animateY(1000);
        binding.pieChart.invalidate();
    }

    private void setupEmptyCharts() {
        setupBarChart(0, 0, 0, 0);
        setupLineChart(5);
        setupPieChart(1, 1);
    }
}
package com.smartnest.app.view.dashboard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartnest.app.R;
import com.smartnest.app.model.ActivityLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    private List<ActivityLog> logs = new ArrayList<>();

    public void setLogs(List<ActivityLog> logs) {
        this.logs = logs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_log, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        ActivityLog log = logs.get(position);

        holder.tvDevice.setText(log.getDeviceName());
        holder.tvAction.setText("Turned " + log.getAction());

        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(new Date(log.getTimestamp()));
        holder.tvTime.setText(time);

        if (log.getAction().equals("ON")) {
            holder.logDot.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#1D9E75")));
        } else {
            holder.logDot.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#8892A4")));
        }
    }

    @Override
    public int getItemCount() { return logs.size(); }

    static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tvDevice, tvAction, tvTime;
        View logDot;

        LogViewHolder(View itemView) {
            super(itemView);
            tvDevice = itemView.findViewById(R.id.tvLogDevice);
            tvAction = itemView.findViewById(R.id.tvLogAction);
            tvTime = itemView.findViewById(R.id.tvLogTime);
            logDot = itemView.findViewById(R.id.logDot);
        }
    }
}
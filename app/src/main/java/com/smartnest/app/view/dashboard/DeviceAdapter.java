package com.smartnest.app.view.dashboard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.smartnest.app.R;
import com.smartnest.app.model.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    public interface OnToggleListener {
        void onToggle(Device device);
    }

    private List<Device> devices = new ArrayList<>();
    private OnToggleListener listener;

    public DeviceAdapter(OnToggleListener listener) {
        this.listener = listener;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        Device device = devices.get(position);

        holder.tvIcon.setText(device.getIcon());
        holder.tvName.setText(device.getName());
        holder.switchDevice.setChecked(device.isOn());

        if (device.isOn()) {
            holder.tvStatus.setText("Active");
            holder.tvStatus.setTextColor(Color.parseColor("#1D9E75"));
            holder.cardView.setCardBackgroundColor(Color.parseColor("#0D2B1F"));
        } else {
            holder.tvStatus.setText("Offline");
            holder.tvStatus.setTextColor(Color.parseColor("#8892A4"));
            holder.cardView.setCardBackgroundColor(Color.parseColor("#111827"));
        }

        holder.switchDevice.setOnCheckedChangeListener(null);
        holder.switchDevice.setOnCheckedChangeListener((btn, isChecked) -> {
            listener.onToggle(device);
        });
    }

    @Override
    public int getItemCount() { return devices.size(); }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcon, tvName, tvStatus;
        Switch switchDevice;
        CardView cardView;

        DeviceViewHolder(View itemView) {
            super(itemView);
            tvIcon = itemView.findViewById(R.id.tvIcon);
            tvName = itemView.findViewById(R.id.tvDeviceName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            switchDevice = itemView.findViewById(R.id.switchDevice);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
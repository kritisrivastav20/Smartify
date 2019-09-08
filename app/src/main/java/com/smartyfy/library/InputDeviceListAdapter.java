package com.smartyfy.library;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartyfy.R;
import com.smartyfy.bean_class.InputDevice;

import java.util.ArrayList;
import java.util.List;

public class InputDeviceListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final int ROOM_VIEW = 1, DEVICE_VIEW = 2, NOT_FOUND_VIEW = 3;
    private List<InputDevice> inputDevices;

    public InputDeviceListAdapter() {
        inputDevices = new ArrayList<InputDevice>();
    }

    @Override
    public int getItemViewType(int position) {
        return inputDevices.get(position).id.isEmpty() && !inputDevices.get(position).room.id.isEmpty() ? ROOM_VIEW : inputDevices.get(position).id.isEmpty() && inputDevices.get(position).room.id.isEmpty() ? NOT_FOUND_VIEW : DEVICE_VIEW;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return i == ROOM_VIEW ? new RoomView(viewGroup) : new InputDeviceViewHolder(viewGroup, i);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (viewHolder instanceof InputDeviceViewHolder) {
            if (!inputDevices.get(i).id.isEmpty()) {
                ((InputDeviceViewHolder) viewHolder).tv_sensor_name.setText(inputDevices.get(i).device_name);
                if (inputDevices.get(i).show_ui==1 || inputDevices.get(i).show_ui == 0) {
                    ((InputDeviceViewHolder) viewHolder).tv_sensor_value.setVisibility(View.GONE);
                    ((InputDeviceViewHolder) viewHolder).iv_sensor_value.setVisibility(View.VISIBLE);
                    if (inputDevices.get(i).value.equals("1")) {
                        ((InputDeviceViewHolder) viewHolder).iv_sensor_value.setColorFilter(Color.GREEN);
                    } else {
                        ((InputDeviceViewHolder) viewHolder).iv_sensor_value.setColorFilter(Color.RED);
                    }
                }
                else if (inputDevices.get(i).show_ui == 2) {
                    ((InputDeviceViewHolder) viewHolder).iv_sensor_value.setVisibility(View.GONE);
                    ((InputDeviceViewHolder) viewHolder).tv_sensor_value.setVisibility(View.VISIBLE);
                    ((InputDeviceViewHolder) viewHolder).tv_sensor_value.setText(inputDevices.get(i).value);
                }
//                ((InputDeviceViewHolder) viewHolder).tv_sensor_value.setText(inputDevices.get();
            }
        } else if (viewHolder instanceof RoomView) {
            ((RoomView) viewHolder).tv_room.setText(inputDevices.get(i).room.name);
        }
    }

    @Override
    public int getItemCount() {
        return inputDevices.size();
    }

    public void setInputDevices(List<InputDevice> inputDevices) {
        this.inputDevices = inputDevices;
        notifyDataSetChanged();
    }

    class InputDeviceViewHolder extends ViewHolder {
        final TextView tv_sensor_name, tv_sensor_value;
        final ImageView iv_sensor_value;
        public InputDeviceViewHolder(ViewGroup parent, int viewType) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_input_device, parent, false));
            tv_sensor_name = itemView.findViewById(R.id.tv_sensor_name);
            tv_sensor_value = itemView.findViewById(R.id.tv_sensor_value);
            iv_sensor_value = itemView.findViewById(R.id.iv_sensor_value);
            if (viewType == DEVICE_VIEW) {
                tv_sensor_name.setVisibility(View.VISIBLE);
                iv_sensor_value.setVisibility(View.VISIBLE);
                tv_sensor_name.setText("");
            }
            if (viewType == NOT_FOUND_VIEW) {
                iv_sensor_value.setVisibility(View.GONE);
                tv_sensor_name.setVisibility(View.VISIBLE);
                tv_sensor_name.setText("No Device Found");
            }
        }
    }

    class RoomView extends ViewHolder {
        final TextView tv_room;
        public RoomView(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_input_device_room, parent, false));
            tv_room = itemView.findViewById(R.id.tv_room);
        }
    }
}

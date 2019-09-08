package com.smartyfy.library;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.smartyfy.R;
import com.smartyfy.bean_class.InputDevice;
import com.smartyfy.bean_class.OutputDevice;
import com.smartyfy.library.image_loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;


import static android.view.View.GONE;

public class DashboardTabButtonAdapter extends RecyclerView.Adapter {
    private static final String TAG = "DashboardTabBtnAdapter";
    private static final int INPUT_VIEW = 1, OUTPUT_VIEW = 2;

    private List<Object> outputDeviceList;
    private Context context;
    private OnStateUpdateListener onStateUpdateListener;


    public DashboardTabButtonAdapter(Context context) {
        this.context = context;
        this.outputDeviceList = new ArrayList<Object>();
    }

    @Override
    public int getItemViewType(int position) {
        return outputDeviceList.get(position) instanceof InputDevice ? INPUT_VIEW : OUTPUT_VIEW;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return i == INPUT_VIEW ? new InputViewHolder(viewGroup) : new OutputViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        if (viewHolder instanceof InputViewHolder) {
            onBindInputDevice((InputViewHolder) viewHolder, i);
        } else if (viewHolder instanceof OutputViewHolder) {
            onBindOutputDevice((OutputViewHolder) viewHolder, i);
        }
    }

    private void onBindInputDevice(@NonNull final InputViewHolder inputViewHolder, int i) {
        final InputDevice inputDevice = (InputDevice) outputDeviceList.get(i);
        inputViewHolder.tv_sensor_name.setText(inputDevice.device_name);
        if (inputDevice.show_ui == 1) {
            inputViewHolder.tv_sensor_value.setVisibility(GONE);
            inputViewHolder.iv_sensor_value.setVisibility(View.VISIBLE);
            if (inputDevice.value.equals("1")) {
                inputViewHolder.iv_sensor_value.setColorFilter(Color.GREEN);
            } else {
                inputViewHolder.iv_sensor_value.setColorFilter(Color.RED);
            }
        } else {
            inputViewHolder.tv_sensor_value.setVisibility(View.VISIBLE);
            inputViewHolder.iv_sensor_value.setVisibility(GONE);
            inputViewHolder.tv_sensor_value.setText(inputDevice.value);
        }
    }

    private void onBindOutputDevice(@NonNull final OutputViewHolder outputViewHolder, int i) {
        final OutputDevice outputDevice = (OutputDevice) outputDeviceList.get(i);
        try {
            if (outputDevice.motorised_control == 1) {
                outputViewHolder.sb_dimmer.setVisibility(GONE);
                outputViewHolder.bt_change_color.setVisibility(GONE);
                outputViewHolder.sw_state.setVisibility(GONE);
                outputViewHolder.iv_close.setVisibility(View.VISIBLE);
                outputViewHolder.iv_open.setVisibility(View.VISIBLE);
                outputViewHolder.iv_stop.setVisibility(View.VISIBLE);
            } else if (outputDevice.type_id.equals("3")) {
                outputViewHolder.sb_dimmer.setVisibility(View.VISIBLE);
                outputViewHolder.bt_change_color.setVisibility(GONE);
                outputViewHolder.sw_state.setVisibility(View.VISIBLE);
                outputViewHolder.iv_close.setVisibility(View.GONE);
                outputViewHolder.iv_open.setVisibility(View.GONE);
                outputViewHolder.iv_stop.setVisibility(View.GONE);
                if (!outputDevice.value.trim().isEmpty()) {
                    outputViewHolder.sb_dimmer.setProgress(Integer.parseInt(outputDevice.value) - 10);
                    outputViewHolder.sw_state.setChecked(Integer.parseInt(outputDevice.value) > 10);
                } else {
                    outputViewHolder.sb_dimmer.setProgress(0);
                    outputViewHolder.sw_state.setChecked(false);
                }
            } else if (outputDevice.type_id.equals("2")) {
                outputViewHolder.bt_change_color.setVisibility(View.VISIBLE);
                outputViewHolder.sw_state.setVisibility(GONE);
                outputViewHolder.sb_dimmer.setVisibility(GONE);
                outputViewHolder.iv_close.setVisibility(View.GONE);
                outputViewHolder.iv_open.setVisibility(View.GONE);
                outputViewHolder.iv_stop.setVisibility(View.GONE);
            } else if (outputDevice.type_id.equals("1")) {
                outputViewHolder.sb_dimmer.setVisibility(GONE);
                outputViewHolder.bt_change_color.setVisibility(GONE);
                outputViewHolder.sw_state.setVisibility(View.VISIBLE);
                outputViewHolder.iv_close.setVisibility(View.GONE);
                outputViewHolder.iv_open.setVisibility(View.GONE);
                outputViewHolder.iv_stop.setVisibility(View.GONE);
                if (!outputDevice.value.trim().isEmpty()) {
                    outputViewHolder.sw_state.setChecked(Integer.parseInt(outputDevice.value) == 1);
                } else {
                    outputViewHolder.sw_state.setChecked(false);
                }
            }
            outputViewHolder.tv_type.setText(outputDevice.device_name);
            ImageLoader loader = new ImageLoader(context, R.drawable.ic_lightbulb_outline_black_24dp);
            loader.DisplayImage(outputDevice.icon_url, outputViewHolder.iv_icon);

            outputViewHolder.sw_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (outputViewHolder.sw_state.isChecked()) {
                        if (outputDevice.type_id.equals("3")) {
                            outputDevice.value = "50";
                        } else {
                            outputDevice.value = "1";
                        }
                    } else {
                        if (outputDevice.type_id.equals("3")) {
                            outputDevice.value = "10";
                        } else
                            outputDevice.value = "0";
                    }
                    if (null != onStateUpdateListener)
                        onStateUpdateListener.onStateUpdated(outputDevice, Integer.parseInt(outputDevice.index));
                }
            });
            outputViewHolder.sb_dimmer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    outputDevice.value = String.valueOf(seekBar.getProgress() + 10);
                    if (null != onStateUpdateListener)
                        onStateUpdateListener.onStateUpdated(outputDevice, Integer.parseInt(outputDevice.index));
                }
            });
            outputViewHolder.bt_change_color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showColorModal(outputDevice, outputViewHolder);
                }
            });
            outputViewHolder.iv_open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outputDevice.value = "1";
                    if (null != onStateUpdateListener)
                        onStateUpdateListener.onStateUpdated(outputDevice, Integer.parseInt(outputDevice.index));
                }
            });
            outputViewHolder.iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outputDevice.value = "2";
                    if (null != onStateUpdateListener)
                        onStateUpdateListener.onStateUpdated(outputDevice, Integer.parseInt(outputDevice.index));
                }
            });
            outputViewHolder.iv_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outputDevice.value = "0";
                    if (null != onStateUpdateListener)
                        onStateUpdateListener.onStateUpdated(outputDevice, Integer.parseInt(outputDevice.index));

                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "Error Occured while populating Button", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error Details")
                    .setMessage(
                            "Button Data: " + outputDevice.toString()
                    ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
    }

    private void showColorModal(final OutputDevice outputDevice, final OutputViewHolder outputViewHolder) {
        int color = Color.WHITE;
        if (!outputDevice.value.isEmpty() && outputDevice.value.length() == 8) try {
            color = Color.parseColor("#" + outputDevice.value);
        } catch (Exception e) {
            Log.e(TAG, "Unable to parse color " + outputDevice.value);
        }
        ColorPickerDialog.OnAmbilWarnaListener listener = new ColorPickerDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(ColorPickerDialog dialog) {

            }

            @Override
            public void onOk(ColorPickerDialog dialog, int color) {
                outputDevice.value = String.format("%08X", (color));
                if (null != onStateUpdateListener) onStateUpdateListener.onStateUpdated(outputDevice, Integer.parseInt(outputDevice.index));
            }
        };
        ColorPickerDialog dialog = new ColorPickerDialog(context, color, true, listener,R.style.MyTheme);
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return outputDeviceList.size();
    }

    public void setInputDeviceList(List<InputDevice> inputDeviceList) {
        for (InputDevice inputDevice : inputDeviceList) {
            if (inputDevice.show_ui == 0) continue;
            InputDevice inputDevice1 = new InputDevice(inputDevice);
            this.outputDeviceList.add(inputDevice1);
        }
    }

    public void setOutputDeviceList(List<OutputDevice> outputDeviceList) {
        //For creating duplicate copy
//        this.outputDeviceList =  new ArrayList<Object>();
        for (OutputDevice outputDevice : outputDeviceList) {
            OutputDevice outputDevice1 = new OutputDevice(outputDevice);
            this.outputDeviceList.add(outputDevice1);
        }
    }

    public void clearDevices() {
        this.outputDeviceList =  new ArrayList<Object>();
    }

    public void setOnStateUpdateListener(OnStateUpdateListener onStateUpdateListener) {
        this.onStateUpdateListener = onStateUpdateListener;
    }

    class OutputViewHolder extends RecyclerView.ViewHolder {
        final TextView tv_type;
        final ImageView iv_icon;
        final Switch sw_state;
        final SeekBar sb_dimmer;
        final Button bt_change_color;
        final ImageView iv_stop, iv_open, iv_close;
        OutputViewHolder(ViewGroup viewGroup) {
            super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_button_room_view, viewGroup, false));
            tv_type = itemView.findViewById(R.id.tv_type);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            sw_state = itemView.findViewById(R.id.sw_state);
            sb_dimmer = itemView.findViewById(R.id.sb_dimmer);
            iv_stop = itemView.findViewById(R.id.iv_stop);
            iv_open = itemView.findViewById(R.id.iv_open);
            iv_close = itemView.findViewById(R.id.iv_close);
            bt_change_color = itemView.findViewById(R.id.bt_change_color);
        }
    }

    class InputViewHolder extends RecyclerView.ViewHolder {
        final TextView tv_sensor_name, tv_sensor_value;
        final ImageView iv_sensor_value;
        InputViewHolder(ViewGroup viewGroup) {
            super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_input_device, viewGroup, false));
            tv_sensor_name = itemView.findViewById(R.id.tv_sensor_name);
            tv_sensor_value = itemView.findViewById(R.id.tv_sensor_value);
            iv_sensor_value = itemView.findViewById(R.id.iv_sensor_value);
        }
    }

    public interface OnStateUpdateListener {
        public void onStateUpdated(OutputDevice outputDevice, int index);
    }
}

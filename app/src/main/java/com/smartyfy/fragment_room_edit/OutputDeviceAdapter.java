package com.smartyfy.fragment_room_edit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;

import com.smartyfy.R;
import com.smartyfy.bean_class.Icon;
import com.smartyfy.bean_class.InputDevice;
import com.smartyfy.bean_class.OutputDevice;
import com.smartyfy.bean_class.Room;
import com.smartyfy.bean_class.Type;
import com.smartyfy.library.CustomDatePicker;
import com.smartyfy.library.image_loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

class OutputDeviceAdapter extends RecyclerView.Adapter<OutputDeviceAdapter.OutputDeviceViewHolder> {
    private Room room;
    private List<OutputDevice> outputDevices;
    private List<Type> types;
    private List<Icon> icons;
    private Context context;
    public OutputDeviceAdapter(Context context, List<Type> types, List<Icon> icons, Room room) {
        this.room = room;
        this.types = types;
        this.icons = icons;
        this.context = context;
        outputDevices = new ArrayList<OutputDevice>();
    }
    @NonNull
    @Override
    public OutputDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OutputDeviceViewHolder(viewGroup, room, types, icons, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final OutputDeviceViewHolder outputDeviceViewHolder, int i) {
        final OutputDevice outputDevice = outputDevices.get(i);

        outputDeviceViewHolder.populateSensor(context);

        outputDeviceViewHolder.til_name.getEditText().setText(outputDevice.device_name);
        outputDeviceViewHolder.til_start.getEditText().setText(outputDevice.on_time);
        outputDeviceViewHolder.til_end.getEditText().setText(outputDevice.off_time);
        outputDeviceViewHolder.til_activation.getEditText().setText(outputDevice.activation_time);
        outputDeviceViewHolder.cb_all_off.setChecked(outputDevice.all_off.equals("1"));
        outputDeviceViewHolder.cb_all_on.setChecked(outputDevice.all_on.equals("1"));
        Log.e("Adapter Issue", ""+outputDevice.sensor_id);
        outputDeviceViewHolder.sp_icon.setSelection(outputDeviceViewHolder.iconIndex(outputDevice.icon_name));

        ImageLoader loader = new ImageLoader(context, R.drawable.ic_lightbulb_outline_black_24dp);
        loader.DisplayImage(outputDevice.icon_url, outputDeviceViewHolder.iv_icon);

        outputDeviceViewHolder.sp_type.setSelection(outputDeviceViewHolder.typeIndex(outputDevice.type));
        if (outputDevice.sensor_id < room.inputDevices.size())
            outputDeviceViewHolder.sp_sensor.setSelection(outputDevice.sensor_id);
        else {
            outputDeviceViewHolder.sp_sensor.setSelection(0);
            outputDevice.sensor_id = -1;
        }

        CustomDatePicker.timeListener(outputDeviceViewHolder.til_start.getEditText(), context);
        CustomDatePicker.timeListener(outputDeviceViewHolder.til_end.getEditText(), context);

        outputDeviceViewHolder.til_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                outputDevice.device_name = s.toString();
            }
        });
        outputDeviceViewHolder.til_start.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                outputDevice.on_time = s.toString();
            }
        });
        outputDeviceViewHolder.til_end.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                outputDevice.off_time = s.toString();
            }
        });
        outputDeviceViewHolder.til_activation.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                outputDevice.activation_time = s.toString();
            }
        });
        outputDeviceViewHolder.sp_icon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Icon icon = outputDeviceViewHolder.getIcon(position);
                if (icon != null) {
                    outputDevice.icon_id = icon.id;
                    outputDevice.icon_name = icon.icon_name;
                    outputDevice.icon_url = icon.url;
                    ImageLoader loader = new ImageLoader(context, R.drawable.ic_lightbulb_outline_black_24dp);
                    loader.DisplayImage(icon.url, outputDeviceViewHolder.iv_icon);
                } else {
                    outputDevice.icon_id = "";
                    outputDevice.icon_name = "";
                    outputDevice.icon_url = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        outputDeviceViewHolder.sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Type type = outputDeviceViewHolder.getType(position);
                if (type != null) {
                    outputDevice.type = type.type;
                    outputDevice.type_id = type.id;
                    if (type.id.equals("2")) { //RGB
                        outputDevice.on_time = "";
                        outputDevice.off_time = "";
                        outputDevice.activation_time = "0";
                        outputDevice.all_on = "0";
                        outputDevice.all_off = "0";
                        outputDevice.sensor_id = -1;
                        outputDeviceViewHolder.cb_all_on.setChecked(false);
                        outputDeviceViewHolder.cb_all_off.setChecked(false);
                        outputDeviceViewHolder.til_start.getEditText().setText("");
                        outputDeviceViewHolder.til_end.getEditText().setText("");
                        outputDeviceViewHolder.til_activation.getEditText().setText("0");
                        outputDeviceViewHolder.sp_sensor.setSelection(0);

                        outputDeviceViewHolder.cb_all_on.setEnabled(false);
                        outputDeviceViewHolder.cb_all_off.setEnabled(false);
                        outputDeviceViewHolder.til_start.getEditText().setClickable(false);
                        outputDeviceViewHolder.til_start.getEditText().setEnabled(false);
                        outputDeviceViewHolder.til_end.getEditText().setClickable(false);
                        outputDeviceViewHolder.til_end.getEditText().setEnabled(false);
                        outputDeviceViewHolder.til_activation.getEditText().setClickable(false);
                        outputDeviceViewHolder.til_activation.getEditText().setEnabled(false);
//                        outputDeviceViewHolder.sp_sensor.setClickable(false);
//                        outputDeviceViewHolder.sp_sensor.setEnabled(false);

                    } else {
                        outputDeviceViewHolder.cb_all_on.setEnabled(true);
                        outputDeviceViewHolder.cb_all_off.setEnabled(true);
                        outputDeviceViewHolder.til_start.getEditText().setClickable(true);
                        outputDeviceViewHolder.til_start.getEditText().setEnabled(true);
                        outputDeviceViewHolder.til_end.getEditText().setClickable(true);
                        outputDeviceViewHolder.til_end.getEditText().setEnabled(true);
                        outputDeviceViewHolder.til_activation.getEditText().setClickable(true);
                        outputDeviceViewHolder.til_activation.getEditText().setEnabled(true);
//                        outputDeviceViewHolder.sp_sensor.setClickable(true);
//                        outputDeviceViewHolder.sp_sensor.setEnabled(true);
                    }
                } else {
                    outputDevice.type = "";
                    outputDevice.type_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        outputDeviceViewHolder.sp_sensor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    outputDevice.sensor_id = position - 1;
                } else {
                    outputDevice.sensor_id = -1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        outputDeviceViewHolder.cb_all_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outputDeviceViewHolder.cb_all_on.isChecked()) {
                    outputDevice.all_on = "1";
                } else {
                    outputDevice.all_on = "0";
                }
            }
        });
        outputDeviceViewHolder.cb_all_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outputDeviceViewHolder.cb_all_off.isChecked()) {
                    outputDevice.all_off = "1";
                } else {
                    outputDevice.all_off = "0";
                }
            }
        });
        outputDeviceViewHolder.bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputDevices.remove(outputDeviceViewHolder.getAdapterPosition());
                notifyItemRemoved(outputDeviceViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return outputDevices.size();
    }

    public void setOutputDevices(List<OutputDevice> outputDevices) {
        this.outputDevices = outputDevices;
        notifyDataSetChanged();
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    class OutputDeviceViewHolder extends RecyclerView.ViewHolder {
        final Spinner sp_type, sp_icon, sp_sensor;
        final ImageView iv_icon;
        final TextInputLayout til_name, til_start, til_end, til_activation;
        final CheckBox cb_all_on, cb_all_off;
        final Button bt_remove;
        private Room room;
        private List<Type> types;
        private List<Icon> icons;
        public OutputDeviceViewHolder(ViewGroup parent, Room room, List<Type> types, List<Icon> icons, Context context) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_room_edit_output_devices, parent, false));
            sp_type = itemView.findViewById(R.id.sp_type);
            sp_icon = itemView.findViewById(R.id.sp_icon);
            sp_sensor = itemView.findViewById(R.id.sp_sensor);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            til_name = itemView.findViewById(R.id.til_name);
            til_start = itemView.findViewById(R.id.til_start);
            til_end = itemView.findViewById(R.id.til_end);
            til_activation = itemView.findViewById(R.id.til_activation);
            cb_all_on = itemView.findViewById(R.id.cb_all_on);
            cb_all_off = itemView.findViewById(R.id.cb_all_off);
            bt_remove = itemView.findViewById(R.id.bt_remove);
            bt_remove.setVisibility(View.GONE);
            sp_type.setClickable(false);
            sp_type.setEnabled(false);
            sp_sensor.setClickable(false);
            sp_sensor.setEnabled(false);
            til_activation.getEditText().setEnabled(false);
            til_activation.getEditText().setFocusable(false);
            til_activation.getEditText().setClickable(false);
            this.room = room;
            this.types = types;
            this.icons = icons;

            populateIcons(context);
            populateTypes(context);
        }
        public void populateIcons(Context context) {
            List<String> icon_name = new ArrayList<String>();
            icon_name.add("Select");
            for (Icon icon : icons) {
                icon_name.add(icon.icon_name);
            }
            ArrayAdapter<String> iconArrayAdapter = new ArrayAdapter<String>(context, R.layout.simple_spinner_item,icon_name);
            iconArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_icon.setAdapter(iconArrayAdapter);
        }
        public void populateTypes(Context context) {
            List<String> type_name = new ArrayList<String>();
            type_name.add("Select");
            for (Type type : types) {
                type_name.add(type.type);
            }
            ArrayAdapter<String> typeArrayAdapter = new ArrayAdapter<String>(context, R.layout.simple_spinner_item, type_name);
            typeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_type.setAdapter(typeArrayAdapter);
        }
        public void populateSensor(Context context) {
            List<String> sensor_name = new ArrayList<String>();
            sensor_name.add("Select");
            for (InputDevice device : room.inputDevices) {
                sensor_name.add(device.device_name);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.simple_spinner_item, sensor_name);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_sensor.setAdapter(adapter);
        }
        public Icon getIcon(int position) {
            if (position == 0) return null;
            position -= 1;
            return icons.get(position);
        }
        public Type getType(int position) {
            if (position == 0) return null;
            position -= 1;
            return types.get(position);
        }
        public InputDevice getSensor(int position) {
            if (position==0) return null;
            position -= 1;
            return room.inputDevices.get(position);
        }
        public int iconIndex(String icon_name) {
            for (Icon icon : icons) {
                if (icon.icon_name.equals(icon_name)) return icons.indexOf(icon) + 1;
            }
            return 0;
        }
        public int typeIndex(String type_str) {
            for (Type type : types) {
                if (type.type.equals(type_str)) return types.indexOf(type) + 1;
            }
            return 0;
        }
        public int sensorIndex(String name) {
            for (InputDevice inputDevice : room.inputDevices) {
                if (inputDevice.device_name.equals(name)) return room.inputDevices.indexOf(inputDevice) + 1;
            }
            return 0;
        }
    }

    public interface Listener {
        public void onChange();
        public void onRemove();
    }
}

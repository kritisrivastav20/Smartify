package com.smartyfy.fragment_room_edit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.smartyfy.R;
import com.smartyfy.bean_class.InputDevice;
import com.smartyfy.bean_class.Type;
import com.smartyfy.library.CustomDatePicker;

import java.util.ArrayList;
import java.util.List;

class InputDeviceAdapter extends RecyclerView.Adapter<InputDeviceAdapter.InputDeviceViewHolder> {
    private List<InputDevice> inputDevices;
    private List<Type> types;
    private Context context;
    private Listener listener;
    public InputDeviceAdapter(Context context, List<Type> types) {
        inputDevices = new ArrayList<InputDevice>();
        this.types = types;
        this.context = context;
    }
    @NonNull
    @Override
    public InputDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new InputDeviceViewHolder(viewGroup, types, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final InputDeviceViewHolder inputDeviceViewHolder, int i) {
        final InputDevice inputDevice = inputDevices.get(i);

        inputDeviceViewHolder.til_name.getEditText().setText(inputDevice.device_name);
        inputDeviceViewHolder.til_start.getEditText().setText(inputDevice.start_time);
        inputDeviceViewHolder.til_end.getEditText().setText(inputDevice.end_time);
        inputDeviceViewHolder.til_deactivation_time.getEditText().setText(inputDevice.deactivation_time);

        inputDeviceViewHolder.sp_type.setSelection(inputDeviceViewHolder.typeIndex(inputDevice.type));

        CustomDatePicker.timeListener(inputDeviceViewHolder.til_start.getEditText(), context);
        CustomDatePicker.timeListener(inputDeviceViewHolder.til_end.getEditText(), context);

        inputDeviceViewHolder.til_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (listener!=null) listener.onChange();
                inputDevice.device_name = s.toString();
            }
        });
        inputDeviceViewHolder.til_start.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputDevice.start_time = s.toString();
            }
        });
        inputDeviceViewHolder.til_end.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputDevice.end_time = s.toString();
            }
        });
        inputDeviceViewHolder.til_deactivation_time.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputDevice.deactivation_time = s.toString();
            }
        });
        inputDeviceViewHolder.sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Type type = inputDeviceViewHolder.getType(position);
                if (type != null) {
                    inputDevice.type = type.type;
                    inputDevice.type_id = type.id;
                } else {
                    inputDevice.type = "";
                    inputDevice.type_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        inputDeviceViewHolder.bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onRemove();
                inputDevices.remove(inputDeviceViewHolder.getAdapterPosition());
                notifyItemRemoved(inputDeviceViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return inputDevices.size();
    }

    public void setInputDevices(List<InputDevice> inputDevices) {
        this.inputDevices = inputDevices;
        notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    class InputDeviceViewHolder extends RecyclerView.ViewHolder {
        private List<Type> types;
        final Spinner sp_type;
        final TextInputLayout til_start, til_end, til_name, til_deactivation_time;
        final Button bt_remove;
        public InputDeviceViewHolder(ViewGroup parent, List<Type> types, Context context) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_room_edit_input_device, parent, false));
            sp_type = itemView.findViewById(R.id.sp_type);
            sp_type.setClickable(false);
            sp_type.setEnabled(false);
            til_start = itemView.findViewById(R.id.til_start);
            til_end = itemView.findViewById(R.id.til_end);
            til_name = itemView.findViewById(R.id.til_name);
            til_deactivation_time = itemView.findViewById(R.id.til_deactivation_time);
            bt_remove = itemView.findViewById(R.id.bt_remove);
            bt_remove.setVisibility(View.GONE);
            this.types = types;
            populateTypes(context);
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
        public int typeIndex(String type_str) {
            for (Type type : types) {
                if (type.type.equals(type_str)) return types.indexOf(type) + 1;
            }
            return 0;
        }
        public Type getType(int position) {
            if (position == 0) return null;
            position -= 1;
            return types.get(position);
        }
    }

    interface Listener {
        public void onChange();
        public void onRemove();
    }
}
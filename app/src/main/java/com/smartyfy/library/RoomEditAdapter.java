package com.smartyfy.library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.smartyfy.R;
import com.smartyfy.RoomEditButtonFragment;
import com.smartyfy.bean_class.Button;
import com.smartyfy.bean_class.Room;

import java.util.Arrays;
import java.util.List;
@Deprecated
public class RoomEditAdapter extends RecyclerView.Adapter {
    private static final String TAG = "RoomEditAdapter";
    private static final int VIEW_TYPE_ROOM = 1;
    private static final int VIEW_TYPE_BUTTON = 2;

    private Room room;
    private Context context;

    private String mode;

    public RoomEditAdapter(Context context, String mode) {
        this.context = context;
        this.room = new Room();
        this.mode = mode;
    }
    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_ROOM : VIEW_TYPE_BUTTON;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return i == VIEW_TYPE_ROOM ? new RoomViewHolder(viewGroup) : new ButtonViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RoomViewHolder) {
            onBindRoomViewHolder((RoomViewHolder) viewHolder);
        }
        if (viewHolder instanceof ButtonViewHolder) {
            Button button = room.buttons.get(i - 1);
            onBindButtonViewHolder((ButtonViewHolder) viewHolder,i, button);
        }
    }

    private void onBindButtonViewHolder(@NonNull final ButtonViewHolder viewHolder, int i, final Button button) {
//        if (mode.equals(RoomEditButtonFragment.ADD_ROOM) || button.isNew) {
//            viewHolder.til_button_id.getEditText().setEnabled(true);
//        } else if (mode.equals(RoomEditButtonFragment.EDIT_ROOM)) {
//            viewHolder.til_button_id.getEditText().setEnabled(false);
//        }
        toggleMotionSensorTypeFields(viewHolder, button.type.equals("Sensor"));

        viewHolder.sp_icon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                button.icon = position > 0 ? viewHolder.icon_name.get(position) : "";
                if (position == 0) {
                    viewHolder.iv_icon.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.iv_icon.setVisibility(View.VISIBLE);
                    viewHolder.iv_icon.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(viewHolder.icon_name.get(position), "drawable", context.getPackageName())));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewHolder.cb_remote_access.setChecked(button.remote_access == 1);
        viewHolder.cb_detect_motion.setChecked(button.detect_motion == 1);
        viewHolder.cb_dimming.setChecked(button.dimming == 1);
        viewHolder.cb_color_light.setChecked(button.hasColor == 1);
        if (viewHolder.cb_color_light.isChecked()) {
            viewHolder.cb_detect_motion.setChecked(false);
            viewHolder.cb_dimming.setChecked(false);
            viewHolder.cb_detect_motion.setEnabled(false);
            viewHolder.cb_detect_motion.setClickable(false);
            viewHolder.cb_dimming.setEnabled(false);
            viewHolder.cb_dimming.setClickable(false);
        } else {
            viewHolder.cb_detect_motion.setEnabled(true);
            viewHolder.cb_detect_motion.setClickable(true);
            viewHolder.cb_dimming.setEnabled(true);
            viewHolder.cb_dimming.setClickable(true);
        }
        toggleMotionFields(viewHolder.cb_detect_motion.isChecked(), viewHolder);

//        viewHolder.til_button_id.getEditText().setText(button.id);
//        viewHolder.til_pin.getEditText().setText(String.valueOf(button.pin));
        viewHolder.til_start.getEditText().setText(button.start);
        viewHolder.til_end.getEditText().setText(button.end);
        viewHolder.til_motion_id.getEditText().setText(button.motion_id);
        viewHolder.til_deactivate.getEditText().setText(String.valueOf(button.deactivate));
        viewHolder.til_button_id.getEditText().setText(button.sensorId);
        viewHolder.til_pin.getEditText().setText(button.sensorName);
        viewHolder.til_button_name.getEditText().setText(button.type);
        viewHolder.til_on_time.getEditText().setText(button.onTime);
        viewHolder.til_off_time.getEditText().setText(button.offTime);

        int pos = viewHolder.type.indexOf(button.type);
        if (pos == -1) {
            pos = 2;
        }
        viewHolder.sp_type.setSelection(pos);
        pos = viewHolder.icon_name.indexOf(button.icon);
        viewHolder.sp_icon.setSelection(pos == -1 ? 0 : pos);

        viewHolder.cb_detect_motion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewHolder.cb_detect_motion.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                toggleMotionFields(isChecked, viewHolder);
                button.detect_motion = isChecked ? 1 : 0;
            }
        });

        viewHolder.cb_dimming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewHolder.cb_dimming.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                button.dimming = isChecked ? 1 : 0;
            }
        });

        viewHolder.cb_remote_access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewHolder.cb_remote_access.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                button.remote_access = isChecked ? 1 : 0;
            }
        });

        viewHolder.cb_color_light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewHolder.cb_color_light.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                button.hasColor = isChecked ? 1 : 0;
                if (isChecked) {
                    viewHolder.cb_detect_motion.setChecked(false);
                    viewHolder.cb_dimming.setChecked(false);
                    viewHolder.cb_detect_motion.setEnabled(false);
                    viewHolder.cb_detect_motion.setClickable(false);
                    viewHolder.cb_dimming.setEnabled(false);
                    viewHolder.cb_dimming.setClickable(false);
                } else {
                    viewHolder.cb_detect_motion.setChecked(false);
                    viewHolder.cb_dimming.setChecked(false);
                    viewHolder.cb_detect_motion.setEnabled(true);
                    viewHolder.cb_detect_motion.setClickable(true);
                    viewHolder.cb_dimming.setEnabled(true);
                    viewHolder.cb_dimming.setClickable(true);
                }
            }
        });

        viewHolder.til_on_time.getEditText().setInputType(InputType.TYPE_NULL);
        viewHolder.til_off_time.getEditText().setInputType(InputType.TYPE_NULL);
        viewHolder.til_start.getEditText().setInputType(InputType.TYPE_NULL);
        viewHolder.til_end.getEditText().setInputType(InputType.TYPE_NULL);

        CustomDatePicker.timeListener(viewHolder.til_start.getEditText(), context);
        CustomDatePicker.timeListener(viewHolder.til_end.getEditText(), context);
        CustomDatePicker.timeListener(viewHolder.til_on_time.getEditText(), context);
        CustomDatePicker.timeListener(viewHolder.til_off_time.getEditText(), context);

        viewHolder.bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.bt_remove.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                int index = room.buttons.indexOf(button);
                room.buttons.remove(button);
                notifyItemRemoved(index+1);
            }
        });

        viewHolder.sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) { //Sensor
                    button.type = viewHolder.type.get(position);
                }
                if (position == 1) {
                    toggleMotionSensorTypeFields(viewHolder, true);
                } else {
                    toggleMotionSensorTypeFields(viewHolder, false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Sensor ID
        viewHolder.til_button_id.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                button.sensorId = s.toString();
            }
        });
        //Sensor Name
        viewHolder.til_pin.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                button.sensorName = s.toString();
            }
        });
//        viewHolder.til_button_id.getEditText().addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                button.id = s.toString();
//            }
//        });
//        viewHolder.til_pin.getEditText().addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                button.pin = s.toString().trim().length() > 0 ? Integer.parseInt(s.toString()) : 0;
//            }
//        });
        viewHolder.til_on_time.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                button.onTime = s.toString();
            }
        });
        viewHolder.til_off_time.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                button.offTime = s.toString();
            }
        });
        viewHolder.til_start.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                button.start = s.toString();
            }
        });
        viewHolder.til_end.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                button.end = s.toString();
            }
        });
        viewHolder.til_deactivate.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                button.deactivate = s.toString().length() > 0 ? Integer.parseInt(s.toString()) : 0;
            }
        });
        viewHolder.til_motion_id.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                button.motion_id = s.toString();
            }
        });
        viewHolder.til_button_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                button.type = s.toString();
            }
        });
    }

    private void toggleMotionSensorTypeFields(ButtonViewHolder viewHolder, boolean show) {
        //If motion sensor then hide other details
        if (show) {
            viewHolder.cb_detect_motion.setChecked(false);
            viewHolder.cb_detect_motion.setVisibility(View.GONE);
            viewHolder.cb_dimming.setVisibility(View.GONE);
            viewHolder.cb_remote_access.setVisibility(View.GONE);
            viewHolder.cb_color_light.setVisibility(View.GONE);
            viewHolder.til_button_name.setVisibility(View.GONE);
            viewHolder.til_on_time.setVisibility(View.GONE);
            viewHolder.til_off_time.setVisibility(View.GONE);
            viewHolder.til_button_id.setVisibility(View.VISIBLE);
            viewHolder.til_pin.setVisibility(View.VISIBLE);
            viewHolder.sp_icon.setVisibility(View.INVISIBLE);
            viewHolder.iv_icon.setVisibility(View.INVISIBLE);
            viewHolder.tv_icon.setVisibility(View.INVISIBLE);
            viewHolder.bt_remove.setText("Remove Sensor");
            viewHolder.til_button_id.setHint("Sensor ID");
            viewHolder.til_pin.setHint("Sensor Name");
        } else {
            viewHolder.cb_detect_motion.setVisibility(View.VISIBLE);
            viewHolder.cb_dimming.setVisibility(View.VISIBLE);
            viewHolder.cb_remote_access.setVisibility(View.VISIBLE);
            viewHolder.cb_color_light.setVisibility(View.VISIBLE);
            viewHolder.til_button_id.setVisibility(View.GONE);
            viewHolder.til_pin.setVisibility(View.GONE);
            viewHolder.til_on_time.setVisibility(View.VISIBLE);
            viewHolder.til_off_time.setVisibility(View.VISIBLE);
            viewHolder.sp_icon.setVisibility(View.VISIBLE);
            viewHolder.iv_icon.setVisibility(View.VISIBLE);
            viewHolder.tv_icon.setVisibility(View.VISIBLE);
            viewHolder.bt_remove.setText("Remove Button");
            viewHolder.til_button_name.setVisibility(View.VISIBLE);
        }
    }

    private void toggleMotionFields(boolean show, ButtonViewHolder viewHolder) {
        if (show) {
            viewHolder.til_start.setVisibility(View.VISIBLE);
            viewHolder.til_end.setVisibility(View.VISIBLE);
            viewHolder.til_motion_id.setVisibility(View.VISIBLE);
            viewHolder.til_deactivate.setVisibility(View.VISIBLE);
            viewHolder.vw_divider.setVisibility(View.VISIBLE);
            viewHolder.tv_sec.setVisibility(View.VISIBLE);
        } else {
            viewHolder.til_start.setVisibility(View.GONE);
            viewHolder.til_end.setVisibility(View.GONE);
            viewHolder.til_motion_id.setVisibility(View.GONE);
            viewHolder.til_deactivate.setVisibility(View.GONE);
            viewHolder.vw_divider.setVisibility(View.GONE);
            viewHolder.tv_sec.setVisibility(View.GONE);
        }
    }

    private void onBindRoomViewHolder(@NonNull RoomViewHolder viewHolder) {
        if (mode.equals(RoomEditButtonFragment.ADD_ROOM)) {
            viewHolder.til_room_id.getEditText().setEnabled(true);
        } else if (mode.equals(RoomEditButtonFragment.EDIT_ROOM)) {
            viewHolder.til_room_id.getEditText().setEnabled(false);
        }
        viewHolder.til_room_name.getEditText().setText(room.name);
        viewHolder.til_room_id.getEditText().setText(room.id);
        viewHolder.til_room_id.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                room.id = s.toString();
            }
        });
        viewHolder.til_room_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                room.name = s.toString();
            }
        });
    }

    public void setRoom(Room room) {
        this.room = new Room(room); //To Create a new object and not copy prev pointer (obj)
        notifyDataSetChanged();
    }

    public void addButton(Button button) {
        this.room.buttons.add(button);
        notifyItemInserted(room.buttons.size() + 1);
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public int getItemCount() {
        return room.buttons.size() + 1;
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        final Spinner sp_type, sp_icon;
        final CheckBox cb_remote_access, cb_dimming, cb_detect_motion, cb_color_light;
        final TextInputLayout til_start, til_end, til_motion_id, til_deactivate, til_pin, til_button_id, til_button_name, til_on_time, til_off_time;
        final android.widget.Button bt_remove;
        final TextView tv_icon;
        final View vw_divider;
        final List<String> type;
        final List<String> icon;
        final List<String> icon_name;
        final TextView tv_sec;
        final ImageView iv_icon;
        public ButtonViewHolder(ViewGroup viewGroup) {
            super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_button_edit, viewGroup, false));
            sp_type = itemView.findViewById(R.id.sp_type);
            sp_icon = itemView.findViewById(R.id.sp_icon);
            cb_color_light = itemView.findViewById(R.id.cb_color_light);
            cb_color_light.setHapticFeedbackEnabled(true);
            cb_remote_access = itemView.findViewById(R.id.cb_remote_access);
            cb_remote_access.setHapticFeedbackEnabled(true);
            cb_dimming = itemView.findViewById(R.id.cb_dimming);
            cb_dimming.setHapticFeedbackEnabled(true);
            cb_detect_motion = itemView.findViewById(R.id.cb_detect_motion);
            cb_detect_motion.setHapticFeedbackEnabled(true);
            til_button_id = itemView.findViewById(R.id.til_button_id);
            til_pin = itemView.findViewById(R.id.til_pin);
            til_start = itemView.findViewById(R.id.til_start);
            til_end = itemView.findViewById(R.id.til_end);
            til_motion_id = itemView.findViewById(R.id.til_motion_id);
            til_deactivate = itemView.findViewById(R.id.til_deactivate);
            til_button_name = itemView.findViewById(R.id.til_button_name);
            til_on_time = itemView.findViewById(R.id.til_on_time);
            til_off_time = itemView.findViewById(R.id.til_off_time);
            vw_divider = itemView.findViewById(R.id.vw_divider);
            bt_remove = itemView.findViewById(R.id.bt_remove);
            bt_remove.setHapticFeedbackEnabled(true);
            tv_sec = itemView.findViewById(R.id.tv_sec);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_icon = itemView.findViewById(R.id.tv_icon);

            type = Arrays.asList(viewGroup.getContext().getResources().getStringArray(R.array.type));
            icon = Arrays.asList(viewGroup.getContext().getResources().getStringArray(R.array.icon));
            icon_name = Arrays.asList(viewGroup.getContext().getResources().getStringArray(R.array.logo_location));
        }
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        final TextInputLayout til_room_id, til_room_name;
        public RoomViewHolder(ViewGroup viewGroup) {
            super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_room_edit, viewGroup, false));
            til_room_id = itemView.findViewById(R.id.til_room_id);
            til_room_name = itemView.findViewById(R.id.til_room_name);
        }
    }
}

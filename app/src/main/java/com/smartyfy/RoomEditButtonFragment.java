package com.smartyfy;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.smartyfy.bean_class.Room;
import com.smartyfy.bean_class.Smartyfy;
import com.smartyfy.library.BaseFragment;
import com.smartyfy.library.CustomDatePicker;
import com.smartyfy.library.RoomEditAdapter;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

@Deprecated
public class RoomEditButtonFragment extends BaseFragment {
    public static final String TAG = "RoomEditButtonFragment";
    public static final String MODE = "mode", ADD_ROOM = "add_room", EDIT_ROOM = "edit_room", ROOM = "room", ROOM_INDEX = "room_index";

    private Room room;
    private int room_index = 0;

    private OnFragmentInteractionListener mListener;

    private String mode;

    private RecyclerView rv_list;
    private Button bt_save, bt_add_button;
    private RoomEditAdapter adapter;

    public RoomEditButtonFragment() {
        // Required empty public constructor
    }

    public static RoomEditButtonFragment addRoomInstance() {
        RoomEditButtonFragment fragment = new RoomEditButtonFragment();
        Bundle args = new Bundle();
        args.putString(MODE, ADD_ROOM);
        fragment.setArguments(args);
        return fragment;
    }

    public static RoomEditButtonFragment editRoomInstance(Room room, int room_index) {
        RoomEditButtonFragment fragment = new RoomEditButtonFragment();
        Bundle args = new Bundle();
        args.putParcelable(ROOM, room);
        args.putString(MODE, EDIT_ROOM);
        args.putInt(ROOM_INDEX, room_index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString(MODE, ADD_ROOM);
            if (mode.equals(EDIT_ROOM)) {
                room = getArguments().getParcelable(ROOM);
                room_index = getArguments().getInt(ROOM_INDEX);
            }
        }
        if (room == null) room = new Room();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room_edit_button_old, container, false);
        init(view);
        listener();
        if (getActivity() != null && getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).setTitle(mode.equals(ADD_ROOM) ? "Add Room" : room.name);
        }
        return view;
    }

    private void init(View view) {
        adapter = new RoomEditAdapter(getContext(), mode);
        rv_list = view.findViewById(R.id.rv_list);
        bt_add_button = view.findViewById(R.id.bt_add_button);
        bt_add_button.setHapticFeedbackEnabled(true);
        bt_save = view.findViewById(R.id.bt_save);
        bt_save.setHapticFeedbackEnabled(true);
        rv_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_list.setHasFixedSize(true);
        rv_list.setAdapter(adapter);
        adapter.setRoom(room);
    }

    private void listener() {
        bt_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_add_button.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                com.smartyfy.bean_class.Button button = new com.smartyfy.bean_class.Button();
                button.isNew = true;
                adapter.addButton(button);
            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_save.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                Room room = adapter.getRoom();
                if (validate(room)) {
                    if (bt_save.getText().equals("Save")) {
                        bt_save.setText("Confirm");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bt_save.setText("Save");
                            }
                        }, 3000);
                    } else if (bt_save.getText().equals("Confirm")) {
                        if (mode.equals(ADD_ROOM)) {
                            mListener.onRoomAdd(room);
                        } else if (mode.equals(EDIT_ROOM)) {
                            mListener.onRoomEdit(room);
                        }
                        getActivity().onBackPressed();
                    }
                }
            }
        });
    }

    private boolean validate(Room room) {
        if (room.id.trim().length() == 0) {
            Toast.makeText(getContext(), "Room ID Required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (room.name.trim().length() == 0) {
            Toast.makeText(getContext(), "Room Name Required", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (com.smartyfy.bean_class.Button button : room.buttons) {
//            if (button.id.trim().length() == 0) {
//                rv_list.scrollToPosition(room.buttons.indexOf(button) + 1);
//                Toast.makeText(getContext(), "Button ID Required", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//            if (button.pin == 0) {
//                rv_list.scrollToPosition(room.buttons.indexOf(button) + 1);
//                Toast.makeText(getContext(), "Pin number Required", Toast.LENGTH_SHORT).show();
//                return false;
//            }
            if (button.type.trim().length() == 0) {
                rv_list.scrollToPosition(room.buttons.indexOf(button) + 1);
                Toast.makeText(getContext(), "Type Required", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (button.icon.trim().length() == 0 && !button.type.trim().equals("Sensor")) {
                rv_list.scrollToPosition(room.buttons.indexOf(button) + 1);
                Toast.makeText(getContext(), "Icon Required", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (button.type.trim().equals("Sensor")) {
                if (button.sensorName.trim().isEmpty()) {
                    rv_list.scrollToPosition(room.buttons.indexOf(button) + 1);
                    Toast.makeText(getContext(), "Sensor Name Required", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (button.sensorId.trim().isEmpty()) {
                    rv_list.scrollToPosition(room.buttons.indexOf(button) + 1);
                    Toast.makeText(getContext(), "Sensor ID Required", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                // Check for on off time
                try {
                    Date on_time = CustomDatePicker.parseDate(button.onTime,"HH:mm",Locale.getDefault());
                    Date off_time = CustomDatePicker.parseDate(button.offTime, "HH:mm", Locale.getDefault());
                    if (on_time.after(off_time)) {
                        Toast.makeText(getContext(), "Invalid On Time", Toast.LENGTH_SHORT).show();
                        rv_list.scrollToPosition(room.buttons.indexOf(button) + 1);
                        return false;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (button.detect_motion == 1) {
                if (button.start.trim().length() == 0) {
                    rv_list.scrollToPosition(room.buttons.indexOf(button) + 1);
                    Toast.makeText(getContext(), "Start Time Required", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (button.end.trim().length() == 0) {
                    rv_list.scrollToPosition(room.buttons.indexOf(button) + 1);
                    Toast.makeText(getContext(), "End Time Required", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (button.deactivate == 0) {
                    rv_list.scrollToPosition(room.buttons.indexOf(button) + 1);
                    Toast.makeText(getContext(), "Deactivate Sec Required", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (button.motion_id.trim().length() == 0) {
                    rv_list.scrollToPosition(room.buttons.indexOf(button) + 1);
                    Toast.makeText(getContext(), "Motion ID Required", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                button.start = "";
                button.end = "";
                button.deactivate = 0;
                button.motion_id = "";
            }
        }
        return true;
    }

    @Override
    protected void onDataUpdate(Smartyfy smartyfy) {
        super.onDataUpdate(smartyfy);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

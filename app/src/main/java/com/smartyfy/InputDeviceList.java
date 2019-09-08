package com.smartyfy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartyfy.bean_class.InputDevice;
import com.smartyfy.bean_class.Room;
import com.smartyfy.bean_class.Smartyfy;
import com.smartyfy.library.BaseFragment;
import com.smartyfy.library.InputDeviceListAdapter;

import java.util.ArrayList;
import java.util.List;

public class InputDeviceList extends BaseFragment {
    public static final String TAG = "InputDeviceList";
    private OnFragmentInteractionListener mListener;

    private RecyclerView rv_input_device;
    private InputDeviceListAdapter adapter;

    private Smartyfy smartyfy;

    public InputDeviceList() {

    }

    public static InputDeviceList newInstance(Smartyfy smartyfy) {
        InputDeviceList frag = new InputDeviceList();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Smartyfy", smartyfy);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            smartyfy = getArguments().getParcelable("Smartyfy");
        }
        if (smartyfy == null) smartyfy = new Smartyfy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_device, container, false);
        init(view);
        if (getActivity() != null && getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).setTitle("Sensors");
        }
        return view;
    }

    private void init(View view) {
        rv_input_device = view.findViewById(R.id.rv_input_device);
        adapter = new InputDeviceListAdapter();
        rv_input_device.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_input_device.setHasFixedSize(true);
        rv_input_device.setAdapter(adapter);
        populateInputDevice();
    }

    private void populateInputDevice() {
        List<InputDevice> inputDevices = new ArrayList<>();
        for (Room room : smartyfy.rooms) {
            InputDevice inputDeviceRoom =  new InputDevice();
            inputDeviceRoom.id = "";
            inputDeviceRoom.room = new Room(room);
            inputDevices.add(inputDeviceRoom);
            for (InputDevice inputDevice : room.inputDevices) {
                inputDevices.add(new InputDevice(inputDevice));
            }
            if (room.inputDevices.size() == 0) {
                inputDevices.add(new InputDevice());
            }
        }
        adapter.setInputDevices(inputDevices);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    protected void onDataUpdate(Smartyfy smartyfy) {
        super.onDataUpdate(smartyfy);
        this.smartyfy = smartyfy;
        populateInputDevice();
    }
}

package com.smartyfy.fragment_room_edit;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartyfy.OnFragmentInteractionListener;
import com.smartyfy.R;
import com.smartyfy.bean_class.Icon;
import com.smartyfy.bean_class.InputDevice;
import com.smartyfy.bean_class.OutputDevice;
import com.smartyfy.bean_class.Room;
import com.smartyfy.bean_class.Smartyfy;
import com.smartyfy.bean_class.Type;
import com.smartyfy.library.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PagerFragment extends BaseFragment {
    public static final String TAG = "PagerFragment";

    private Room room;
    private int index, room_index;

    private RecyclerView rv_list;
    private InputDeviceAdapter inputDeviceAdapter;
    private OutputDeviceAdapter outputDeviceAdapter;
    private FloatingActionButton fab_add;

    private List<Icon> icons;
    private List<Type> inputTypes, outputTypes;

    private SharedPreferences preferences;

    public PagerFragment(){}

    private OnFragmentInteractionListener mListener;
    private InternalListener internalListener;

    public static PagerFragment newInstance(Room room, int index, int room_index) {
        PagerFragment fragment = new PagerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Room", room);
        bundle.putInt("index", index);
        bundle.putInt("room_index", room_index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            room = getArguments().getParcelable("Room");
            index = getArguments().getInt("index");
            room_index = getArguments().getInt("room_index");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_room_edit_pager_fragment, container, false);
        init(view);
        listener();
        return view;
    }

    private void init(View view) {
        preferences = getContext().getSharedPreferences(getString(R.string.preference_name), Context.MODE_PRIVATE);
        icons = new ArrayList<Icon>();
        inputTypes = new ArrayList<Type>();
        outputTypes = new ArrayList<Type>();
        setIcons();
        setTypes();
        rv_list = view.findViewById(R.id.rv_list);
        fab_add = view.findViewById(R.id.fab_add);
        inputDeviceAdapter = new InputDeviceAdapter(getContext(), inputTypes);
        outputDeviceAdapter = new OutputDeviceAdapter(getContext(), outputTypes,icons,room);
        rv_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_list.setHasFixedSize(true);
        if (index == 0) {
            rv_list.setAdapter(outputDeviceAdapter);
            outputDeviceAdapter.setOutputDevices(room.outputDevices);
        }
        if (index == 1) {
            rv_list.setAdapter(inputDeviceAdapter);
            inputDeviceAdapter.setInputDevices(room.inputDevices);
        }
    }



    private void listener() {
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 0) {
                    OutputDevice outputDevice = new OutputDevice();
                    outputDevice.type = outputTypes.get(0).type;
                    outputDevice.type_id = outputTypes.get(0).id;
                    room.outputDevices.add(outputDevice);
                    outputDeviceAdapter.notifyItemInserted(outputDeviceAdapter.getItemCount());
                }
                if (index == 1) {
                    InputDevice inputDevice = new InputDevice();
                    room.inputDevices.add(inputDevice);
                    inputDeviceAdapter.notifyItemInserted(inputDeviceAdapter.getItemCount());
                    if (internalListener != null) {
                        internalListener.onInputSensorAdded();
                    }
                }
            }
        });
        if (index == 1) {
            inputDeviceAdapter.setListener(new InputDeviceAdapter.Listener() {
                @Override
                public void onChange() {
                    if (null != internalListener) internalListener.onInputSensorAdded();
                }

                @Override
                public void onRemove() {
                    if (null != internalListener) internalListener.onInputSensorAdded();
                }
            });
        }
    }

    private void setIcons() {
        if (index == 0) try {
            JSONArray ja_icons = new JSONArray(preferences.getString(getString(R.string.pref_icon),""));
            for (int i = 0, size = ja_icons.length(); i < size ;i++) {
                Icon icon = new Icon();
                JSONObject jsonObject = ja_icons.getJSONObject(i);
                icon.icon_name = jsonObject.getString("icon_name");
                icon.url=jsonObject.getString("url");
                icon.id=jsonObject.getString("id");
                icons.add(icon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTypes() {
        try {
            JSONArray ja_type = new JSONArray(preferences.getString(getString(R.string.pref_type),""));
            for (int i = 0, size = ja_type.length(); i < size ; i++) {
                Type type = new Type();
                JSONObject jsonObject = ja_type.getJSONObject(i);
                type.id = jsonObject.getString("id");
                type.type = jsonObject.getString("type");
                type.output = jsonObject.getInt("output");
                if (index == 0 && type.output == 1) {
                    outputTypes.add(type);
                }
                if (index == 1 && type.output == 0) {
                    inputTypes.add(type);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            if (getParentFragment() instanceof InternalListener) {
                internalListener = (InternalListener) getParentFragment();
            }
        } else {
            throw new RuntimeException(context.toString() + " activity not instance of OnFragmentInteractionListener.");
        }
    }

    @Override
    protected void onDataUpdate(Smartyfy smartyfy) {
        super.onDataUpdate(smartyfy);
        if (index == 0) {

        } else if (index == 1) {

        }
    }

    @Override
    public void onInputTypeAdded() {
        super.onInputTypeAdded();
        if (index == 0) {
            outputDeviceAdapter.notifyDataSetChanged();
        }
    }
}
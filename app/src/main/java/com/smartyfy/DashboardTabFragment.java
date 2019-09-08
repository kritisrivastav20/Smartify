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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import com.smartyfy.bean_class.OutputDevice;
import com.smartyfy.bean_class.Room;
import com.smartyfy.bean_class.Smartyfy;
import com.smartyfy.library.BaseFragment;
import com.smartyfy.library.DashboardTabButtonAdapter;
import com.smartyfy.library.image_loader.ImageLoader;

public class DashboardTabFragment extends BaseFragment {
    public static final String TAG = "DashboardTabFragment";
    private Room room;
    private int index;
    private OnFragmentInteractionListener mListener;
    private RecyclerView rv_button_list;
//    private Switch sw_master;
    private ImageView iv_header_img;
    private Button bt_all_on, bt_all_off;
    private DashboardTabButtonAdapter adapter;
    public DashboardTabFragment(){}
    public static DashboardTabFragment newInstance(int index) {
        DashboardTabFragment fragment = new DashboardTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt("Index",0);
        }
//        if (getActivity() instanceof DashboardActivity)
//            room = ((DashboardActivity) getActivity()).getRoom(index);
//        if (room == null) {
//            room = new Room();
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_tab, container, false);
        init(view);
        listener();
        room = new Room();
        return view;
    }

    private void init(View view) {
        adapter = new DashboardTabButtonAdapter(getContext());
        rv_button_list = view.findViewById(R.id.rv_button_list);
//        sw_master = view.findViewById(R.id.sw_master);
        bt_all_on = view.findViewById(R.id.bt_all_on);
        bt_all_off = view.findViewById(R.id.bt_all_off);
        iv_header_img = view.findViewById(R.id.iv_header_img);
        rv_button_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_button_list.setHasFixedSize(true);
        rv_button_list.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof DashboardActivity)
            room = ((DashboardActivity) getActivity()).getRoom(index);
        if (room == null) {
            room = new Room();
        }
        ImageLoader loader = new ImageLoader(getContext(), R.drawable.room_placeholder);
        loader.DisplayImage(room.url, iv_header_img);
        adapter.clearDevices();
        adapter.setInputDeviceList(room.inputDevices);
        adapter.setOutputDeviceList(room.outputDevices);
        adapter.notifyDataSetChanged();
    }

    private void listener() {
//        sw_master.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sw_master.isChecked()) {
//                    if (null != mListener) mListener.onMasterChanged(room, "1");
//                } else {
//                    if (null != mListener) mListener.onMasterChanged(room, "0");
//                }
//            }
//        });
        adapter.setOnStateUpdateListener(new DashboardTabButtonAdapter.OnStateUpdateListener() {
            @Override
            public void onStateUpdated(OutputDevice outputDevice, int index) {
                if (null != mListener) mListener.onValueChanged(outputDevice, room, index);
            }
        });
        bt_all_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) mListener.onMasterChanged(room, "1");
            }
        });
        bt_all_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) mListener.onMasterChanged(room, "0");
            }
        });
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
    protected void onDataUpdate(Smartyfy smartyfy) {
        super.onDataUpdate(smartyfy);
        if (!room.url.equals(smartyfy.rooms.get(index).url)) {
            ImageLoader loader = new ImageLoader(getContext(), R.drawable.room_placeholder);
            loader.DisplayImage(smartyfy.rooms.get(index).url, iv_header_img);
        }
        room = smartyfy.rooms.get(index);
        adapter.clearDevices();
        adapter.setInputDeviceList(room.inputDevices);
        adapter.setOutputDeviceList(room.outputDevices);
        adapter.notifyDataSetChanged();
    }
}

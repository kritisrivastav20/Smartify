package com.smartyfy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.smartyfy.bean_class.Room;
import com.smartyfy.bean_class.Smartyfy;
import com.smartyfy.library.BaseFragment;
import com.smartyfy.library.RoomListAdapter;


public class RoomSettingFragment extends BaseFragment {

    public static final String TAG = "RoomSettingFragment";

    private OnFragmentInteractionListener mListener;

    private Smartyfy smartyfy;

    private RecyclerView rv_room_list;
    private Button bt_add_room;
    private RoomListAdapter adapter;

    public RoomSettingFragment() {
        // Required empty public constructor
    }

    public static RoomSettingFragment newInstance(Smartyfy smartyfy) {
        RoomSettingFragment fragment = new RoomSettingFragment();
        Bundle args = new Bundle();
        args.putParcelable("Smartyfy",smartyfy);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            smartyfy = getArguments().getParcelable("Smartyfy");
        }
        if (smartyfy == null) {
            smartyfy = new Smartyfy();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room_setting, container, false);
        init(view);
        listener();
        if (getActivity() instanceof DashboardActivity)
            ((DashboardActivity) getActivity()).setTitle("Room Settings");
        return view;
    }

    private void init(View view) {
        rv_room_list = view.findViewById(R.id.rv_room_list);
        bt_add_room = view.findViewById(R.id.bt_add_room);
        bt_add_room.setHapticFeedbackEnabled(true);
        adapter = new RoomListAdapter(getContext());
        rv_room_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_room_list.setHasFixedSize(true);
        rv_room_list.setAdapter(adapter);
        adapter.setRooms(smartyfy.rooms);
    }

    private void listener() {
        adapter.setCallback(new RoomListAdapter.Callback() {
            @Override
            public void onRoomSelected(Room room, int room_index) {
                mListener.onRoomSelectEdit(room, room_index);
            }
        });

        bt_add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_add_room.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                mListener.onNewRoomClicked();
            }
        });
    }

    @Override
    protected void onDataUpdate(Smartyfy smartyfy) {
        super.onDataUpdate(smartyfy);
        adapter.setRooms(smartyfy.rooms);
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

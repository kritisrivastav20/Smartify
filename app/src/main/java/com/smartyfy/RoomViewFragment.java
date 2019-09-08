package com.smartyfy;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartyfy.bean_class.Button;
import com.smartyfy.bean_class.Room;
import com.smartyfy.bean_class.Smartyfy;
import com.smartyfy.library.BaseFragment;
import com.smartyfy.library.ButtonViewAdapter;
@Deprecated
public class RoomViewFragment extends BaseFragment {
    public static final String TAG = "RoomViewFragment";

    private Room room;

    private int room_index = 0;

    private OnFragmentInteractionListener mListener;

    private RecyclerView rv_list;
    private ButtonViewAdapter adapter;

    public RoomViewFragment() {
        // Required empty public constructor
    }

    public static RoomViewFragment newInstance(Room room, int room_index) {
        RoomViewFragment fragment = new RoomViewFragment();
        Bundle args = new Bundle();
        args.putParcelable("Room", room);
        args.putInt("index", room_index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            room = getArguments().getParcelable("Room");
            room_index = getArguments().getInt("index");
        }
        if (null == room) {
            room = new Room();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room_view, container, false);
        init(view);
        listener();
        if(getActivity() != null && getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).setTitle(room.name);
        }
        return view;
    }

    private void init(View view) {
        rv_list = view.findViewById(R.id.rv_button_list);
        adapter = new ButtonViewAdapter(getContext());
        rv_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_list.setHasFixedSize(true);
        rv_list.setAdapter(adapter);
        adapter.setButtons(room.getButtons());
    }

    private void listener() {
        adapter.setCallback(new ButtonViewAdapter.Callback() {
            @Override
            public void onSeekbarChange(Button button) {
                mListener.onDimmerChange(button, room, button.value, room.buttons.indexOf(button));
            }

            @Override
            public void onButtonStateChange(Button button, boolean isChecked) {
                mListener.onButtonClicked(button, room, button.onButtonStateChange(isChecked), room.buttons.indexOf(button));
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void onDataUpdate(Smartyfy smartyfy) {
        super.onDataUpdate(smartyfy);
        adapter.setButtons(smartyfy.rooms.get(room_index).getButtons());
        room = smartyfy.rooms.get(room_index);
    }
}

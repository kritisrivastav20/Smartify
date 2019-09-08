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
import com.smartyfy.library.DashboardRoomAdapter;

@Deprecated
public class DashboardFragmentOld extends BaseFragment {

    public static final String TAG = "DashboardFragmentOld";

    private OnFragmentInteractionListener mListener;

    private RecyclerView rv_list;

    private DashboardRoomAdapter adapter;

    private Smartyfy smartyfy;

    public DashboardFragmentOld() {
        // Required empty public constructor
    }

    public static DashboardFragmentOld newInstance(Smartyfy smartyfy) {
        DashboardFragmentOld fragment = new DashboardFragmentOld();
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
        View view = inflater.inflate(R.layout.fragment_dashboard_old, container, false);
        init(view);
        listener();
        if(getActivity() != null && getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).setTitle("Welcome");
        }
        return view;
    }

    private void init(View view) {
        rv_list = view.findViewById(R.id.rv_list);
        adapter = new DashboardRoomAdapter(getContext());
        rv_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_list.setHasFixedSize(true);
        rv_list.setAdapter(adapter);
        adapter.setRoomList(smartyfy.rooms);
    }

    private void listener() {
        adapter.setCallback(new DashboardRoomAdapter.Callback() {
            @Override
            public void onBt1Clicked(Button button, Room room, int room_index, int button_index, int value) {
                mListener.onButtonClicked(button, room, value, button_index);
            }

            @Override
            public void onBt2Clicked(Button button, Room room, int room_index, int button_index, int value) {
                mListener.onButtonClicked(button, room, value, button_index);
            }

            @Override
            public void onBt3Clicked(Button button, Room room, int room_index, int button_index, int value) {
                mListener.onButtonClicked(button, room, value, button_index);
            }

            @Override
            public void onBt4Clicked(Button button, Room room, int room_index, int button_index, int value) {
                mListener.onButtonClicked(button, room, value, button_index);
            }

            @Override
            public void onBt5Clicked(Button button, Room room, int room_index, int button_index, int value) {
                mListener.onButtonClicked(button, room, value, button_index);
            }

            @Override
            public void onBt6Clicked(Button button, Room room, int room_index, int button_index, int value) {
                mListener.onButtonClicked(button, room, value, button_index);
            }

            @Override
            public void onBtClicked(Button button, Room room, int room_index, int button_index, int value) {
                mListener.onButtonClicked(button, room, value, button_index);
            }

            @Override
            public void onEditClicked(Room room, int room_index) {
                mListener.onRoomSelect(room, room_index);
            }
        });
    }

    @Override
    protected void onDataUpdate(Smartyfy smartyfy) {
        Log.e(TAG, "OnDataUpdate");
        super.onDataUpdate(smartyfy);
        adapter.setRoomList(smartyfy.rooms);
        Log.e(TAG, smartyfy.toString());
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

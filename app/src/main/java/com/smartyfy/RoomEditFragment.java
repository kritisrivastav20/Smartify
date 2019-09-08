package com.smartyfy;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartyfy.library.BaseFragment;

@Deprecated
public class RoomEditFragment extends BaseFragment {
    private OnFragmentInteractionListener mListener;

    private ViewPager vp_button_input;
    private BottomNavigationView bnv_nav;
    public RoomEditFragment() {
        // Required empty public constructor
    }

    public static RoomEditFragment newInstance() {
        RoomEditFragment fragment = new RoomEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room_edit_old, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        vp_button_input = view.findViewById(R.id.vp_button_input);
        bnv_nav = view.findViewById(R.id.bnv_nav);
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

package com.smartyfy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartyfy.library.DashboardTabAdapter;
import com.smartyfy.bean_class.Smartyfy;
import com.smartyfy.library.BaseFragment;

import java.util.List;

public class DashboardFragment extends BaseFragment {
    public static final String TAG = "DashboardFragment";
    private OnFragmentInteractionListener mListener;
    private Smartyfy smartyfy;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DashboardTabAdapter adapter;

    public DashboardFragment() {}

    public static DashboardFragment newInstance(Smartyfy smartyfy) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Smartyfy", new Smartyfy(smartyfy));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            smartyfy = getArguments().getParcelable("Smartyfy");
        }
        if (null == smartyfy) {
            smartyfy = new Smartyfy();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        init(view);
        if (getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).setTitle("Welcome " + ((DashboardActivity) getActivity()).getUser().name);
        }
        return view;
    }

    private void init(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.vp_container);
        tabLayout.setupWithViewPager(viewPager);
        adapter = new DashboardTabAdapter(getChildFragmentManager(), smartyfy.rooms);
        viewPager.setAdapter(adapter);
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
    public void onResume() {
        super.onResume();
//        List<Fragment> frags = getChildFragmentManager().getFragments();
//        for (Fragment frag : frags) {
//            if (frag != null && frag.isVisible()) {
//                BaseFragment baseFragment = (BaseFragment) frag;
//                baseFragment.notifyData(smartyfy);
//            }
//        }
    }

    @Override
    protected void onDataUpdate(Smartyfy smartyfy) {
        super.onDataUpdate(smartyfy);
        if (smartyfy.rooms.size() != this.smartyfy.rooms.size())
            adapter.setRooms(smartyfy.rooms);
        this.smartyfy = new Smartyfy(smartyfy);
        List<Fragment> frags = getChildFragmentManager().getFragments();
        for (Fragment frag : frags) {
            if (frag != null && frag.isVisible()) {
                BaseFragment baseFragment = (BaseFragment) frag;
                baseFragment.notifyData(smartyfy);
            }
        }
    }
}

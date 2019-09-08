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

import com.smartyfy.bean_class.Smartyfy;
import com.smartyfy.library.BaseFragment;
import com.smartyfy.library.UserListAdapter;

public class UsersListFragment extends BaseFragment {
    public static final String TAG = "UsersListFragment";

    private OnFragmentInteractionListener mListener;
    private RecyclerView rv_user_list;
    private UserListAdapter adapter;
    private Smartyfy smartyfy;

    public UsersListFragment() {}

    public static UsersListFragment newInstance(Smartyfy smartyfy) {
        UsersListFragment frag =  new UsersListFragment();
        Bundle bundle  = new Bundle();
        bundle.putParcelable("Smartyfy", smartyfy);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            smartyfy = getArguments().getParcelable("Smartyfy");
        }
        if (smartyfy == null) smartyfy = new Smartyfy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        init(view);
        if (getActivity() != null && getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).setTitle("Users");
        }
        adapter.setUsers(smartyfy.users);
        return view;
    }

    private void init(View view) {
        rv_user_list = view.findViewById(R.id.rv_user_list);
        adapter = new UserListAdapter();
        rv_user_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_user_list.setHasFixedSize(true);
        rv_user_list.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    protected void onDataUpdate(Smartyfy smartyfy) {
        super.onDataUpdate(smartyfy);
        this.smartyfy = smartyfy;
        adapter.setUsers(smartyfy.users);
    }
}

package com.smartyfy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.smartyfy.bean_class.Smartyfy;
import com.smartyfy.library.BaseFragment;
@Deprecated
public class LoginFragment extends BaseFragment {
    public static final String TAG = "LoginFragment";
    private OnFragmentInteractionListener mListener;

    private TextInputLayout til_username, til_password;
    private Button bt_login;

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onDataUpdate(Smartyfy smartyfy) {
        super.onDataUpdate(smartyfy);
        if (smartyfy != null) {
            mListener.loginSuccessful(til_username.getEditText().getText().toString(), til_password.getEditText().getText().toString());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);
        listener();
        return view;
    }

    private void init(View view) {
        til_password = view.findViewById(R.id.til_password);
        til_username = view.findViewById(R.id.til_username);

        bt_login = view.findViewById(R.id.bt_login);
    }

    private void listener() {
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof DashboardActivity && ((DashboardActivity)getActivity()).isConnected()) {
                    if (validate()) {
                        mListener.onLogin(til_username.getEditText().getText().toString(), til_password.getEditText().getText().toString());
                    }
                } else {
                    Toast.makeText(getContext(), "Unable to connect to Device", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validate() {
        if (til_username.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Mobile Cannot be empty", Toast.LENGTH_SHORT).show();
            til_username.setErrorEnabled(true);
            return false;
        }
        if (til_password.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Password Cannot be empty", Toast.LENGTH_SHORT).show();
            til_password.setErrorEnabled(true);
            return false;
        }
        return true;
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
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }
}

package com.smartyfy;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.smartyfy.bean_class.Smartyfy;
import com.smartyfy.library.BaseFragment;

public class SettingFragment extends BaseFragment {
    public static final String TAG = "SettingFragment";

    private Smartyfy smartyfy;

    private TextInputLayout til_wifi_un, til_wifi_pass, til_gprs_mobile;
    private Button bt_save;

    private OnFragmentInteractionListener mListener;

    public SettingFragment() {}

    public static SettingFragment newInstance(Smartyfy smartyfy) {
        SettingFragment  fragment = new SettingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Smartyfy", smartyfy);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onDataUpdate(Smartyfy smartyfy) {
        super.onDataUpdate(smartyfy);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            smartyfy = getArguments().getParcelable("Smartyfy");
        }
        if (null == smartyfy) {
            smartyfy = new Smartyfy();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        init(view);
        setUp();
        listener();
        if (getActivity() != null && getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).setTitle("Settings");
        }
        return view;
    }

    private void init(View view) {
        til_wifi_un = view.findViewById(R.id.til_wifi_un);
        til_wifi_pass = view.findViewById(R.id.til_wifi_pass);
        til_gprs_mobile = view.findViewById(R.id.til_gprs_mobile);

        bt_save = view.findViewById(R.id.bt_save);
        bt_save.setHapticFeedbackEnabled(true);
    }

    private void setUp() {
        til_wifi_un.getEditText().setText(smartyfy.client.wifi_username);
        til_wifi_pass.getEditText().setText(smartyfy.client.wifi_password);
        til_gprs_mobile.getEditText().setText(smartyfy.client.gprs_mob);
    }

    private void listener() {
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_save.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                if (bt_save.getText().equals("Save")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bt_save.setText("Save");
                        }
                    }, 3000);
                    bt_save.setText("Confirm");
                } else {
                    smartyfy.client.wifi_username = til_wifi_un.getEditText().getText().toString();
                    smartyfy.client.wifi_password = til_wifi_pass.getEditText().getText().toString();
                    smartyfy.client.gprs_mob = til_gprs_mobile.getEditText().getText().toString();
                    mListener.onEEPROMChange();
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                }
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
}

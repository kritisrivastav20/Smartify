package com.smartyfy;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.smartyfy.bean_class.Smartyfy;
import com.smartyfy.library.BaseFragment;

public class AccountSetting extends BaseFragment {
    public static final String TAG = "AccountSetting";

    private Smartyfy smartyfy;
    private OnFragmentInteractionListener mListener;

    private TextInputLayout til_first_name, til_last_name, til_mobile, til_password;
    private Button bt_save;

    public AccountSetting() {

    }

    public static AccountSetting newInstance(Smartyfy smartyfy) {
        AccountSetting setting = new AccountSetting();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Smartyfy", smartyfy);
        setting.setArguments(bundle);
        return setting;
    }

    @Override
    protected void onDataUpdate(Smartyfy smartyfy) {
        super.onDataUpdate(smartyfy);
        this.smartyfy = smartyfy;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            smartyfy = getArguments().getParcelable("Smartyfy");
        }
        if (smartyfy == null) smartyfy = new Smartyfy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_setting, container, false);
        init(view);
        setUp();
        listener();
        return view;

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

    private void init(View view) {
        til_first_name = view.findViewById(R.id.til_first_name);
        til_last_name = view.findViewById(R.id.til_last_name);
        til_mobile = view.findViewById(R.id.til_mobile);
        til_password = view.findViewById(R.id.til_password);

        bt_save = view.findViewById(R.id.bt_save);
        bt_save.setHapticFeedbackEnabled(true);
    }

    private void setUp() {
        til_first_name.getEditText().setText(smartyfy.first_name);
        til_last_name.getEditText().setText(smartyfy.last_name);
        til_mobile.getEditText().setText(smartyfy.reg_mobile);
        til_password.getEditText().setText(smartyfy.password);
    }

    private void listener() {
        til_first_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()> 0) {
                    til_first_name.setErrorEnabled(false);
                }
            }
        });
        til_last_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()> 0) {
                    til_last_name.setErrorEnabled(false);
                }
            }
        });
        til_mobile.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()> 0) {
                    til_mobile.setErrorEnabled(false);
                }
            }
        });
        til_password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()> 0) {
                    til_password.setErrorEnabled(false);
                }
            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_save.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                if (validate()) {
                    if (bt_save.getText().equals("Save")) {
                        bt_save.setText("Confirm");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bt_save.setText("Save");
                            }
                        }, 3000);
                    } else {
                        smartyfy.first_name = til_first_name.getEditText().getText().toString();
                        smartyfy.last_name = til_last_name.getEditText().getText().toString();
                        smartyfy.reg_mobile = til_mobile.getEditText().getText().toString();
                        smartyfy.password = til_password.getEditText().getText().toString();
                        mListener.onAccountSettingChange(smartyfy);
                        if (getActivity() != null)
                            getActivity().onBackPressed();
                    }
                }
            }
        });
    }

    private boolean validate() {
        if (til_first_name.getEditText().getText().toString().trim().isEmpty()) {
            til_first_name.setErrorEnabled(true);
            til_first_name.setError("Required");
            return false;
        }
        if (til_last_name.getEditText().getText().toString().trim().isEmpty()) {
            til_last_name.setErrorEnabled(true);
            til_last_name.setError("Required");
            return false;
        }
        if (til_mobile.getEditText().getText().toString().trim().isEmpty()) {
            til_mobile.setErrorEnabled(true);
            til_mobile.setError("Required");
            return false;
        }
        if (til_password.getEditText().getText().toString().trim().isEmpty()) {
            til_password.setErrorEnabled(true);
            til_password.setError("Required");
            return false;
        }
        return true;
    }
}

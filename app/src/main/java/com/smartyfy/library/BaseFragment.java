package com.smartyfy.library;

import android.support.v4.app.Fragment;

import com.smartyfy.bean_class.Smartyfy;

public class BaseFragment extends Fragment {

    public void notifyData(Smartyfy smartyfy) {
        onDataUpdate(smartyfy);
    }
    protected void onDataUpdate(Smartyfy smartyfy){

    }
    public void onInputTypeAdded(){}
}

package com.smartyfy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.smartyfy.library.Connection;
import com.smartyfy.library.JSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements JSONResponse {
    private static final int REQUEST_CHECK = 1;
    private static final String MPIN = "mpin";
    private static final String TYPE = "type";
    private static final String ICON = "icon";
    private static final String USER = "user";
    private static final String DATA = "data";
    private SharedPreferences pref;
    private boolean isValid = false, isTimeout = false;
    private String key;
    private Button bt_other;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startActivity(new Intent(this, EEPROMActivity.class));
//        finish();
        pref = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        key = pref.getString(getString(R.string.pref_key), "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isTimeout = true;
                initiateApp();
            }
        }, 3000);
        if (key.isEmpty()) {
            isValid = true;
        } else {
            connectServer(REQUEST_CHECK);
        }

//        bt_other = findViewById(R.id.bt_other);
//        bt_other.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    private void connectServer(int request) {
        Connection conn = new Connection(this);
        conn.setJSONResponse(this);
        String endpoint = "smartify.php?action=init";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(MPIN, key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conn.startJSONConn(jsonObject, endpoint, new HashMap<String, String>(), request);
    }

    private void initiateApp() {
        if (!isValid || !isTimeout) {
            return;
        }
        if (key.isEmpty()) {
            startActivity(new Intent(MainActivity.this, SecurityActivity.class));
        }
        else {
//            if (pref.getString(getString(R.string.pref_is_admin),"").equals("1")) {
//                startActivity(new Intent(MainActivity.this, EEPROMActivity.class));
//            } else if (pref.getString(getString(R.string.pref_is_admin),"").equals("2")) {
//                startActivity(new Intent(MainActivity.this, OtherActivity.class));
//            } else {
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
//            }
        }
        finish();
    }

    @Override
    public void getResponse(JSONObject jsonObject, String trojan, int request_id, int status_code) throws JSONException {
        if (status_code == 200 && jsonObject.length()>0) {
            if (jsonObject.getString("error").equals("0")) {
                JSONObject jo_data = jsonObject.getJSONArray("data").getJSONObject(0);
//                if (jo_data.getString(DB_NAME))
                try {
                    isValid = true;
                    putToSharedPref(jsonObject);
                    initiateApp();
//                    if (Arrays.asList(getResources().getAssets().list("")).contains(jo_data.getString(DB_NAME)+ ".db")) {
//                        putToSharedPref(jo_data);
//                        gotoHome();
////                        createFileDirectory();
//                        //Get Images From Server
////                        downloadFile(LOGO);
//                    }
//                    else {
//                        Toast.makeText(this, "Database doesnot exists", Toast.LENGTH_SHORT).show();
//                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(this, "Invalid Key", Toast.LENGTH_LONG).show();
                key = "";
                pref.edit().clear().apply();
                isValid = true;
                initiateApp();
            }
        } else {
            Toast.makeText(this, getString(R.string.lbl_no_internet_connection), Toast.LENGTH_LONG).show();
            isValid = true;
            initiateApp();
        }
    }
    private void putToSharedPref(JSONObject jo_data) throws JSONException {
        SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE).edit();
        prefs.putString(getString(R.string.pref_type), jo_data.getJSONArray(TYPE).toString());
        prefs.putString(getString(R.string.pref_icon), jo_data.getJSONArray(ICON).toString());
        prefs.putString(getString(R.string.pref_clients), jo_data.getJSONArray(DATA).toString());
//        ja_icons = jo_data.getJSONArray(ICON);
        prefs.putString(getString(R.string.pref_user), jo_data.getJSONObject(USER).toString());
        prefs.putString(getString(R.string.pref_key), jo_data.getJSONObject(USER).getString("mpin"));
        prefs.apply();
    }
}

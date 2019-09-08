package com.smartyfy;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.smartyfy.bean_class.Button;
import com.smartyfy.bean_class.Client;
import com.smartyfy.bean_class.InputDevice;
import com.smartyfy.bean_class.OutputDevice;
import com.smartyfy.bean_class.Room;
import com.smartyfy.bean_class.Smartyfy;
import com.smartyfy.bean_class.User;
import com.smartyfy.fragment_room_edit.RoomEditFragment;
import com.smartyfy.library.BaseFragment;
import com.smartyfy.library.ClientAdapter;
import com.smartyfy.library.FingerprintUIHelper;
import com.smartyfy.library.NavigationMenuAdapter;
import com.smartyfy.library.Permission;
import com.smartyfy.library.ResponseHandler;
import com.smartyfy.library.ServerConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class DashboardActivity extends AppCompatActivity
        implements OnFragmentInteractionListener {

    private static final String TAG = "DashboardActivity";
    private static final String ACTION_INIT = "init";
    private static final String ACTION_STATE_CHANGE = "stateChange";
    private static final String ACTION_CONN_SETTING_CHANGE = "connSettingChange";
    private static final String ACTION_ROOM_EDIT = "editRoom";
    private static final String ACTION_ROOM_ADD = "addRoom";
    private static final int PING_TIMEOUT = 30000; // 30 Sec

    private static final int REQUEST_INIT = 1;
    private static final int REQUEST_BUTTON_STATE_CHANGED = 2;
    private static final int REQUEST_CONN_SETTING_CHANGE = 3;
    private static final int REQUEST_ROOM_EDIT = 4;
    private static final int REQUEST_ROOM_ADD = 5;
    private static final int REQUEST_ACCOUNT_SETTING_CHANGE = 6;
    private static final String IS_LOGIN = "is_login";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String DATA = "data";
    private static final String ACTION_ACCOUNT_SETTING_CHANGE = "accountSettingChange";
    private static final int REQUEST_MASTER_LOCK_CHANGE = 7;
    private static final String ACTION_MASTER_CHANGE = "masterChange";
    private static final String ACTION_PING = "ping";
    private static final int REQUEST_BUTTON_MASTER_CHANGED = 8;
    private static final int REQUEST_PING = 9;
    private static final String KEY_NAME = "smartify";

    private ServerConnect serverConnect;
    private ResponseHandler responseHandler;

    private Smartyfy smartyfy;

    private Switch sw_master;
    private SharedPreferences prefs;

    private boolean isConnected = false;

    private MenuItem action_wifi_status;

    private ConstraintLayout ll_client;
    private TextView tv_client, tv_title, tv_nav_title;
    private ImageView iv_dropdown_logo;

    private RecyclerView rv_client, rv_menu;

    private NavigationMenuAdapter nav_adapter;
    private ClientAdapter clientAdapter;

    private User user;

    private ProgressDialog dialog;
    private boolean pingCheck = true;
//    @TargetApi(23)
//    private FingerprintUIHelper fingerprintUIHelper;

//    private Cipher cipher;
//    private KeyStore keyStore;
//    private KeyGenerator keyGenerator;
//    @TargetApi(23)
//    private FingerprintManagerCompat.CryptoObject cryptoObject;
//    private FingerprintManagerCompat fingerprintManagerCompat;
//    private KeyguardManager keyguardManager;

    private Permission permission;


    private Runnable ping = new Runnable() {
        @Override
        public void run() {
            if (!pingCheck && serverConnect.isConnected()) {
                serverConnect.stopClient();
                return;
            }
            if (serverConnect.isConnected()) {
                Log.e(TAG, "Pinging");
                pingCheck = false;
                sendMsg(REQUEST_PING, new JSONObject());
                pingHandler.postDelayed(ping, PING_TIMEOUT);
            }
        }
    };
    private Handler pingHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        askPermission();
        listener();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        loadDashboard();
//        String ssid = getCurrentSsid(this);
//        if (ssid != null)
//            Toast.makeText(this, getCurrentSsid(this), Toast.LENGTH_SHORT).show();
//        else Toast.makeText(this, "Nothing Found", Toast.LENGTH_SHORT).show();
    }

    private void askPermission() {
        List<String> permissions = new ArrayList<String>();
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permission.askPermission(permissions);
    }

    public static boolean supportFingerprint(Context context) {
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(context);
        return fingerprintManagerCompat.isHardwareDetected() && fingerprintManagerCompat.hasEnrolledFingerprints();
    }

    public boolean isConnected() {
        return isConnected;
    }

    private void sendMsg(int request, JSONObject sendData) {
        try {
            switch (request) {
                case REQUEST_PING:
                    sendData.put("type","1");
                    sendData.put("action", ACTION_PING);
                    serverConnect.sendJSON(sendData);
                    break;
                case REQUEST_INIT:
                    sendData.put("type", "1");
                    sendData.put("action", ACTION_INIT);
//                    sendData.put("username", "7666547320");
//                    sendData.put("password", "2804");
                    serverConnect.sendJSON(sendData);
                    Log.e(TAG, "Msg Sent");
                    break;
                case REQUEST_BUTTON_STATE_CHANGED:
                    sendData.put("type","1");
                    sendData.put("action",ACTION_STATE_CHANGE);
                    serverConnect.sendJSON(sendData);
                    break;
                case REQUEST_BUTTON_MASTER_CHANGED:
                    sendData.put("type","1");
                    sendData.put("action",ACTION_MASTER_CHANGE);
                    serverConnect.sendJSON(sendData);
                    break;
                case REQUEST_CONN_SETTING_CHANGE:
                    sendData.put("type","1");
                    sendData.put("action", ACTION_CONN_SETTING_CHANGE);
                    serverConnect.sendJSON(sendData);
                    break;
                case REQUEST_ROOM_EDIT:
                    sendData.put("type","1");
                    sendData.put("action", ACTION_ROOM_EDIT);
                    serverConnect.sendJSON(sendData);
                    break;
                case REQUEST_ROOM_ADD:
                    sendData.put("type","1");
                    sendData.put("action", ACTION_ROOM_ADD);
                    serverConnect.sendJSON(sendData);
                    break;
                case REQUEST_ACCOUNT_SETTING_CHANGE:
                    sendData.put("type", "1");
                    sendData.put("action", ACTION_ACCOUNT_SETTING_CHANGE);
                    serverConnect.sendJSON(sendData);
                    break;
                case REQUEST_MASTER_LOCK_CHANGE:
                    sendData.put("type","1");
                    sendData.put("action", ACTION_MASTER_CHANGE);
                    serverConnect.sendJSON(sendData);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void listener() {
        clientAdapter.setOnClickListener(new ClientAdapter.onClickListener() {
            @Override
            public void onClick(int position, Client client) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isHapticFeedbackEnabled())
                    drawer.setHapticFeedbackEnabled(true);
                drawer.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                drawer.closeDrawer(GravityCompat.START);
                DashboardActivity.this.smartyfy.client = client;
                DashboardActivity.this.smartyfy.rooms = new ArrayList<Room>();
                tv_client.setText(client.name);
                serverConnect.setIp(client.client_ip);
                serverConnect.setPort(Integer.parseInt(client.port));
                serverConnect.start();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getString(R.string.pref_client),client.id);
                editor.apply();
                dialog.setTitle("Connecting to " + client.name);
                dialog.setMessage("Please Wait");
                dialog.show();
                loadDashboard();

                rv_menu.setVisibility(View.VISIBLE);
                rv_client.setVisibility(View.GONE);
            }
        });
        ll_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rv_menu.getVisibility() == View.VISIBLE) {
                    rv_menu.setVisibility(View.GONE);
                    rv_client.setVisibility(View.VISIBLE);
                    iv_dropdown_logo.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
                else {
                    rv_menu.setVisibility(View.VISIBLE);
                    rv_client.setVisibility(View.GONE);
                    iv_dropdown_logo.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });
        nav_adapter.setListener(new NavigationMenuAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isHapticFeedbackEnabled())
                    drawer.setHapticFeedbackEnabled(true);
                drawer.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                drawer.closeDrawer(GravityCompat.START);
                if (!checkConnection()) {
                    return;
                }
                if (user.inactive == 1) {
                    Toast.makeText(DashboardActivity.this, "Inactive, contact admin", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (position) {
                    case 0:
                        if (user.isAdmin == 1) {
                            askPassword(SettingFragment.newInstance(smartyfy), SettingFragment.TAG, false);
                        } else {
                            Toast.makeText(DashboardActivity.this, "Admin Authority required", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        if (user.isAdmin == 1) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fl_base, UsersListFragment.newInstance(smartyfy), UsersListFragment.TAG).addToBackStack(null).commit();
                        } else {
                            Toast.makeText(DashboardActivity.this, "Admin Authority required", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_base, InputDeviceList.newInstance(smartyfy), InputDeviceList.TAG).addToBackStack(null).commit();
                        break;
                    case 3:
                        if (user.isAdmin == 1) {
                            askPassword(RoomSettingFragment.newInstance(smartyfy), RoomSettingFragment.TAG, false);
                        } else {
                            Toast.makeText(DashboardActivity.this, "Admin Authority required", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
        responseHandler.setResponse(new ResponseHandler.HandleResponse() {
            @Override
            public void onConnected() {
//                if (prefs.getBoolean(IS_LOGIN, false))
                try {
                    JSONObject jo_data = new JSONObject();
                    jo_data.put(USERNAME, user.mobile);
                    jo_data.put(PASSWORD, user.mpin);
                    sendMsg(REQUEST_INIT, jo_data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null != action_wifi_status)
                    action_wifi_status.setIcon(R.drawable.ic_signal_wifi_4_bar_black_24dp);
                isConnected = true;
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                pingHandler.removeCallbacks(ping);
                pingHandler.postDelayed(ping,PING_TIMEOUT);
                pingCheck = true;
            }

            @Override
            public void onConnectionClosed() {
                if (null != action_wifi_status)
                    action_wifi_status.setIcon(R.drawable.ic_signal_wifi_0_bar_black_24dp);
                isConnected = false;
                pingHandler.removeCallbacks(ping);
//                if (dialog != null && dialog.isShowing()) dialog.dismiss();
            }

            @Override
            public void onConnectionFailed() {
                if (null != action_wifi_status)
                    action_wifi_status.setIcon(R.drawable.ic_signal_wifi_0_bar_black_24dp);
                isConnected = false;
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                pingHandler.removeCallbacks(ping);
                Toast.makeText(DashboardActivity.this, "Unable to connect", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMsgReceived(String msg) {
                try {
                    Log.e(TAG, msg);
                    JSONObject jo_request = new JSONObject(msg);
                    switch (jo_request.getString("action")) {
                        case ACTION_INIT:
                            if (jo_request.getInt("error") == 0) {
                                setInitData(jo_request.getJSONObject("data"));
                                notifyFragments();
                            } else {
                                Toast.makeText(DashboardActivity.this, "Invalid Username Password", Toast.LENGTH_SHORT).show();
                                if (prefs.getBoolean(IS_LOGIN, false)) {
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean(IS_LOGIN, false);
                                    editor.apply();
                                    //clear Fragment Stack
                                    FragmentManager fm = getSupportFragmentManager();
                                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                        fm.popBackStack();
                                    }
                                    loadLogin();
                                } else { //Not logged in

                                }
                            }
                            break;
                        case ACTION_STATE_CHANGE:
                            if (jo_request.getInt("error") == 0) {
                                setInitData(jo_request.getJSONObject("data"));
                                notifyFragments();
                            } else {
                                Toast.makeText(DashboardActivity.this, jo_request.getString("message"),Toast.LENGTH_LONG).show();
                            }
                            break;
                        case ACTION_CONN_SETTING_CHANGE:
                            if (jo_request.getInt("error") == 0) {
                                Toast.makeText(DashboardActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DashboardActivity.this, "Something Went Wrong, Please Try Again.", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case ACTION_ACCOUNT_SETTING_CHANGE:
                            if (jo_request.getInt("error") == 0) {
                                Toast.makeText(DashboardActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString(USERNAME, smartyfy.reg_mobile);
                                editor.putString(PASSWORD, smartyfy.password);
                                editor.apply();
                            } else {
                                Toast.makeText(DashboardActivity.this, "Something Went Wrong, Please Try Again.", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case ACTION_PING:
                            pingCheck = true;
                            break;
//                        case ACTION_MASTER_CHANGE:
//                            if (jo_request.getInt("error") == 0) {
//                                Toast.makeText(DashboardActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(DashboardActivity.this, "Something Went Wrong, Please Try Again.", Toast.LENGTH_SHORT).show();
//                            }
//                            break;
                    }
                    Log.e(TAG, msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        sw_master.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sw_master.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                if (!checkConnection()) {
                    return;
                }
                try {
                    JSONObject jo_send = new JSONObject();
                    jo_send.put("value", sw_master.isChecked() ? "1" : "0");
                    sendMsg(REQUEST_MASTER_LOCK_CHANGE, jo_send);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void notifyFragments() {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        for (Fragment frag : frags) {
            if (frag != null && frag.isVisible()) {
                BaseFragment baseFragment = (BaseFragment) frag;
                baseFragment.notifyData(smartyfy);
            }
        }
    }

    private void setInitData(JSONObject jo_data) throws JSONException {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(DATA + "_" + smartyfy.client.id, jo_data.toString());
        editor.apply();

        smartyfy.rooms = new ArrayList<Room>();
        for (int room_index = 0, room_size = jo_data.getJSONArray("rooms").length(); room_index < room_size ; room_index++) {
            Room room = new Room();
            JSONObject room_obj = jo_data.getJSONArray("rooms").getJSONObject(room_index);
            room.id = room_obj.getString("id");
            room.name = room_obj.getString("name");
            room.url = room_obj.getString("url");
            room.icon_id = room_obj.getString("icon_id");
            room.inputDevices = new ArrayList<InputDevice>();
            for (int i = 0, inputDeviceSize = room_obj.getJSONArray("input_devices").length(); i < inputDeviceSize;i++) {
                JSONObject input_device_obj = room_obj.getJSONArray("input_devices").getJSONObject(i);
                InputDevice inputDevice = new InputDevice();
                inputDevice.id = input_device_obj.getString("id");
                inputDevice.type_id = input_device_obj.getString("type_id");
                inputDevice.type = input_device_obj.getString("type");
                inputDevice.device_name = input_device_obj.getString("device_name");
                inputDevice.start_time = input_device_obj.getString("start_time");
                inputDevice.end_time = input_device_obj.getString("end_time");
                inputDevice.deactivation_time = input_device_obj.getString("deactivation_time");
                if (input_device_obj.has("cart_no"))
                    inputDevice.cart_no = input_device_obj.getString("cart_no");
                if (input_device_obj.has("position"))
                    inputDevice.position = input_device_obj.getString("position");
                if (input_device_obj.has("value"))
                    inputDevice.value = input_device_obj.getString("value");
                if (input_device_obj.has("show_ui"))
                    inputDevice.show_ui = input_device_obj.getInt("show_ui");
                room.inputDevices.add(inputDevice);
            }
            room.outputDevices = new ArrayList<OutputDevice>();
            for (int outputDeviceIndex = 0, outputDeviceSize = room_obj.getJSONArray("output_devices").length(); outputDeviceIndex < outputDeviceSize; outputDeviceIndex++) {
                JSONObject outputDeviceObj = room_obj.getJSONArray("output_devices").getJSONObject(outputDeviceIndex);
                OutputDevice outputDevice = new OutputDevice();
                outputDevice.index = outputDeviceObj.getString("index");
                outputDevice.all_off = outputDeviceObj.getString("all_off");
                outputDevice.all_on = outputDeviceObj.getString("all_on");
                outputDevice.on_time = outputDeviceObj.getString("on_time");
                outputDevice.off_time = outputDeviceObj.getString("off_time");
                outputDevice.type_id = outputDeviceObj.getString("type_id");
                outputDevice.type = outputDeviceObj.getString("type");
                outputDevice.icon_url = outputDeviceObj.getString("icon_url");
                outputDevice.icon_id = outputDeviceObj.getString("icon_id");
                outputDevice.icon_name = outputDeviceObj.getString("icon_name");
                outputDevice.device_name = outputDeviceObj.getString("device_name");
                outputDevice.activation_time = outputDeviceObj.getString("activation_time");
                outputDevice.id = outputDeviceObj.getString("id");
                outputDevice.sensor_id = outputDeviceObj.getInt("sensor_id");
                if (outputDeviceObj.has("value"))
                    outputDevice.value = outputDeviceObj.getString("value");
                if (outputDeviceObj.has("motorised_control"))
                    outputDevice.motorised_control = outputDeviceObj.getInt("motorised_control");
                room.outputDevices.add(outputDevice);
            }
            smartyfy.rooms.add(room);
        }
        smartyfy.users = new ArrayList<User>();
        for (int userIndex = 0, userSize = jo_data.getJSONArray("users").length(); userIndex < userSize; userIndex++) {
            User user = new User();
            JSONObject user_obj = jo_data.getJSONArray("users").getJSONObject(userIndex);
            user.name = user_obj.getString("name");
            user.mobile = user_obj.getString("mobile");
            user.mpin = user_obj.getString("mpin");
            user.inactive = user_obj.getInt("inactive");
            user.id = user_obj.getString("id");
            user.client_user_active = user_obj.getInt("client_user_active");
            user.isAdmin = user_obj.getInt("is_admin");
            smartyfy.users.add(user);
            if (user.id.equals(this.user.id)) {
                this.user = user;
                if (user.inactive == 1) {
                    Toast.makeText(this, "Inactive. Please contact admin", Toast.LENGTH_SHORT).show();
                }
            }
        }

//        smartyfy.first_name = jo_data.getJSONObject("user_details").getString("first_name");
//        smartyfy.last_name = jo_data.getJSONObject("user_details").getString("last_name");
//        smartyfy.reg_mobile = jo_data.getJSONObject("user_details").getString("reg_mobile");
//        smartyfy.password = jo_data.getJSONObject("user_details").getString("password");
//
//        smartyfy.wifi_username = jo_data.getJSONObject("eeprom").getString("username");
//        smartyfy.wifi_password = jo_data.getJSONObject("eeprom").getString("password");
//        smartyfy.gprs_mobile = jo_data.getJSONObject("eeprom").getString("gprs_mob");
//
//        smartyfy.rooms = new ArrayList<Room>();
//
//        for (int i = 0, len = jo_data.getJSONArray("rooms").length(); i < len; i++) {
//            JSONObject room = jo_data.getJSONArray("rooms").getJSONObject(i);
//            Room room_obj = new Room();
//            room_obj.name = room.getString("name");
//            room_obj.id = room.getString("id");
//            room_obj.buttons = new ArrayList<Button>();
//            for (int j = 0, jlen = room.getJSONArray("buttons").length(); j < jlen; j++) {
//                JSONObject button  = room.getJSONArray("buttons").getJSONObject(j);
//                Button button_obj = new Button();
//                button_obj.dimming = button.getInt("dimming");
//                button_obj.end = button.getString("end");
//
////                button_obj.pin = button.getInt("pin");
//                button_obj.deactivate = button.getInt("deactivate");
//                button_obj.detect_motion = button.getInt("detect_motion");
//                button_obj.start = button.getString("start");
//                button_obj.remote_access = button.getInt("remote_access");
//                button_obj.sensor_id = button.getString("sensor_id");
//                button_obj.type = button.getString("type");
//                if (button.has("sensor_id")) {
//                    button_obj.sensorId = button.getString("sensor_id");
//                }
//                if (button.has("sensor_name")) {
//                    button_obj.sensorName = button.getString("sensor_name");
//                }
//                if (button.has("on_time")) {
//                    button_obj.onTime = button.getString("on_time");
//                }
//                if (button.has("off_time")) {
//                    button_obj.offTime = button.getString("off_time");
//                }
//                if (button.has("color_light")) {
//                    button_obj.hasColor = button.getInt("color_light");
//                }
//                if (button_obj.hasColor == 1) {
//                    button_obj.colorValue = button.getString("value");
//                    button_obj.value = 1;
//                } else {
//                    button_obj.value = button.getInt("value");
//                }
////                button_obj.id = button.getString("id");
//                button_obj.icon = button.getString("icon");
//                room_obj.buttons.add(button_obj);
//            }
//            smartyfy.rooms.add(room_obj);
//        }
//        if (jo_data.has("masterLock")) {
//            sw_master.setChecked(jo_data.getInt("masterLock") == 1);
//        } else {
//            sw_master.setChecked(false);
//        }
        Log.e(TAG, smartyfy.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (serverConnect.isConnected())
            serverConnect.stopClient();
        if (!smartyfy.client.client_ip.isEmpty()) {
//            serverConnect.stopClient();
            serverConnect.setIp(smartyfy.client.client_ip);
            serverConnect.setPort(Integer.parseInt(smartyfy.client.port));
            serverConnect.start();
        }
//        fingerprintUIHelper.startListening();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && supportFingerprint(this) && fingerprintUIHelper != null)
//            fingerprintUIHelper.startListening(cryptoObject);
    }

    @Override
    protected void onPause() {
        super.onPause();
        serverConnect.stopClient();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && supportFingerprint(this) && fingerprintUIHelper != null)
//            fingerprintUIHelper.stopListening();
    }

    private void init() {
        permission = new Permission(this);
        prefs = getSharedPreferences(getString(R.string.preference_name), Context.MODE_PRIVATE);
        ll_client = findViewById(R.id.ll_dropdown);
        tv_client = findViewById(R.id.tv_client);
        tv_nav_title = findViewById(R.id.tv_nav_title);
        rv_client = findViewById(R.id.rv_clients);
        rv_menu = findViewById(R.id.rv_nav_list);
        tv_title = findViewById(R.id.tv_title);
        iv_dropdown_logo = findViewById(R.id.iv_dropdown_logo);
        sw_master = findViewById(R.id.sw_master);
        sw_master.setHapticFeedbackEnabled(true);

        nav_adapter = new NavigationMenuAdapter(this);
        rv_menu.setLayoutManager(new LinearLayoutManager(this));
        rv_menu.setHasFixedSize(true);
        rv_menu.setAdapter(nav_adapter);

        clientAdapter = new ClientAdapter();
        rv_client.setLayoutManager(new LinearLayoutManager(this));
        rv_client.setHasFixedSize(true);
        rv_client.setAdapter(clientAdapter);

        try {
            clientAdapter.setClients(new JSONArray(prefs.getString(getString(R.string.pref_clients), "")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        smartyfy = new Smartyfy();
        responseHandler = new ResponseHandler(this);
        dialog = new ProgressDialog(this,R.style.MyTheme);
        serverConnect = new ServerConnect(responseHandler, "", 0);
//        smartyfy.client = clientAdapter.getClientList().get(0);
        boolean isSet = false;
        String wifi_details = getCurrentSsid(this);
        if (wifi_details != null) {
            Log.e(TAG, "Found wifi ssid " + wifi_details);
            for (Client client : clientAdapter.getClientList()) {
                Log.e(TAG , "client wifi ssid " + client.wifi_username);
                if (client.wifi_username.equals(wifi_details)) {
                    smartyfy.client = client;
                    isSet = true;
                }
            }
        }
        if (!isSet) {
//            smartyfy.client = clientAdapter.getClientList().get(0);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (!drawer.isDrawerOpen(Gravity.START)) {
                drawer.openDrawer(Gravity.START);
                rv_menu.setVisibility(View.GONE);
                rv_client.setVisibility(View.VISIBLE);
                iv_dropdown_logo.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                tv_client.setText("Select Client");
            }

        } else {
            serverConnect.setIp(smartyfy.client.client_ip);
            serverConnect.setPort(Integer.parseInt(smartyfy.client.port));
            serverConnect.start();
            tv_client.setText(smartyfy.client.name);
            dialog.setTitle("Connecting to " + smartyfy.client.name);
            dialog.setMessage("Please Wait");
            dialog.setCancelable(false);
            dialog.show();
        }

        user = new User();
        try {
            JSONObject user_obj = new JSONObject(prefs.getString(getString(R.string.pref_user), ""));
            user.id = user_obj.getString("id");
            user.name = user_obj.getString("name");
            user.mobile = user_obj.getString("mobile");
            user.mpin = user_obj.getString("mpin");
            user.inactive = user_obj.getInt("inactive");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    private void loadDashboard() {
        String data = prefs.getString(DATA + "_" + smartyfy.client.id, "");
        try {
            setInitData(new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (user.inactive==1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_base, UserInactiveFragment.newInstance(),UserInactiveFragment.TAG).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_base, DashboardFragment.newInstance(smartyfy), DashboardFragment.TAG).commit();
        }
        tv_title.setText("Welcome " + user.name);
        tv_nav_title.setText("Hey " + user.name);
//        sw_master.setVisibility(View.VISIBLE);
        sw_master.setVisibility(View.GONE);

    }

    private void loadLogin() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_base, LoginFragment.newInstance(), LoginFragment.TAG).commit();
        tv_title.setText("Login");
        sw_master.setVisibility(View.GONE);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public boolean checkConnection() {
        if (!isConnected) {
            Toast.makeText(this, "Not connected to device", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onValueChanged(OutputDevice outputDevice, Room room, int index) {
        if (!checkConnection()) {
            return;
        }
        try {
            if (index == -1) {
                Log.e(TAG, "Button index -1 - Value Change");
                return;
            }
            JSONObject jo_send = new JSONObject();
            jo_send.put("room_id", room.id)
                    .put("index", index)
                    .put("value",outputDevice.value);
            sendMsg(REQUEST_BUTTON_STATE_CHANGED, jo_send);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMasterChanged(Room room, String value) {
        if (!checkConnection()) {
            return;
        }
        try {
            JSONObject jo_send = new JSONObject();
            jo_send.put("room_id",room.id)
                    .put("value", value);
            sendMsg(REQUEST_BUTTON_MASTER_CHANGED, jo_send);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ip, menu);
        MenuItem action_change_ip = menu.findItem(R.id.action_change_ip);
        action_change_ip.setVisible(false);
        action_wifi_status = menu.findItem(R.id.action_wifi_status);
        action_wifi_status.setIcon(isConnected ? R.drawable.ic_signal_wifi_4_bar_black_24dp : R.drawable.ic_signal_wifi_0_bar_black_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_wifi_status:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLogin(String username, String password) {
        try {
            JSONObject jo_send = new JSONObject();
            jo_send.put("username", username);
            jo_send.put("password",password);
            sendMsg(REQUEST_INIT, jo_send);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loginSuccessful(String username, String password) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.putBoolean(IS_LOGIN, true);
        editor.apply();
        loadDashboard();
    }

    @Override
    public void onButtonClicked(Button button, Room room, int value, int index) {
        if (!checkConnection()) {
            return;
        }
        try {
            if (index == -1) { //Dont send if button not found
                Log.e(TAG, "Button index -1 - Button Clicked");
                return;
            }
            Log.e(TAG, String.valueOf(button.value));
            JSONObject jo_send = new JSONObject();
            jo_send.put("room_id", room.id);
            jo_send.put("index", index);
            jo_send.put("value", button.hasColor == 1 ? button.colorValue : value);
            sendMsg(REQUEST_BUTTON_STATE_CHANGED, jo_send);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDimmerChange(Button button, Room room, int value, int index) {
        if (!checkConnection()) {
            return;
        }
        try {
            if (index == -1) { //Dont Send if button not found
                Log.e(TAG, "Button index -1 - Dimmer updated");
                return;
            }
            Log.e(TAG, String.valueOf(button.value));
            JSONObject jo_send = new JSONObject();
            jo_send.put("room_id", room.id);
            jo_send.put("index", index);
            jo_send.put("value", value);
            sendMsg(REQUEST_BUTTON_STATE_CHANGED, jo_send);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRoomSelect(Room room, int room_index) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_base, RoomViewFragment.newInstance(room, room_index), RoomViewFragment.TAG).addToBackStack(null).commit();
        tv_title.setText(room.name);
    }

    @Override
    public void onRoomSelectEdit(Room room, int room_index) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.fl_base, RoomEditButtonFragment.editRoomInstance(room, room_index), RoomEditButtonFragment.TAG).addToBackStack(null).commit();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fl_base, RoomEditFragment.editInstance()).addToBackStack(null).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_base, RoomEditFragment.editInstance(room, room_index)).addToBackStack(null).commit();
    }

    @Override
    public void onNewRoomClicked() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.fl_base, RoomEditButtonFragment.addRoomInstance(), RoomEditButtonFragment.TAG).addToBackStack(null).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_base, RoomEditFragment.addInstance(), RoomEditFragment.TAG).addToBackStack(null).commit();
    }

    @Override
    public void onEEPROMChange() {
        if (!checkConnection()) {
            return;
        }
        try {
            JSONObject jo_send = new JSONObject();
            jo_send.put("username", smartyfy.client.wifi_username);
            jo_send.put("password", smartyfy.client.wifi_password);
            jo_send.put("gprs_mob", smartyfy.client.gprs_mob);
            sendMsg(REQUEST_CONN_SETTING_CHANGE, jo_send);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRoomEdit(Room room) {
        if (!checkConnection()) {
            return;
        }
        try {
            JSONObject jo_send = new JSONObject();
            jo_send.put("room_id", room.id);
            jo_send.put("room_name", room.name);
            jo_send.put("url",room.url);
            jo_send.put("icon_id", room.icon_id);
            JSONArray ja_input_device = new JSONArray();
//{"type_id": "4", "start_time": "00:00:00", "device_name": "Motion Sensor", "end_time": "00:00:00", "deactivation_time": "0", "type": "Motion Sensor", "id": "2"}
            for (InputDevice inputDevice : room.inputDevices) {
                JSONObject jo_inputDevice = new JSONObject();
                jo_inputDevice.put("device_name", inputDevice.device_name);
                jo_inputDevice.put("type_id", inputDevice.type_id);
                jo_inputDevice.put("start_time", inputDevice.start_time);
                jo_inputDevice.put("end_time", inputDevice.end_time);
                jo_inputDevice.put("deactivation_time", inputDevice.deactivation_time);
                jo_inputDevice.put("type", inputDevice.type);
                jo_inputDevice.put("id", inputDevice.id);
                jo_inputDevice.put("cart_no", inputDevice.cart_no);
                jo_inputDevice.put("position", inputDevice.position);
                jo_inputDevice.put("show_ui", inputDevice.show_ui);
                ja_input_device.put(jo_inputDevice);
            }
//{"index": "0", "all_off": "1", "on_time": "00:00:00", "type_id": "3", "icon_url": "https://papa.fit/content/smartify/fan.png",
// "icon_id": "1", "value": "0", "device_name": "Fan Door", "icon_name": "Fan", "off_time": "00:00:00", "all_on": "1",
// "activation_time": "0", "type": "Dimmer", "id": "1"}
            JSONArray ja_output_device = new JSONArray();
            for (OutputDevice outputDevice : room.outputDevices) {
                JSONObject jo_outputDevice = new JSONObject();
                jo_outputDevice.put("index",room.outputDevices.indexOf(outputDevice))
                        .put("all_off", outputDevice.all_off)
                        .put("all_on", outputDevice.all_on)
                        .put("on_time",outputDevice.on_time)
                        .put("type_id", outputDevice.type_id)
                        .put("icon_url", outputDevice.icon_url)
                        .put("icon_id", outputDevice.icon_id)
                        .put("value", outputDevice.value)
                        .put("device_name", outputDevice.device_name)
                        .put("icon_name", outputDevice.icon_name)
                        .put("off_time", outputDevice.off_time)
                        .put("activation_time", outputDevice.activation_time)
                        .put("type", outputDevice.type)
                        .put("sensor_id", outputDevice.sensor_id)
                        .put("id", outputDevice.id)
                        .put("motorised_control", outputDevice.motorised_control);

                ja_output_device.put(jo_outputDevice);
            }
            jo_send.put("input_devices", ja_input_device)
                    .put("output_devices", ja_output_device);
//            JSONArray ja_button = new JSONArray();
//            for (Button button : room.buttons) {
//                JSONObject button_obj = new JSONObject();
//                button_obj.put("dimming", button.dimming)
//                        .put("end",button.end)
////                        .put("pin",button.pin)
//                        .put("deactivate",button.deactivate)
//                        .put("detect_motion",button.detect_motion)
//                        .put("value",button.value)
//                        .put("start", button.start)
//                        .put("remote_access", button.remote_access)
//                        .put("sensor_id", button.motion_id)
//                        .put("type",button.type)
//                        .put("sensor_id", button.sensorId)
//                        .put("sensor_name", button.sensorName)
//                        .put("on_time", button.onTime)
//                        .put("off_time", button.offTime)
//                        .put("color_light", button.hasColor)
////                        .put("id",button.id)
//                        .put("icon",button.icon);
//                ja_button.put(button_obj);
//            }
//            jo_send.put("buttons", ja_button);
            sendMsg(REQUEST_ROOM_EDIT, jo_send);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRoomAdd(Room room) {
        if (!checkConnection()) {
            return;
        }
        try {
            JSONObject jo_send = new JSONObject();
            jo_send.put("room_id", room.id);
            jo_send.put("room_name", room.name);
            jo_send.put("url",room.url);
            jo_send.put("icon_id", room.icon_id);
            JSONArray ja_input_device = new JSONArray();
//{"type_id": "4", "start_time": "00:00:00", "device_name": "Motion Sensor", "end_time": "00:00:00", "deactivation_time": "0", "type": "Motion Sensor", "id": "2"}
            for (InputDevice inputDevice : room.inputDevices) {
                JSONObject jo_inputDevice = new JSONObject();
                jo_inputDevice.put("device_name", inputDevice.device_name);
                jo_inputDevice.put("type_id", inputDevice.type_id);
                jo_inputDevice.put("start_time", inputDevice.start_time);
                jo_inputDevice.put("end_time", inputDevice.end_time);
                jo_inputDevice.put("deactivation_time", inputDevice.deactivation_time);
                jo_inputDevice.put("type", inputDevice.type);
                jo_inputDevice.put("id", inputDevice.id);
                jo_inputDevice.put("cart_no", inputDevice.cart_no);
                jo_inputDevice.put("position", inputDevice.position);
                jo_inputDevice.put("show_ui", inputDevice.show_ui);
                ja_input_device.put(jo_inputDevice);
            }
//{"index": "0", "all_off": "1", "on_time": "00:00:00", "type_id": "3", "icon_url": "https://papa.fit/content/smartify/fan.png",
// "icon_id": "1", "value": "0", "device_name": "Fan Door", "icon_name": "Fan", "off_time": "00:00:00", "all_on": "1",
// "activation_time": "0", "type": "Dimmer", "id": "1"}
            JSONArray ja_output_device = new JSONArray();
            for (OutputDevice outputDevice : room.outputDevices) {
                JSONObject jo_outputDevice = new JSONObject();
                jo_outputDevice.put("index",room.outputDevices.indexOf(outputDevice))
                        .put("all_off", outputDevice.all_off)
                        .put("all_on", outputDevice.all_on)
                        .put("on_time",outputDevice.on_time)
                        .put("type_id", outputDevice.type_id)
                        .put("icon_url", outputDevice.icon_url)
                        .put("icon_id", outputDevice.icon_id)
                        .put("value", outputDevice.value)
                        .put("device_name", outputDevice.device_name)
                        .put("icon_name", outputDevice.icon_name)
                        .put("off_time", outputDevice.off_time)
                        .put("activation_time", outputDevice.activation_time)
                        .put("type", outputDevice.type)
                        .put("sensor_id", outputDevice.sensor_id)
                        .put("motorised_control", outputDevice.motorised_control)
                        .put("id", outputDevice.id);
                ja_output_device.put(jo_outputDevice);
            }
            jo_send.put("input_devices", ja_input_device)
                    .put("output_devices", ja_output_device);
            sendMsg(REQUEST_ROOM_ADD, jo_send);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccountSettingChange(Smartyfy smartyfy) {
        if (!checkConnection()) {
            return;
        }
        try {
            JSONObject jo_send = new JSONObject();
            jo_send.put("first_name", smartyfy.first_name)
                    .put("last_name", smartyfy.last_name)
                    .put("reg_mobile", smartyfy.reg_mobile)
                    .put("password", smartyfy.password);
            sendMsg(REQUEST_ACCOUNT_SETTING_CHANGE, jo_send);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Room getRoom(int index) {
        return smartyfy.rooms.get(index);
    }

//    private void askFingerPrint(final BaseFragment fragment, final String tag) {
//        Cipher cipher = null;
//        KeyStore keyStore = null;
//        View view = getLayoutInflater().inflate(R.layout.fingerprint_dialog_content,null);
////        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(this);
//        generateKey(keyStore);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyTheme);
//        builder.setView(view);
//        builder.setTitle(R.string.fingerprint_description);
//        builder.setPositiveButton("Use Password", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                askPassword(fragment, tag, true);
//            }
//        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                fingerprintUIHelper.stopListening();
//            }
//        });
//        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                fingerprintUIHelper.stopListening();
//            }
//        });
//        final AlertDialog dialog = builder.create();
//        dialog.show();
////        fingerprintUIHelper.reset();
//        if (initCipher(cipher, keyStore) && cipher != null) {
//            cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);
//            fingerprintUIHelper = new FingerprintUIHelper(fingerprintManagerCompat,
//                    (ImageView) view.findViewById(R.id.fingerprint_icon),
//                    (TextView) view.findViewById(R.id.fingerprint_status),
//                    new FingerprintUIHelper.Callback() {
//                        @Override
//                        public void onAuthenticated() {
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fl_base, fragment, tag).addToBackStack(null).commit();
//                            dialog.dismiss();
//                        }
//
//                        @Override
//                        public void onError() {
//
//                        }
//                    });
////            helper.startAuth(fingerprintManager, cryptoObject);
//            fingerprintUIHelper.startListening(cryptoObject);
//        }
//    }
//    @TargetApi(23)
//    private void generateKey(KeyStore keyStore) {
//        try {
//
//            keyStore = KeyStore.getInstance("AndroidKeyStore");
//
//
//            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
//
//            keyStore.load(null);
//            keyGenerator.init(new
//                    KeyGenParameterSpec.Builder(KEY_NAME,
//                    KeyProperties.PURPOSE_ENCRYPT |
//                            KeyProperties.PURPOSE_DECRYPT)
//                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//                    .setUserAuthenticationRequired(true)
//                    .setEncryptionPaddings(
//                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
//                    .build());
//
//            keyGenerator.generateKey();
//
//        } catch (KeyStoreException
//                | NoSuchAlgorithmException
//                | NoSuchProviderException
//                | InvalidAlgorithmParameterException
//                | CertificateException
//                | IOException exc) {
//            exc.printStackTrace();
//        }
//
//
//    }
//
//
//    @TargetApi(23)
//    public boolean initCipher(Cipher cipher, KeyStore keyStore) {
//        try {
//            cipher = Cipher.getInstance(
//                    KeyProperties.KEY_ALGORITHM_AES + "/"
//                            + KeyProperties.BLOCK_MODE_CBC + "/"
//                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
//        } catch (NoSuchAlgorithmException |
//                NoSuchPaddingException e) {
//            throw new RuntimeException("Failed to get Cipher", e);
//        }
//
//        try {
//            keyStore.load(null);
//            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
//                    null);
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//            return true;
//        } catch (KeyPermanentlyInvalidatedException e) {
//            return false;
//        } catch (KeyStoreException | CertificateException
//                | UnrecoverableKeyException | IOException
//                | NoSuchAlgorithmException | InvalidKeyException e) {
//            throw new RuntimeException("Failed to init Cipher", e);
//        }
//    }

    private void askPassword(final BaseFragment fragment, final String tag, boolean forcePassword) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && supportFingerprint(this) && !forcePassword) {
//            askFingerPrint(fragment, tag);
//            return;
//        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyTheme);
        final EditText editText = new EditText(this);
        editText.setTextColor(Color.WHITE);
        editText.setGravity(Gravity.CENTER);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        builder.setView(editText);
        builder.setTitle("Enter Password to continue");
        builder.setPositiveButton("Continue", null);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setHapticFeedbackEnabled(true);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                if (user.mpin.equals(editText.getText().toString())) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_base, fragment, tag).addToBackStack(null).commit();
                    dialog.dismiss();
                } else {
                    Toast.makeText(DashboardActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setHapticFeedbackEnabled(true);
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                dialog.dismiss();
            }
        });

    }

    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        Log.e("Issue",networkInfo.toString());
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID().replace("\"","");
            }
            Log.e("Isue",networkInfo.toString());
            if (networkInfo.getExtraInfo() != null) {
                ssid = networkInfo.getExtraInfo().replace("\"","");
            }
        }
        return ssid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permission.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permission.onRequestPermissionResult(requestCode,permissions,grantResults);
    }
}

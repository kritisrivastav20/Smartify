package com.smartyfy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartyfy.library.Connection;
import com.smartyfy.library.JSONResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class SecurityActivity extends AppCompatActivity implements JSONResponse {
    private static final String TAG = "SecurityActivity";
    private static final String PANEL = "panel", PANEL_IMAGE = "panel_image", PARTY_NAME = "party_name",
            PARTY_LOGO = "party_logo", NAME1 = "name1", NAME2 = "name2", NAME3 = "name3", NAME4="name4", DB_NAME = "db_name",
            NAME5 = "name5", CANDIDATE_SLIP = "candidate_slip", DATA = "data", MPIN = "mpin", LOGO = "logo", SLIP_IMG = "slip_image";
    private static final int REQUEST_GET_KEY = 1;
    private static final String TYPE = "type";
    private static final String ICON = "icon";
    private static final String USER = "user";
    Button bt_1, bt_2, bt_3, bt_4, bt_5, bt_6, bt_7, bt_8, bt_9, bt_0, bt_enter;
    ImageButton ib_backspace, ib_back;
    TextView tv_title;
    ImageView iv_key_selector[];
    String key = "";
    ProgressDialog progressDialog;
    private JSONArray ja_icons = new JSONArray();
//    Permission permission;
//    private Caste caste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        initialize();
//        setupPermissions();
        listener();
    }

//    private void setupPermissions() {
//        permission = new Permission(this);
//        List<String> permissions = new ArrayList<String>();
//        permissions.add(android.Manifest.permission.READ_SMS);
//        permissions.add(android.Manifest.permission.CALL_PHONE);
//        permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        permission.askPermission(permissions);
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            permission.onRequestPermissionResult(requestCode, permissions, grantResults);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            permission.onActivityResult(requestCode, resultCode, data);
//        }
    }

    private void listener() {
        buttonListener(bt_0, 0);
        buttonListener(bt_1, 1);
        buttonListener(bt_2, 2);
        buttonListener(bt_3, 3);
        buttonListener(bt_4, 4);
        buttonListener(bt_5, 5);
        buttonListener(bt_6, 6);
        buttonListener(bt_7, 7);
        buttonListener(bt_8, 8);
        buttonListener(bt_9, 9);
        ib_backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.length() > 0) {
                    key = key.substring(0, key.length() - 1);
                }
                uiDeselector(key.length() - 1);
            }
        });
        bt_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        if (key.length() == 4) {
            connectServer(REQUEST_GET_KEY);
        } else {
            Toast.makeText(getBaseContext(), "Invalid Key", Toast.LENGTH_LONG).show();
        }
    }

    private void buttonListener(Button bt, final int value) {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.length() < 4)
                    key += value;
                uiSelector(key.length());
                Log.e(TAG, key);
            }
        });
    }

    private void uiSelector(int value) {
        for (int i = 0, count = iv_key_selector.length; i < count ; i++) {
            if (i < value) {
                iv_key_selector[i].setImageResource(R.mipmap.ic_filled_circle);
            }
            else iv_key_selector[i].setImageResource(R.mipmap.ic_unfilled_circle);
        }
//        if (value == 4) submit();
    }

    private void uiDeselector(int value) {
        for (int i = iv_key_selector.length - 1; i >= 0 ; i--) {
            if (i > value) {
                iv_key_selector[i].setImageResource(R.mipmap.ic_unfilled_circle);
            }
            else iv_key_selector[i].setImageResource(R.mipmap.ic_filled_circle);
        }
    }

    private void initialize() {
        bt_1 = (Button) findViewById(R.id.bt_1);
        bt_2 = (Button) findViewById(R.id.bt_2);
        bt_3 = (Button) findViewById(R.id.bt_3);
        bt_4 = (Button) findViewById(R.id.bt_4);
        bt_5 = (Button) findViewById(R.id.bt_5);
        bt_6 = (Button) findViewById(R.id.bt_6);
        bt_7 = (Button) findViewById(R.id.bt_7);
        bt_8 = (Button) findViewById(R.id.bt_8);
        bt_9 = (Button) findViewById(R.id.bt_9);
        bt_0 = (Button) findViewById(R.id.bt_0);
        bt_enter = (Button) findViewById(R.id.bt_enter);

        ib_backspace = (ImageButton) findViewById(R.id.bt_back);
        ib_back = (ImageButton) findViewById(R.id.ib_back);

        tv_title = (TextView) findViewById(R.id.tv_title);

        ImageView iv_1 = (ImageView) findViewById(R.id.iv_1);
        ImageView iv_2 = (ImageView) findViewById(R.id.iv_2);
        ImageView iv_3 = (ImageView) findViewById(R.id.iv_3);
        ImageView iv_4 = (ImageView) findViewById(R.id.iv_4);

        iv_key_selector = new ImageView[]{iv_1, iv_2, iv_3, iv_4};

        progressDialog = new ProgressDialog(this);

//        caste = new Caste(this);
//        try {
//            caste.copytoStorage();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void connectServer(int RequestID) {
        progressDialog.setTitle("Validating Key");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Connection conn = new Connection(this);
        conn.setJSONResponse(this);
        String endpoint = "smartify.php?action=init";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(MPIN, key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conn.startJSONConn(jsonObject, endpoint, new HashMap<String, String>(), RequestID);
    }

    @Override
    public void getResponse(JSONObject jsonObject, String trojan, int request_id, int status_code) throws JSONException {
        if (progressDialog.isShowing()) progressDialog.dismiss();
        if (status_code == 200 && jsonObject.length()>0) {
            if (jsonObject.getString("error").equals("0")) {
//                JSONObject jo_data = jsonObject.getJSONArray(DATA).getJSONObject(0);
//                if (jo_data.getString(DB_NAME))
                try {

                    putToSharedPref(jsonObject);
                    gotoHome();
//                    if (ja_icons.length() > 0)
//                        downloadFile(0);
//                    if (jo_data.getString("panel").equals("99999"))
//                        gotoAdmin();
//                    else if (jo_data.getString("panel").equals("99998"))
//                        gotoOtherAdmin();
//                    else
//                        gotoHome();
//                    if (Arrays.asList(getResources().getAssets().list("")).contains(jo_data.getString(DB_NAME)+ ".db")) {
//                        putToSharedPref(jo_data);
//                        gotoHome();
////                        createFileDirectory();
//                        //Get Images From Server
//                        downloadFile(LOGO);
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
            }
        } else {
            Toast.makeText(this, getString(R.string.lbl_no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }
    private void downloadFile(int index) {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        //Get logo
        String path = Environment.getExternalStorageDirectory() + File.separator + "Smartify" + File.separator + "assets" + File.separator;
        String fileName = "", url = "";
        try {
            url = ja_icons.getJSONObject(index).getString("url");
            fileName = ja_icons.getJSONObject(index).getString("icon_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!fileName.isEmpty() && !url.isEmpty())

            new DownloadFile().execute(url, path, fileName, String.valueOf(index));
        else {
            if (index < ja_icons.length() - 1) {
                downloadFile(index+1);
            } else {
                gotoHome();
            }
        }
    }
//
//    private void createFileDirectory() throws IOException {
//        String path = Environment.getExternalStorageDirectory() + File.separator + "AccurateSearch";
//        File file = new File(path);
//        if (!file.exists()) {
//            file.mkdir();
//            return;
//        }
//        file = new File(path + File.separator + "DataUpdated.txt");
//        if (!file.exists()) {
//            file.createNewFile();
//        } else {
//            file.delete();
//            file.createNewFile();
//        }
//        file = new File(path + File.separator + "assets");
//        if (!file.exists()) file.mkdir();
//    }

    private void putToSharedPref(JSONObject jo_data) throws JSONException {
        SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE).edit();
        prefs.putString(getString(R.string.pref_type), jo_data.getJSONArray(TYPE).toString());
        prefs.putString(getString(R.string.pref_icon), jo_data.getJSONArray(ICON).toString());
        prefs.putString(getString(R.string.pref_clients), jo_data.getJSONArray(DATA).toString());
        ja_icons = jo_data.getJSONArray(ICON);
        prefs.putString(getString(R.string.pref_user), jo_data.getJSONObject(USER).toString());
        prefs.putString(getString(R.string.pref_key), jo_data.getJSONObject(USER).getString("mpin"));
        prefs.apply();
    }

    class DownloadFile extends AsyncTask<String,Integer,Long> {
        ProgressDialog mProgressDialog = new ProgressDialog(SecurityActivity.this);// Change Mainactivity.this with your activity name.
        String fileName = "";
        int index = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Please Wait downloading content.");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
        }
        @Override
        protected Long doInBackground(String... aurl) {
            int count;
            try {
                Log.e(TAG, aurl[0]);
                URL url = new URL((String) aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                fileName=aurl[2];//Change name and subname
                index = Integer.parseInt(aurl[3]);
                int lenghtOfFile = conexion.getContentLength();
                String PATH = (String) aurl[1];
                File folder = new File(PATH);
                if(!folder.exists()){
                    folder.mkdir();//If there is no folder it will be created.
                }
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(PATH+fileName);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress ((int)(total*100/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {}
            return null;
        }
        protected void onProgressUpdate(Integer... progress) {
            mProgressDialog.setProgress(progress[0]);
            if(mProgressDialog.getProgress()==mProgressDialog.getMax()){
                mProgressDialog.dismiss();
//                if (fileName.equals("f1"))
//                    downloadFile(SLIP_IMG);
//                else gotoHome();
                if (index < ja_icons.length() - 1) {
                    downloadFile(index + 1);
                } else gotoHome();
                Toast.makeText(SecurityActivity.this, "File Downloaded", Toast.LENGTH_SHORT).show();
            }
        }
        protected void onPostExecute(String result) {

        }
    }

    private void gotoHome() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

//    private void gotoAdmin() {
//        startActivity(new Intent(this, EEPROMActivity.class));
//        finish();
//    }
//
//    private void gotoOtherAdmin() {
//        startActivity(new Intent(this, OtherActivity.class));
//        finish();
//    }

}

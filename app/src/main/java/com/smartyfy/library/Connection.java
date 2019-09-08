package com.smartyfy.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.smartyfy.BuildConfig;
import com.smartyfy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PortUnreachableException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Ghost Rider on 18/12/2016
 * Used to set up connection to server
 */
public class Connection extends AsyncTask<Object, Void, String> {
    public JSONResponse delegate=null;
    private static final String TAG = "JSON_DATA", APP_JSON = "application/json", FORM_DATA = "form-data";
    private Activity gv_context;
    private String endPoint;
    private int request_id = 0;
    private int status_code = 0;
    private File gv_image;
    private String gv_boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
    private String lineEnd = "\r\n";
    private String twoHypen = "--";
    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1024 * 1024;
    private String request_type = "";
    private PrintWriter writer;
    private String charset = "UTF-8";


    public Connection(Activity context) {
        this.gv_context = context;
    }

    public void setJSONResponse(JSONResponse jsonResponse) {
        delegate = jsonResponse;
    }

    /**
     * Starts the connection to server in async task with content type application/json
     * @param body the data to be sent to server
     * @param endPoint the endpoint of the server to be communicated with
     * @param header the addon headers to be passed ignore for Content-Type:application/json
     * @param request_id the request id
     */
    public void startJSONConn(JSONObject body, String endPoint, @NonNull HashMap<String, String> header, int request_id) {
        setRequest_type(APP_JSON);
//        SharedPreferences preferences = gv_context.getSharedPreferences
//                (gv_context.getString(R.string.prefs_register), Context.MODE_PRIVATE);
        this.endPoint = endPoint;
        this.request_id = request_id;
        if (header.size() == 0) {
            header.put(gv_context.getResources().getString(R.string.connection), gv_context.getResources().getString(R.string.keep_alive));
            header.put(gv_context.getResources().getString(R.string.content_type), gv_context.getResources().getString(R.string.application_json));
//            header.put(gv_context.getResources().getString(R.string.device), DeviceIDGenerator.generateDevice(gv_context));
//            header.put(gv_context.getResources().getString(R.string.uid), preferences.getString(Parameter.UID, ""));
            Log.e(TAG, "Header : " + header.toString());
        }
//        if (!header.containsKey(gv_context.getResources().getString(R.string.content_type)))
        header.put(gv_context.getResources().getString(R.string.content_type), gv_context.getResources().getString(R.string.application_json));
        if (!header.containsKey(gv_context.getResources().getString(R.string.connection)))
            header.put(gv_context.getResources().getString(R.string.connection), gv_context.getResources().getString(R.string.keep_alive));
//        if (!header.containsKey(gv_context.getResources().getString(R.string.device)))
//            header.put(gv_context.getResources().getString(R.string.device), DeviceIDGenerator.generateDevice(gv_context));
//        if (!header.containsKey(gv_context.getString(R.string.uid)))
//            header.put(gv_context.getString(R.string.uid), preferences.getString(Parameter.UID, ""));
        //Call for async task
        this.execute(body, endPoint /*Endpoint*/, header);
    }

    /**
     * Starts the connection to server in async task with content type multipart/form-data
     * @param body the data to be sent to server
     * @param endPoint the endpoint of the server to be communicated with
     * @param header the addon headers to be passed ignore for Content-Type:multipart/form-data
     * @param request_id the request id
     */
    public void startFormDataConn(JSONObject body, String endPoint, @NonNull HashMap<String, String> header, int request_id) {
        setRequest_type(FORM_DATA);
//        SharedPreferences preferences = gv_context.getSharedPreferences
//                (gv_context.getString(R.string.prefs_register), Context.MODE_PRIVATE);
        this.endPoint = endPoint;
        this.request_id = request_id;
        if (header.size() == 0) {
            header.put(gv_context.getResources().getString(R.string.connection), gv_context.getResources().getString(R.string.keep_alive));
            header.put(gv_context.getResources().getString(R.string.content_type),
                    gv_context.getResources().getString(R.string.form_data) + "; boundary=" + gv_boundary);
//            header.put(gv_context.getResources().getString(R.string.device), DeviceIDGenerator.generateDevice(gv_context));
//            header.put(gv_context.getResources().getString(R.string.uid), preferences.getString(Parameter.UID, ""));
            Log.e(TAG, "Header : " + header.toString());
        }
        if (!header.containsKey(gv_context.getResources().getString(R.string.content_type)))
            header.put(gv_context.getResources().getString(R.string.content_type),
                    gv_context.getResources().getString(R.string.form_data) + "; boundary=" + gv_boundary);
        if (!header.containsKey(gv_context.getResources().getString(R.string.connection)))
            header.put(gv_context.getResources().getString(R.string.connection), gv_context.getResources().getString(R.string.keep_alive));
//        if (!header.containsKey(gv_context.getResources().getString(R.string.device)))
//            header.put(gv_context.getResources().getString(R.string.device), DeviceIDGenerator.generateDevice(gv_context));
//        if (!header.containsKey(gv_context.getString(R.string.uid)))
//            header.put(gv_context.getString(R.string.uid), preferences.getString(Parameter.UID, ""));
        //Call for async task
        this.execute(body, endPoint /*Endpoint*/, header);
    }

    private void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
    private String getRequest_type() { return this.request_type; }

    private String uploadFile(JSONObject json, String endpoint, HashMap<String, String> headers, boolean isHttps) {
        String sourceFileUri = "";
        JSONObject postjson = new JSONObject();
        int serverResponseCode = 0;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;

        try {
            String protocol = gv_context.getString(R.string.https);
            if (!isHttps)
                protocol = gv_context.getString(R.string.http);
            URL url = new URL(protocol + gv_context.getResources().getString(R.string.server_address) + endpoint);
            Log.e(TAG, protocol + gv_context.getResources().getString(R.string.server_address) + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod(gv_context.getResources().getString(R.string.post));
            //Setting headers
            for (String key : headers.keySet()) {
//                if (! headers.get(key).equals(gv_context.getString(R.string.form_data)))
                conn.setRequestProperty(key, headers.get(key));
            }
            dos = new DataOutputStream(conn.getOutputStream());
            writer = new PrintWriter(new OutputStreamWriter(dos, charset));
            dos = fillDataOutputStream(dos, json);

            Log.d(TAG, "Data sent : " + json.toString());
            dos.flush();
            dos.close();
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.d(TAG, "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseCode);
            status_code = serverResponseCode;
            if (serverResponseCode == 200) {
                InputStream is = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                String response = "";
                while ((line = reader.readLine())!= null)
//                    response += line;
                    response = readFromFile(this.gv_context);
                Log.d(TAG,"Result: " + response);
                is.close();
                if(!response.equals(""))
                    postjson = new JSONObject(response);
                else
                    postjson = new JSONObject();
            }
            else if (serverResponseCode == 301) {
                if (isHttps) {
                    Log.e(TAG, "Trying unsecured connection");
                    return uploadFile(json, endpoint, headers, false);
                }
                else {
                    Log.e(TAG, "Trying secured connection");
                    return uploadFile(json, endpoint, headers, true);
                }
            }
            else {
                InputStream is = new BufferedInputStream(conn.getErrorStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                String response = "";
                while ((line = reader.readLine())!= null)
                    response += line;
                if (BuildConfig.DEBUG) {
                    final String final_response = response;
                    final String final_response_code = serverResponseCode + "";
                    gv_context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WebView webView = new WebView(gv_context);
                            webView.loadData(final_response, "text/html", "UTF-8");
                            AlertDialog dialog = new AlertDialog.Builder(gv_context)
                                    .setTitle("Error " + final_response_code + ":")
                                    .setView(webView)
                                    .setPositiveButton("OK", null)
                                    .setCancelable(false)
                                    .create();
                            dialog.show();
                            ViewGroup.LayoutParams lp = webView.getLayoutParams();
                            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                            webView.setLayoutParams(lp);
                            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) webView.getLayoutParams();
                            p.setMargins(0, 0, 0, 0);
                            webView.setLayoutParams(p);
                            webView.setVerticalScrollBarEnabled(true);
                            webView.setHorizontalScrollBarEnabled(true);
                        }
                    });
                }

                Log.d(TAG,"Result: " + response);
                is.close();
                postjson = new JSONObject();
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Log.e(TAG, "error: " + ex.getMessage(), ex);
            return new JSONObject().toString();
        } catch (PortUnreachableException | ConnectException e) {
            e.printStackTrace();
            status_code = 0;
            Log.e(TAG, "No internet connection");
        } catch (IOException e) {
            e.printStackTrace();
            status_code = 0;
        } catch (final Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Upload file to server Exception : " + e.getMessage(), e);
            return new JSONObject().toString();
        }
        return postjson.toString();
    }

    private DataOutputStream fillDataOutputStream(DataOutputStream outputStream, JSONObject json) throws IOException, JSONException {
        switch (getRequest_type()) {
            case APP_JSON:
                outputStream.writeBytes(json.toString());
                break;
            case FORM_DATA:
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
//                    outputStream.writeBytes(twoHypen + gv_boundary + lineEnd);
                    writer.append(twoHypen).append(gv_boundary).append(lineEnd);
                    String key = keys.next();
                    Object obj = json.get(key);
                    if (obj instanceof File) {
                        File file = (File) obj;
                        FileInputStream fileInputStream = new FileInputStream(file);
                        writer.append("Content-Disposition: form-data; name=\"" + key + "\"" +
                                "; filename=\"" + file.getName() + "\"")
                                .append(lineEnd);
                        writer.append(
                                "Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())
                        )
                                .append(lineEnd);
                        writer.append("Content-Transfer-Encoding: binary").append(lineEnd);
                        writer.append(lineEnd);
                        writer.flush();

//                        // create a buffer of maximum size
                        byte[] buffer = new byte[4096];
                        int bytesRead = -1;
                        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        outputStream.flush();
                        fileInputStream.close();
                        writer.append(lineEnd);
                        writer.flush();
                    } else {
                        writer.append("Content-Disposition: form-data; name=\"" + key + "\"")
                                .append(lineEnd);
                        writer.append("Content-Type: text/plain; charset=" + charset).append(lineEnd);
                        writer.append(lineEnd);
                        writer.append(obj.toString()).append(lineEnd);
                        writer.flush();
                    }
                }
                writer.append(lineEnd).flush();
                writer.append(twoHypen + gv_boundary + twoHypen).append(lineEnd);
                writer.close();
                break;
        }
        Log.e(TAG, "DOS : " + outputStream.toString());
        return outputStream;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object... voids) {
        return uploadFile((JSONObject) voids[0] /*Body*/, (String) voids[1] /*Endpoint*/,
                (HashMap<String, String>) voids[2]/*Headers*/, true);
    }

    @Override
    protected void onPostExecute(String jsonString) {
//        writeToFile(jsonString, this.gv_context);
//        jsonString = readFromFile(this.gv_context);
        if (null != delegate) try {
            delegate.getResponse(new JSONObject(jsonString), endPoint, request_id, status_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(jsonString);
    }

//    @Override
//    protected void onCancelled() {
//        super.onCancelled();
//    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.getAssets().open("dummyData.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("dummyData.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}

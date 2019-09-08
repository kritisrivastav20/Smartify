package com.smartyfy.library;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tecno Waev on 18-12-2016.
 */

public interface JSONResponse {
    void getResponse(JSONObject jsonObject, String trojan, int request_id, int status_code) throws JSONException;
}

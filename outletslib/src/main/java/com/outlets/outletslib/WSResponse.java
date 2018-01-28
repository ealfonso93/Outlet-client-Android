package com.outlets.outletslib;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rufo on 28/01/2018.
 */

public class WSResponse {

    private JSONObject jsonResponse;
    private int status;
    public String id;

    public WSResponse(String response) throws JSONException {
        jsonResponse = new JSONObject(response);
        status = jsonResponse.getInt("status");
        id = jsonResponse.getString("id");
    }

    public int getStatus() throws JSONException {
        return status;
    }

    public String getId() throws JSONException {
        return id;
    }

    public JSONObject getResposeData(){
        return jsonResponse;
    }

    public JSONObject getRawResponse(){
        return jsonResponse;
    }
}

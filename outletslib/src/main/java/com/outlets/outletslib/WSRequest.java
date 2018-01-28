package com.outlets.outletslib;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by rufo on 28/01/2018.
 */

public class WSRequest {

    private String host;
    private String endPoint;
    private HashMap<String,String> params;
    private String method;
    private String uid;

    private PetitionListener petitionListener;

    public WSRequest() {
    }

    public WSRequest(String host, String endPoint, HashMap<String,String> params, String method) {
        this.endPoint = endPoint;
        this.host = host;
        this.params = params;
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public HashMap<String,String> getParams() {
        return params;
    }

    public void setParams(HashMap<String,String> params) {
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public PetitionListener getPetitionListener() {
        return petitionListener;
    }

    public void setPetitionListener(PetitionListener petitionListener) {
        this.petitionListener = petitionListener;
    }

    public String buildPayload() throws JSONException {
        setUid(UUID.randomUUID().toString());

        JSONObject payload = new JSONObject();
        payload.put("method",getMethod());
        payload.put("endpoint",getEndPoint());
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        payload.put("params",params);
        payload.put("id", getUid());

        return payload.toString();
    }


}

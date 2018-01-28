package com.outlets.outletslib;


import android.util.EventLog;

import com.outlets.outletslib.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by rufo on 27/01/2018.
 */

public class OutletService extends WebSocketListener {

    private String host;
    private WebSocket webSocket;
    private Communication comm;

    private WebSocketEvents eventsListener;

    public OutletService(String host) {
        this(host, null);
    }

    public OutletService(String host, WebSocketEvents eventsListener) {
        reset();
        this.host = host;
        comm = new Communication();
        this.eventsListener = eventsListener;
    }

    public void connect(){
        if(!host.toLowerCase().contains("ws://")){
            Log.d("asd","Invalid websocket address");
            reset();
            return;
        }
        Request request = new Request.Builder().url(host).build();
        OkHttpClient client = new OkHttpClient();
        webSocket = client.newWebSocket(request,this);
    }

    public boolean disconnect(){
        if(webSocket == null) return false;
        if (webSocket.close(1000, "Programmer requested close")){
            Log.d("asd", "Websocket being closed..");
            return true;
        }
        Log.d("asd", "Websocket already closed or closing");
        return false;
    }

    public void get(String endpoint, PetitionListener listener) throws JSONException {
        get(endpoint,null, listener);
    }

    public void get(String endpoint, HashMap<String,String> params, PetitionListener listener) throws JSONException {

        if (params == null){
            params = new HashMap<>();
        }

        params.put("timestamp",String.valueOf(System.currentTimeMillis()));

        WSRequest request = new WSRequest();

        request.setHost(host);
        request.setEndPoint(endpoint);
        request.setMethod("GET");
        request.setParams(params);
        request.setPetitionListener(listener);

        comm.addRequest(request);

        webSocket.send(request.buildPayload());
    }

    private void reset(){
        webSocket = null;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        if(eventsListener != null) eventsListener.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);

        WSResponse response = null;
        try {
            response = new WSResponse(text);

            if (response.getStatus() >= 200 && response.getStatus() < 400){
                WSRequest request = comm.getRequest(response.getId());
                comm.removeRequest(request);
                request.getPetitionListener().onSuccessfull(response.getStatus(),response.getResposeData());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(eventsListener != null) eventsListener.onMessage(webSocket,text);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        if(eventsListener != null) eventsListener.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        if(eventsListener != null) eventsListener.onFailure(webSocket, t, response);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public Communication getComm() {
        return comm;
    }
}

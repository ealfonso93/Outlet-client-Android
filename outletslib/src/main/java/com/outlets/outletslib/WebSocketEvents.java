package com.outlets.outletslib;

import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * Created by rufo on 28/01/2018.
 */

public interface WebSocketEvents {
    public void onOpen(WebSocket webSocket, Response response);

    public void onMessage(WebSocket webSocket, String text);

    public void onClosed(WebSocket webSocket, int code, String reason);

    public void onFailure(WebSocket webSocket, Throwable t, Response response);
}

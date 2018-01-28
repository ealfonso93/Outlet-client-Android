package com.outlets.outletslib;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;
import okhttp3.WebSocket;

import static junit.framework.Assert.*;

/**
 * Created by rufo on 28/01/2018.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TestOutletService {

    private class Custom extends OutletService{
        public boolean openCalled;
        public boolean messageCalled;
        public boolean closeCalled;
        public boolean failureCalled;

        Custom(String host){
            super(host);
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            openCalled = true;
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            messageCalled = true;
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            closeCalled = true;
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            failureCalled = true;
        }
    }

    private OutletService outletService;

    @Before
    public void setUp(){
        outletService = new OutletService("ws://192.168.1.136:12345");
        outletService.connect();
    }

    @Test
    public void connectConnectsWebsocket(){

        assertNotNull(outletService.getWebSocket());

        outletService.setHost("s://dummy:moredummy");
        outletService.connect();

        assertNull(outletService.getWebSocket());
    }

    @Test
    public void disconnectClosesWebsocket(){
        boolean disconected = outletService.disconnect();

        assertTrue(disconected);

        disconected = outletService.disconnect();

        assertFalse(disconected);
    }

    @Test
    public void getReceivesResponse() throws JSONException, InterruptedException {

        outletService.connect();

        assertTrue(outletService.getComm().getPendingRequests().size() == 0);

        outletService.get("/api/news",null,null);

        assertTrue(outletService.getComm().getPendingRequests().size() != 0);
    }

    @Test
    public void nullRequestIfNoExists(){
        assertNull(outletService.getComm().getRequest("dummy"));

        WSRequest request = new WSRequest();
        request.setUid("Not dummy");

        outletService.getComm().addRequest(request);

        assertNull(outletService.getComm().getRequest("dummy"));
    }

    @Test
    public void checkCallbackInvocation() throws InterruptedException, JSONException {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Custom customOutlet = new Custom(outletService.getHost());
        customOutlet.connect();

        countDownLatch.await(2000, TimeUnit.MILLISECONDS);

        assertTrue(customOutlet.openCalled);

        customOutlet.get("/nowhere",null);

        countDownLatch.await(2000, TimeUnit.MILLISECONDS);

        assertTrue(customOutlet.messageCalled);

        customOutlet.disconnect();

        countDownLatch.await(2000, TimeUnit.MILLISECONDS);

        assertTrue(customOutlet.closeCalled);

        customOutlet.setHost("s:///sdasda");

        countDownLatch.await(2000, TimeUnit.MILLISECONDS);

        //assertTrue(customOutlet.failureCalled);

    }
}

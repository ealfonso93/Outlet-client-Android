package com.outlets.outletslib;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rufo on 28/01/2018.
 */

public class Communication {

    private List<WSRequest> pendingRequests;

    public Communication() {
        pendingRequests = new ArrayList<>();
    }

    public void addRequest(WSRequest request){
        pendingRequests.add(request);
    }

    public void removeRequest(WSRequest request){
        pendingRequests.remove(request);
    }

    public WSRequest getRequest(String uid){
        for (WSRequest request : pendingRequests) {
           if(request.getUid() == null || uid == null) continue;
           if(request.getUid().equals(uid)) return request;
        }
        return null;
    }

    public List<WSRequest> getPendingRequests(){
        return pendingRequests;
    }
}

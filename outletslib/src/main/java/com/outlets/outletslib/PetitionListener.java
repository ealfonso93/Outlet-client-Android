package com.outlets.outletslib;

import org.json.JSONObject;

/**
 * Created by rufo on 27/01/2018.
 */

public interface PetitionListener {

    void onSuccessfull(int statusCode, JSONObject response);

    void onFailure(int statusCode, JSONObject response);
}

package com.example.h.cloudcycle.WebServiceControl;

import com.google.gson.annotations.SerializedName;

/**
 * Created by H on 12/03/2018.
 */

public class GeneralResponse {

    String code;

    @SerializedName("success")
    boolean success;

    public String getCode() {
        return code;
    }

    public boolean isSuccess() {
        return success;
    }
}

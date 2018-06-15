package com.example.h.cloudcycle.WebServiceControl;

import com.google.gson.annotations.SerializedName;

/**
 * Created by H on 20/02/2018.
 */

public class Bike {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lng")
    private double longitude;
    @SerializedName("user_id")
    private double user_id;

    public double getLatitude() {
        return latitude;
    }

    public double getUser_id() {
        return user_id;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

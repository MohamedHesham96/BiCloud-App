package com.example.h.cloudcycle.WebServiceControl;

import com.google.gson.annotations.SerializedName;

/**
 * Created by H on 10/02/2018.
 */

public class History {

    @SerializedName("id")
    String id;

    @SerializedName("bike_id")
    String bike_id;

    @SerializedName("distance")
    String distance;

    @SerializedName("price")
    String price;

    @SerializedName("date")
    String date;

    public String getBike_id() {
        return bike_id;
    }

    public String getDistance() {
        return distance;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "History{" +
                "id='" + id + '\'' +
                ", bike_id='" + bike_id + '\'' +
                ", distance='" + distance + '\'' +
                ", price='" + price + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}

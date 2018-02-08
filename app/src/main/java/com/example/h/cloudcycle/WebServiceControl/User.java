package com.example.h.cloudcycle.WebServiceControl;

import com.google.gson.annotations.SerializedName;

/**
 * Created by H on 09/02/2018.
 */

public class User {

    @SerializedName("id")
    int id;

    @SerializedName("balance")
    float balance;

    @SerializedName("name")
    String name;

    @SerializedName("email")
    String email;

    @SerializedName("token")
    String token;

    @SerializedName("img")
    String image;

    public int getId() {
        return id;
    }

    public float getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getImage() {
        return image;
    }
}

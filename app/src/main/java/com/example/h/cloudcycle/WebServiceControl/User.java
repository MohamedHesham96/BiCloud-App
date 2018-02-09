package com.example.h.cloudcycle.WebServiceControl;

import com.google.gson.annotations.SerializedName;

/**
 * Created by H on 09/02/2018.
 */

public class User {

    @SerializedName("id")
    int id;

    @SerializedName("type")
    String type;

    @SerializedName("balance")
    String balance;

    @SerializedName("name")
    String name;

    @SerializedName("email")
    String email;

    @SerializedName("token")
    String token;

    @SerializedName("img")
    String img;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", balance=" + balance +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", image='" + img + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getBalance() {
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
        return img;
    }
}

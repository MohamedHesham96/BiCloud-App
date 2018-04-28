package com.example.h.cloudcycle.WebServiceControl;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;


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

    @SerializedName("verified")
    String verified;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", balance='" + balance + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", img='" + img + '\'' +
                ", verified='" + verified + '\'' +
                '}';
    }

    public String getVerified() {
        return verified;
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

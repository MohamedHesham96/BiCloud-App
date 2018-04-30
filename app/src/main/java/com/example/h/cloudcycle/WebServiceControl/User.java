package com.example.h.cloudcycle.WebServiceControl;

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
    boolean verified;

    @SerializedName("success")
    boolean success;

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

    public boolean isSuccess() {
        return success;
    }

    public boolean isVerified() {
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
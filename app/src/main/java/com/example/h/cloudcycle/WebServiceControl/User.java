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
    int verified;

    @SerializedName("success")
    boolean success;


    @SerializedName("created_at")
    String created_at;


    @SerializedName("updated_at")
    String updated_at;

    public boolean isSuccess() {
        return success;
    }

    public int getVerified() {
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

    @Override
    public String toString() {
        return "Name: " + name + "\nEmail: " + email;
    }
}
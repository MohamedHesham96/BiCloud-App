package com.example.h.cloudcycle.WebServiceControl;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by H on 10/02/2018.
 */

public class SignupResponse {

    @SerializedName("success")
    String[] success;

    @SerializedName("email")
    String[] email;

    @SerializedName("password")
    String[] password;

    @SerializedName("token")
    String[] token;

    @SerializedName("img")
    String[] img;

    @Override
    public String toString() {
        return "SignupResponse{" +
                "success=" + Arrays.toString(success) +
                ", email=" + Arrays.toString(email) +
                ", password=" + Arrays.toString(password) +
                ", token=" + Arrays.toString(token) +
                ", img=" + Arrays.toString(img) +
                '}';
    }

    public String[] getPassword() {
        return password;
    }

    public String[] getImg() {
        return img;
    }

    public String[] getSuccess() {
        return success;
    }

    public String[] getToken() {
        return token;
    }

    public String[] getEmail() {
        return email;
    }
}

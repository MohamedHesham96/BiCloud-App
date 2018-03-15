package com.example.h.cloudcycle.WebServiceControl;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by H on 09/02/2018.
 */

public interface ApiInterface {

    // Login / Sign up Management
    @POST("user/login")
    Call<User> getUserInfo(@Query("email") String email, @Query("password") String password);

    @Multipart
    @POST("user/signup")
    Call<SignupResponse> createNewUser(@Query("name") String userName, @Query("email") String userEmail,
                                       @Query("password") String password,
                                       @Part MultipartBody.Part image);

    @POST("user/signup")
    Call<SignupResponse> createNewUser(@Query("name") String userName,
                                       @Query("email") String userEmail,
                                       @Query("password") String password);

    @POST("user/history")
    Call<List<History>> getUserHistory(@Query("id") int id);

    // Update User Data
    @POST("user/update/name")
    Call<GeneralResponse> updateUserName(@Query("id") String id, @Query("name") String name);

    @POST("user/update/email")
    Call<GeneralResponse> updateUserEmail(@Query("id") String id, @Query("email") String email);

    @POST("user/update/password")
    Call<GeneralResponse> updateUserPassword(@Query("id") String id, @Query("password") String password,
                                             @Query("repassword") String rePassword);

    @Multipart
    @POST("user/update/photo")
    Call<GeneralResponse> updateUserPhoto(@Query("id") String id, @Part MultipartBody.Part image);

    @GET("bike/lockedbikes")
    Call<List<Bike>> getLockedBikes();

    // Password Management
    @POST("forget/password")
    Call<GeneralResponse> forgetPassword(@Query("email") String email);

    @POST("reset/password")
    Call<GeneralResponse> resetPassword(@Query("email") String email, @Query("password") String password);

}

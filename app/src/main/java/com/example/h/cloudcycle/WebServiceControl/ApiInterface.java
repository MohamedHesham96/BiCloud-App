package com.example.h.cloudcycle.WebServiceControl;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by H on 09/02/2018.
 */

public interface ApiInterface {

    @POST("login")
    Call<User> getUserInfo(@Query("email") String email, @Query("password") String password);

    @POST("history")
    Call<List<History>> getUserHistory(@Query("id") int id);

    @Multipart
    @POST("signup")
    Call<SignupResponse> createNewUser(@Query("name") String userName,
                                       @Query("email") String userEmail,
                                       @Query("password") String password,
                                       @Part MultipartBody.Part image);

    @POST("signup")
    Call<SignupResponse> createNewUser(@Query("name") String userName,
                                       @Query("email") String userEmail,
                                       @Query("password") String password);

    @GET("lockedbikes")
    Call<List<Bike>> getLockedBikes();

}

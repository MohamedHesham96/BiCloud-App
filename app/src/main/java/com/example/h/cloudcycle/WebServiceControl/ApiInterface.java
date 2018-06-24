package com.example.h.cloudcycle.WebServiceControl;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
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
    Call<User> getUserInfo(@Query("email") String email, @Query("password") String password, @Query("client_id") String clientId, @Query("client_pass") String clientPass);

    @Multipart
    @POST("user/signup")
    Call<SignupResponse> createNewUser(@Query("name") String userName, @Query("email") String userEmail,
                                       @Query("password") String password,
                                       @Part MultipartBody.Part image, @Query("client_id") String clientId, @Query("client_pass") String clientPass);

    @POST("user/signup")
    Call<SignupResponse> createNewUser(@Query("name") String userName,
                                       @Query("email") String userEmail,
                                       @Query("password") String password, @Query("client_id") String clientId, @Query("client_pass") String clientPass);

    @POST("user/history")
    Call<List<History>> getUserHistory(@Query("id") String id, @Query("client_id") String clientId, @Query("client_pass") String clientPass);


    @POST("user/remove/history")
    Call<GeneralResponse> deleteAllHistory(@Query("id") String id, @Query("client_id") String clientId, @Query("client_pass") String clientPass);


    @POST("user/remove/history/1")
    Call<GeneralResponse> deleteOneHistory(@Query("history_id") String id, @Query("user_id") String userId, @Query("client_id") String clientId, @Query("client_pass") String clientPass);


    // Update User Data
    @POST("user/update/name")
    Call<GeneralResponse> updateUserName(@Query("id") String id, @Query("email") String email, @Query("name") String name, @Query("client_id") String clientId, @Query("client_pass") String clientPass);

    @POST("user/update/email")
    Call<GeneralResponse> updateUserEmail(@Query("id") String id, @Query("email") String email,
                                          @Query("remail") String remail, @Query("client_id") String clientId, @Query("client_pass") String clientPass);

    @POST("user/update/password")
    Call<GeneralResponse> updateUserPassword(@Query("id") String id, @Query("email") String email,
                                             @Query("password") String password,
                                             @Query("repassword") String rePassword,
                                             @Query("client_id") String clientId, @Query("client_pass") String clientPass);

    // Update User Data
    @PUT("user/delete/account")
    Call<GeneralResponse> deleteAccount(@Query("id") String id, @Query("password") String password, @Query("client_id") String clientId, @Query("client_pass") String clientPass);

    @POST("user/feedback/create")
    Call<GeneralResponse> createFeedbackForUser(@Query("user_id") String id, @Query("content") String content, @Query("client_id") String clientId, @Query("client_pass") String clientPass);

    @POST("maintenance/feedback/create")
    Call<GeneralResponse> createFeedbackForMaintenance(@Query("maintenance_id") String id, @Query("content") String content, @Query("client_id") String clientId, @Query("client_pass") String clientPass);

    // Update User Data
    @Multipart
    @PUT("user/update/photo")
    Call<GeneralResponse> updateUserPhoto(@Query("id") String id, @Query("email") String email,
                                          @Part MultipartBody.Part image, @Query("client_id") String clientId, @Query("client_pass") String clientPass);

    @POST("bike/lockedbikes")
    Call<List<Bike>> getLockedBikes(@Query("client_id") String clientId, @Query("client_pass") String clientPass);

    @POST("bike/bikes")
    Call<List<Bike>> getAllBikes(@Query("client_id") String clientId, @Query("client_pass") String clientPass);

    // Password Management
    @POST("forget/password")
    Call<GeneralResponse> forgetPassword(@Query("email") String email, @Query("client_id") String clientId, @Query("client_pass") String clientPass);

    @PUT("reset/password")
    Call<GeneralResponse> resetPassword(@Query("email") String email, @Query("password") String password, @Query("client_id") String clientId, @Query("client_pass") String clientPass);

    @POST("complain/Complain/all")
    Call<List<Complain>> getAllComplains(@Query("client_id") String clientId, @Query("client_pass") String clientPass);

    @POST("complain/selectComplain/user")
    Call<GeneralResponse> makeComplainForUser(@Query("bike_id") String bikeId, @Query("complain_id") String complainId, @Query("user_id") String userId, @Query("client_id") String clientId, @Query("client_pass") String clientPass);

    @POST("complain/selectComplain/maintenance")
    Call<GeneralResponse> makeComplainForSuperVisor(@Query("bike_id") String bikeId, @Query("complain_id") String complainId, @Query("maintenance_id") String superVisorId, @Query("client_id") String clientId, @Query("client_pass") String clientPass);


    @POST("show/user")
    Call<User> getUserData(@Query("user_id") String userId, @Query("client_id") String clientId, @Query("client_pass") String clientPass);

}

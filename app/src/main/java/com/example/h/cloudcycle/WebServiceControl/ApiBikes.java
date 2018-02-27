package com.example.h.cloudcycle.WebServiceControl;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by H on 20/02/2018.
 */

public class ApiBikes {


    public static final String BASE_URL = "https://mousaelenanyfciscu.000webhostapp.com/app/webservice/bike/";
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit;
    }
}

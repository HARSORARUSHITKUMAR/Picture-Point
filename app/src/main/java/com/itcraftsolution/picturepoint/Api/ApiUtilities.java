package com.itcraftsolution.picturepoint.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtilities {

    public static final String BASE_URL ="https://api.unsplash.com";
    public static final String APi_KEY = "3rkesOb93PLDzN1ntU9WHGgesMxUhHGfPlXF7mkv8zI";

    public static Retrofit retrofit = null;

    public static ApiInterface apiInterface()
    {
        if(retrofit == null)
        {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }
}

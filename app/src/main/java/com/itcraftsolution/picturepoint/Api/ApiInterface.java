package com.itcraftsolution.picturepoint.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static com.itcraftsolution.picturepoint.Api.ApiUtilities.APi_KEY;

import com.itcraftsolution.picturepoint.Models.ImageModel;
import com.itcraftsolution.picturepoint.Models.SearchModel;

import java.util.List;

public interface ApiInterface {
    //Image Get
    @Headers("Authorization: " + APi_KEY)
    @GET("curated")
    Call<SearchModel> getImages(
            @Query("page") int page,
            @Query("per_page") int perPage
    );

    //Search Images
    @Headers("Authorization: " + APi_KEY)
    @GET("search")
    Call<SearchModel> SearchImages(
            @Query("query") String query,
            @Query("page") int searchpage,
            @Query("per_page") int searchperPage
    );
}

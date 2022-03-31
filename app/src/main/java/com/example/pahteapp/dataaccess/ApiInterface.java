package com.example.pahteapp.dataaccess;

import com.example.pahteapp.domain.DiscoveredMovies;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("discover/movie")
    Call<DiscoveredMovies> getMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );
}

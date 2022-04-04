package com.example.pahteapp.dataaccess;

import com.example.pahteapp.domain.Authenticate;
import com.example.pahteapp.domain.DiscoverGenres;
import com.example.pahteapp.domain.DiscoveredMovies;
import com.example.pahteapp.domain.Genre;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("discover/movie")
    Call<DiscoveredMovies> getMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page,
            @Query("vote_average.gte") Integer rating,
            @Query("with_genres") String genre,
            @Query("sort_by") String sort
    );

    @GET("genre/movie/list")
    Call<DiscoverGenres> getGenres(
            @Query("api_key") String apiKey

    );

    @GET("authentication/token/new")
    Call<Authenticate> getRequestToken(
            @Query("api_key") String apiKey
    );

    @POST("authentication/token/validate_with_login")
    @FormUrlEncoded
    Call<Authenticate> doLogin(
            @Query("api_key") String apiKey,
            @Field("username") String username,
            @Field("password") String password,
            @Field("request_token") String requestToken
    );

    @POST("authentication/session/new")
    @FormUrlEncoded
    Call<Authenticate> createSession(
            @Query("api_key") String apiKey,
            @Field("request_token") String requestToken
    );

    @POST("authentication/guest_session/new")
    Call<Authenticate> createGuestSession(
            @Query("api_key") String apiKey
    );
}

package com.example.pahteapp.dataaccess;

import com.example.pahteapp.domain.Authenticate;
import com.example.pahteapp.domain.DiscoverGenres;
import com.example.pahteapp.domain.DiscoveredMovies;
import com.example.pahteapp.domain.MovieList;
import com.example.pahteapp.domain.PaginatedUserList;
import com.example.pahteapp.domain.User;
import com.example.pahteapp.domain.Movie;
import com.example.pahteapp.domain.UserList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    @GET("search/movie")
    Call<DiscoveredMovies> getMoviesByName(
            @Query("api_key") String apiKey,
            @Query("query") String query
    );

    @GET("genre/movie/list")
    Call<DiscoverGenres> getGenres(
            @Query("api_key") String apiKey

    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") Integer id,
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

    @POST("list")
    Call<Authenticate> createUserList(
            @Header("Content-Type") String charset,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionID,
            @Body UserList list
    );

    @POST("list/{list_id}/add_item")
    @FormUrlEncoded
    Call<Authenticate> addMovieToList(
            @Path("list_id") String list_id,
            @Header("Content-Type") String charset,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionID,
            @Field("media_id") Integer movieID
    );

    @GET("account/{account_id}/lists")
    Call<PaginatedUserList> getAllListsUser(
            @Path("account_id") Integer accountId,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId
    );

    @GET("account")
    Call<User> getUser(
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId
    );

    @GET("list/{list_id}")
    Call<MovieList> getList(
            @Path("list_id") Integer listId,
            @Query("api_key") String apiKey
    );
}

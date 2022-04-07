package com.example.pahteapp.dataaccess;

import com.example.pahteapp.domain.login.Authenticate;
import com.example.pahteapp.domain.genre.DiscoverGenres;
import com.example.pahteapp.domain.movie.DiscoveredMovies;
import com.example.pahteapp.domain.list.MovieList;
import com.example.pahteapp.domain.list.PaginatedUserList;
import com.example.pahteapp.domain.login.User;
import com.example.pahteapp.domain.movie.Movie;
import com.example.pahteapp.domain.list.UserList;

import retrofit2.Call;
import retrofit2.http.Body;
import com.example.pahteapp.domain.reviews.PaginatedReviews;

import retrofit2.http.DELETE;
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
    Call<Authenticate> addMovieToList(
            @Path("list_id") Integer list_id,
            @Header("Content-Type") String charset,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionID,
            @Body Movie movie
    );

    @GET("movie/{movie_id}/reviews")
    Call<PaginatedReviews> getMovieReviews(
            @Path("movie_id") Integer movieId,
            @Query("api_key") String apiKey,
            @Query("page") Integer page
    );

    @GET("movie/{movie_id}/videos")
    Call<Movie> getMovieTrailers(
            @Path("movie_id") Integer movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language
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

    @DELETE("authentication/session")
    Call<Authenticate> logout(
            @Query("api_Key") String apiKey,
            @Query("session_id") String sessionId
    );

    @POST("list/{list_id}/remove_item")
    @FormUrlEncoded
    Call<Authenticate> deleteMovieFromList(
            @Path("list_id") Integer listId,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId,
            @Field("media_id") Integer movieId
    );

    @POST("movie/{movie_id}/rating")
    @FormUrlEncoded
    Call<Authenticate> giveMovieRating(
            @Path("movie_id") Integer movieId,
            @Query("api_key") String apiKey,
            @Query("guest_session_id") String guestSessionId,
            @Query("session_id") String sessionId,
            @Field("value") float rating
    );
}

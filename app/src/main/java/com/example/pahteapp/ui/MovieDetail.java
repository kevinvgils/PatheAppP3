package com.example.pahteapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pahteapp.R;
import com.example.pahteapp.dataaccess.ApiClient;
import com.example.pahteapp.dataaccess.ApiInterface;
import com.example.pahteapp.domain.DiscoveredMovies;
import com.example.pahteapp.domain.Movie;
import com.example.pahteapp.domain.reviews.PaginatedReviews;
import com.example.pahteapp.domain.reviews.Review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetail extends AppCompatActivity {

    private Movie selectedMovie;
    private TextView movieTitle;
    private ImageView movieBanner;
    private RatingBar movieRating;
    private TextView movieRatingText;
    private TextView movieDuration;
    private TextView movieGenres;
    private TextView movieDescription;
    private ReviewAdapter mAdapter;

    private final List<Review> nReviewList = new ArrayList<>();
    Integer page = 1;

    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        movieTitle = findViewById(R.id.movieTitle);
        movieBanner = findViewById(R.id.movieBanner);
        movieRating = findViewById(R.id.movieRating);
        movieRatingText = findViewById(R.id.movieRatingText);
        movieDuration = findViewById(R.id.movieDuration);
        movieGenres = findViewById(R.id.movieGenres);
        movieDescription = findViewById(R.id.movieDescription);

        setAdapter();
        getMovie();
    }

    private void setAdapter() {
        mRecyclerView = findViewById(R.id.reviewRecyclerView);
        mAdapter = new ReviewAdapter(this, nReviewList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private Integer getId(){
        return (int) getIntent().getSerializableExtra("id");
    }

    private void set_data(){
        movieTitle.setText(selectedMovie.getTitle());
        Picasso.get().load("https://image.tmdb.org/t/p/w500/" + selectedMovie.getBackdropPath()).into(movieBanner);
        movieRating.setRating((float) (selectedMovie.getVoteAverage() / 2));
        movieRatingText.setText(selectedMovie.getVoteAverage() / 2 + " / 5");
        movieDuration.setText(selectedMovie.getRuntime() / 60 + "h " + selectedMovie.getRuntime() % 60 + "m");
        movieDescription.setText(selectedMovie.getOverview());

        if(selectedMovie.getGenreIds() != null && selectedMovie.getGenreIds().size() != 0){
            String genres = "Genres: ";
            for(Integer genre : selectedMovie.getGenreIds()){
                genres = genres.concat(genre+", ");
            }
            genres = genres.substring(0, genres.length()-2);
            movieGenres.setText(genres);
        } else {
            movieGenres.setText("No genres found");
        }
    }

    private void getMovie(){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<Movie> call = apiInterface.getMovie(getId(), "1e2c1f57cbed4d3e0c5dcad5996f2649");

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                selectedMovie = response.body();
                Log.d("Response", getId() + "");
                set_data();
                getReview();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("MainActivity", t.toString());
            }
        });
    }

    private void getReview() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<PaginatedReviews> call = apiInterface.getMovieReviews(getId(), "1e2c1f57cbed4d3e0c5dcad5996f2649", page);

        call.enqueue(new Callback<PaginatedReviews>() {
            @Override
            public void onResponse(Call<PaginatedReviews> call, Response<PaginatedReviews> response) {
                PaginatedReviews paginatedReviews = response.body();
                nReviewList.addAll(paginatedReviews.getResults());
                mAdapter.setReviewList(nReviewList);
            }

            @Override
            public void onFailure(Call<PaginatedReviews> call, Throwable t) {

            }
        });

    }
}

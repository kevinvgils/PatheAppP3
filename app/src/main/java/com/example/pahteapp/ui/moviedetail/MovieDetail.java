package com.example.pahteapp.ui.moviedetail;

import static com.example.pahteapp.ui.login.login.IS_GUEST;
import static com.example.pahteapp.ui.login.login.SESSION_ID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pahteapp.R;
import com.example.pahteapp.dataaccess.ApiClient;
import com.example.pahteapp.dataaccess.ApiInterface;
import com.example.pahteapp.domain.login.Authenticate;
import com.example.pahteapp.dataaccess.Logout;
import com.example.pahteapp.domain.genre.Genre;
import com.example.pahteapp.domain.movie.Movie;
import com.example.pahteapp.domain.reviews.PaginatedReviews;
import com.example.pahteapp.domain.reviews.Review;
import com.example.pahteapp.ui.list.UserListActivity;
import com.example.pahteapp.ui.login.login;
import com.example.pahteapp.ui.movie.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    private PaginatedReviews paginatedReviews;
    private Button reviewSubmit;
    private RatingBar reviewRating;


    private final List<Review> nReviewList = new ArrayList<>();
    Integer page = 1;

    private RecyclerView mRecyclerView;

    private boolean trailerSearchDone;
    private WebView mTrailerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("PatheApp");
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.lists) {
                    if(IS_GUEST) {
                        Toast.makeText(getApplicationContext(), "Can't view lists as guest", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                        startActivity(intent);
                    }
                } else if(item.getItemId() == R.id.logout) {
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                    Logout.doLogout(getApplicationContext());
                } else if(item.getItemId() == R.id.home) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                return false;
            }
        });

        movieTitle = findViewById(R.id.movieTitle);
        movieBanner = findViewById(R.id.movieBanner);
        movieRating = findViewById(R.id.movieRating);
        movieRatingText = findViewById(R.id.movieRatingText);
        movieDuration = findViewById(R.id.movieDuration);
        movieGenres = findViewById(R.id.movieGenres);
        movieDescription = findViewById(R.id.movieDescription);
        reviewSubmit = findViewById(R.id.reviewSubmitButton);
        reviewRating = findViewById(R.id.reviewRating);

        mTrailerView = findViewById(R.id.trailerView);
        mTrailerView.setWebViewClient(new WebViewClient());
        WebSettings trailerSettings = mTrailerView.getSettings();
        trailerSettings.setJavaScriptEnabled(true);

        movieBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!trailerSearchDone){
                    getTrailer();
                }
                trailerSearchDone = true;
            }
        });

        Button addMovieButton = findViewById(R.id.addMovieButton);
        if(!IS_GUEST){
            addMovieButton.setVisibility(View.VISIBLE);
            addMovieButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                    intent.putExtra("movie", selectedMovie.getId());
                    startActivity(intent);
                }
            });
        }

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

        if(selectedMovie.getGenreList() != null && selectedMovie.getGenreList().size() != 0){
            String genres = "";
            for(Genre genre : selectedMovie.getGenreList()){
                genres = genres.concat(genre.getName()+", ");
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

        reviewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Authenticate> call;
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                if (IS_GUEST) {
                    call = apiInterface.giveMovieRating(selectedMovie.getId(), "1e2c1f57cbed4d3e0c5dcad5996f2649" ,SESSION_ID, null, (reviewRating.getRating() * 2));
                } else {
                    call = apiInterface.giveMovieRating(selectedMovie.getId(), "1e2c1f57cbed4d3e0c5dcad5996f2649",null, SESSION_ID, reviewRating.getRating() * 2);
                }

                call.enqueue(new Callback<Authenticate>() {
                    @Override
                    public void onResponse(Call<Authenticate> call, Response<Authenticate> response) {
                        if (!response.isSuccessful()) return;
                        Toast.makeText(getApplicationContext(), "Review added", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Authenticate> call, Throwable t) {

                    }
                });

            }
        });
    }

    private void getReview() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<PaginatedReviews> call = apiInterface.getMovieReviews(getId(), "1e2c1f57cbed4d3e0c5dcad5996f2649", page);

        call.enqueue(new Callback<PaginatedReviews>() {
            @Override
            public void onResponse(Call<PaginatedReviews> call, Response<PaginatedReviews> response) {
                paginatedReviews = response.body();
                nReviewList.addAll(paginatedReviews.getResults());
                Log.d("Review", "" + paginatedReviews.getTotalPages());
                mAdapter.setReviewList(nReviewList);
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (!recyclerView.canScrollVertically(1) && !(page > paginatedReviews.getTotalPages())) {
                            Log.d("Scroll", "Bottom reached");
                            page++;
                            getReview();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<PaginatedReviews> call, Throwable t) {

            }
        });

    }

    private void showTrailer(){
        mTrailerView.setVisibility(View.VISIBLE);
        movieBanner.setVisibility(View.INVISIBLE);
        movieTitle.setVisibility(View.INVISIBLE);
    }

    private void getTrailer(){
        final boolean[] output = {false};
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<Movie> call = apiInterface.getMovieTrailers(getId(), "1e2c1f57cbed4d3e0c5dcad5996f2649", "nl");

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                try {
                    Log.d("Trailer Response", response.toString());
                    Movie allTrailers = response.body();
                    Log.d("Trailer Response Object", allTrailers.getResults().get(0).getKey());
                    if(allTrailers.getResults() != null){
                        mTrailerView.loadUrl("https://www.youtube.com/embed/"+allTrailers.getResults().get(0).getKey());
                        showTrailer();
                    } else{
                        throw new NullPointerException("No movies found");
                    }

                } catch (Exception e){
                    Log.w("Trailer Warning",e);
                    Toast.makeText(getApplicationContext(), "Geen film trailers gevonden", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d("Trailer Response", selectedMovie.getId()+"");
                Log.e("Trailer Error", t.toString());
            }
        });
    }
}

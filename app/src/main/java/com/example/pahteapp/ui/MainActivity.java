package com.example.pahteapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pahteapp.R;
import com.example.pahteapp.dataaccess.ApiClient;
import com.example.pahteapp.dataaccess.ApiInterface;
import com.example.pahteapp.domain.DiscoverGenres;
import com.example.pahteapp.domain.DiscoveredMovies;
import com.example.pahteapp.domain.Genre;
import com.example.pahteapp.domain.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final LinkedList<Movie> nMovieList = new LinkedList<>();
    Integer page = 1;

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ProgressBar progressBar;

    private LinkedList<Genre> mGenreList = new LinkedList<>();
    private FilterAdapter mFilterAdapter;
    private RecyclerView mGenresRecyclerView;

    private String mGenre;
    private Integer mRating;
    private String mSorting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        setAdapters();
        getMovies(false);
        setUpFilters();

        //Waneer laatste item gehaald in recyclerview haal volgende pagina op
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    progressBar.setVisibility(View.VISIBLE);
                    page++;
                    getMovies(false);
                }
            }
        });
    }

    private void setUpFilters() {
        Switch filterSwitch = findViewById(R.id.FiltersSwitch);
        Button searchButton = findViewById(R.id.submitFilterButton);
        filterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout FilterWrap = findViewById(R.id.FilterWrap);
                if (filterSwitch.isChecked()){
                    FilterWrap.setVisibility(View.VISIBLE);
                } else{
                    FilterWrap.setVisibility(View.GONE);
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMovies(true);
            }
        });

        getGenres();
    }

    private void getFilters() {

        //filteren
        mGenre = mFilterAdapter.getFilteredGenres();

        EditText minRating = findViewById(R.id.minRating);
        if(!minRating.getText().toString().equals("")){
            mRating = Integer.parseInt(minRating.getText().toString());
        }

        //sorteren

        RadioButton name = findViewById(R.id.sortName);
        RadioButton date = findViewById(R.id.sortDate);
        RadioButton rating = findViewById(R.id.sortRating);

        StringBuilder sortBuilder = new StringBuilder();
        if(name.isChecked()){
            sortBuilder.append("original_title");
        } else if(date.isChecked()){
            sortBuilder.append("release_date");
        } else if(rating.isChecked()){
            sortBuilder.append("vote_average");
        }

        Switch decendSwitch = findViewById(R.id.decendSwitch);
        if(decendSwitch.isChecked()){
            sortBuilder.append(".desc");
        } else{
            sortBuilder.append(".asc");
        }

        mSorting = sortBuilder.toString();

    }

    private void setAdapters() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new MovieAdapter(this, nMovieList);
        mRecyclerView.setAdapter(mAdapter);
        int gridColumnCount = 2;

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridColumnCount));

        //filters
        mGenresRecyclerView = findViewById(R.id.GenresRecyclerView);
        mFilterAdapter = new FilterAdapter(this, mGenreList);
        mGenresRecyclerView.setAdapter(mFilterAdapter);
        mGenresRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void getGenres() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        try{
            Call<DiscoverGenres> call = apiInterface.getGenres("1e2c1f57cbed4d3e0c5dcad5996f2649");
            call.enqueue(new Callback<DiscoverGenres>() {
                @Override
                public void onResponse(Call<DiscoverGenres> call, Response<DiscoverGenres> response) {
                    DiscoverGenres genreList = response.body();
                    mGenreList.addAll(genreList.getGenres());

                    if(mGenreList != null){
                        mFilterAdapter.setMGenreList(mGenreList);
                        Log.d("Filters", mFilterAdapter.getItemCount() + "");
                        mFilterAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("GenreListGenres", mGenreList.toString());
                    }
                }

                @Override
                public void onFailure(Call<DiscoverGenres> call, Throwable t) {
                    Log.e("MainActivity", t.toString());
                }
            });
        }
        catch (Exception e){
            Log.e("GenreError", e.toString());
        }
    }

    private void getMovies(Boolean filter) {
        if(filter){
            getFilters();
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DiscoveredMovies> call = apiInterface.getMovies("1e2c1f57cbed4d3e0c5dcad5996f2649", page, mRating, mGenre, mSorting);

        call.enqueue(new Callback<DiscoveredMovies>() {
            @Override
            public void onResponse(Call<DiscoveredMovies> call, Response<DiscoveredMovies> response) {
                progressBar.setVisibility(View.GONE);
                DiscoveredMovies movies = response.body();
                if(filter){
                    nMovieList.clear();
                }
                nMovieList.addAll(movies.getResults());
                Log.d("MovieListMovies", nMovieList.toString());
                mAdapter.setMovieList(nMovieList);
            }

            @Override
            public void onFailure(Call<DiscoveredMovies> call, Throwable t) {
                Log.e("MainActivity", t.toString());
            }
        });
    }

}
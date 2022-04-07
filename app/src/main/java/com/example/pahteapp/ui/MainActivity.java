package com.example.pahteapp.ui;

import static com.example.pahteapp.ui.login.IS_GUEST;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.Switch;

import com.example.pahteapp.R;
import com.example.pahteapp.dataaccess.ApiClient;
import com.example.pahteapp.dataaccess.ApiInterface;
import com.example.pahteapp.dataaccess.Logout;
import com.example.pahteapp.domain.genre.DiscoverGenres;
import com.example.pahteapp.domain.movie.DiscoveredMovies;
import com.example.pahteapp.domain.genre.Genre;
import com.example.pahteapp.domain.movie.Movie;

import java.util.LinkedList;

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
        getSupportActionBar().hide();
        progressBar = findViewById(R.id.progress_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("PatheApp");
        toolbar.inflateMenu(R.menu.main_menu);
        setAdapters();
        getMovies(false);
        setUpFilters();

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
                }

                return false;
            }
        });

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
        ConstraintLayout FilterWrap = findViewById(R.id.FilterWrap);
        Switch filterSwitch = findViewById(R.id.FiltersSwitch);
        Button filterButton = findViewById(R.id.submitFilterButton);
        Button searchButton = findViewById(R.id.submitSearchButton);
        filterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (filterSwitch.isChecked()){
                    FilterWrap.setVisibility(View.VISIBLE);
                } else{
                    hideFilters(view);
                }
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMovies(true);
                hideFilters(view);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText FilterMovieTitle = findViewById(R.id.FilterMovieTitle);
                if(!FilterMovieTitle.getText().toString().equals("")){
                    getMovie(FilterMovieTitle.getText().toString());
                    hideFilters(view);
                }
            }
        });

        getGenres();
    }

    private void hideFilters(View view){
        ConstraintLayout FilterWrap = findViewById(R.id.FilterWrap);
        Switch filterSwitch = findViewById(R.id.FiltersSwitch);
        FilterWrap.setVisibility(View.GONE);
        filterSwitch.setChecked(false);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        int gridColumnCount = getResources().getInteger(R.integer.grid_column_count);

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

    private void getMovie(String query) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DiscoveredMovies> call = apiInterface.getMoviesByName("1e2c1f57cbed4d3e0c5dcad5996f2649", query);

        call.enqueue(new Callback<DiscoveredMovies>() {
            @Override
            public void onResponse(Call<DiscoveredMovies> call, Response<DiscoveredMovies> response) {
                progressBar.setVisibility(View.GONE);
                DiscoveredMovies movies = response.body();
                nMovieList.clear();
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
                mAdapter.setMovieList(nMovieList);
            }

            @Override
            public void onFailure(Call<DiscoveredMovies> call, Throwable t) {
                Log.e("MainActivity", t.toString());
            }
        });
    }

}
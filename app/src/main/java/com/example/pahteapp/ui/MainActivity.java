package com.example.pahteapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.example.pahteapp.R;
import com.example.pahteapp.dataaccess.ApiClient;
import com.example.pahteapp.dataaccess.ApiInterface;
import com.example.pahteapp.domain.DiscoveredMovies;
import com.example.pahteapp.domain.Movie;

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

    private Switch mFilterSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        setAdapter();
        getAllMovies();

        //Waneer laatste item gehaald in recyclerview haal volgende pagina op
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    progressBar.setVisibility(View.VISIBLE);
                    page++;
                    getAllMovies();
                }
            }
        });

        setUpFilters();
    }

    private void setUpFilters() {
        mFilterSwitch = findViewById(R.id.FiltersSwitch);
        mFilterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout FilterWrap = findViewById(R.id.FilterWrap);
                if (mFilterSwitch.isChecked()){
                    FilterWrap.setVisibility(View.VISIBLE);
                } else{
                    FilterWrap.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setAdapter() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new MovieAdapter(this, nMovieList);
        mRecyclerView.setAdapter(mAdapter);
        int gridColumnCount = 2;

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridColumnCount));
    }

    private void getAllMovies() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<DiscoveredMovies> call = apiInterface.getMovies("1e2c1f57cbed4d3e0c5dcad5996f2649", page, "18,28");

        call.enqueue(new Callback<DiscoveredMovies>() {
            @Override
            public void onResponse(Call<DiscoveredMovies> call, Response<DiscoveredMovies> response) {
                progressBar.setVisibility(View.GONE);
                DiscoveredMovies movies = response.body();
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
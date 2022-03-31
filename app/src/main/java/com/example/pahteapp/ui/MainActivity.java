package com.example.pahteapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

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

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new MovieAdapter(this, nMovieList);
        mRecyclerView.setAdapter(mAdapter);
        int gridColumnCount = 2;

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridColumnCount));

        getAllMovies();
    }

    private void getAllMovies() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<DiscoveredMovies> call = apiInterface.getMovies("1e2c1f57cbed4d3e0c5dcad5996f2649");

        call.enqueue(new Callback<DiscoveredMovies>() {
            @Override
            public void onResponse(Call<DiscoveredMovies> call, Response<DiscoveredMovies> response) {
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
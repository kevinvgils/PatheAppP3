package com.example.pahteapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pahteapp.R;
import com.example.pahteapp.dataaccess.ApiClient;
import com.example.pahteapp.dataaccess.ApiInterface;
import com.example.pahteapp.domain.DiscoveredMovies;
import com.example.pahteapp.domain.Movie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<DiscoveredMovies> call = apiInterface.getMovies("1e2c1f57cbed4d3e0c5dcad5996f2649");

        call.enqueue(new Callback<DiscoveredMovies>() {
            @Override
            public void onResponse(Call<DiscoveredMovies> call, Response<DiscoveredMovies> response) {
                DiscoveredMovies movies = response.body();
                List<Movie> moviesList = movies.getResults();
                Log.d("MovieListMovies", moviesList.toString());
            }

            @Override
            public void onFailure(Call<DiscoveredMovies> call, Throwable t) {

            }
        });
    }
}
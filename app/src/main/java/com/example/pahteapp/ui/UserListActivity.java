package com.example.pahteapp.ui;

import static com.example.pahteapp.ui.login.SESSION_ID;
import static com.example.pahteapp.ui.login.USER_INFO;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pahteapp.R;
import com.example.pahteapp.dataaccess.ApiClient;
import com.example.pahteapp.dataaccess.ApiInterface;
import com.example.pahteapp.domain.DiscoveredMovies;
import com.example.pahteapp.domain.Movie;
import com.example.pahteapp.domain.PaginatedUserList;
import com.example.pahteapp.domain.UserList;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {
    private final LinkedList<UserList> nUserList = new LinkedList<>();
    Integer page = 1;

    private RecyclerView mRecyclerView;
    private ListAdapter mAdapter;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        setAdapter();
        getAllLists();
    }

    private void getAllLists() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<PaginatedUserList> call = apiInterface.getAllListsUser(USER_INFO.getId(), "1e2c1f57cbed4d3e0c5dcad5996f2649", SESSION_ID);

        call.enqueue(new Callback<PaginatedUserList>() {
            @Override
            public void onResponse(Call<PaginatedUserList> call, Response<PaginatedUserList> response) {
                if (!response.isSuccessful()) return;
                PaginatedUserList userLists = response.body();
                nUserList.addAll(userLists.getResults());
                Log.d("UserList", nUserList.toString());
                mAdapter.setUserList(nUserList);
            }

            @Override
            public void onFailure(Call<PaginatedUserList> call, Throwable t) {
                Log.d("UserList", "failed userId: " + USER_INFO.getId() + " session: " + SESSION_ID + " " + t.toString());
            }
        });
    }

    private void setAdapter() {
        mRecyclerView = findViewById(R.id.listRecyclerview);
        mAdapter = new ListAdapter(this, nUserList);
        mRecyclerView.setAdapter(mAdapter);
        int gridColumnCount = 1;

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridColumnCount));
    }
}

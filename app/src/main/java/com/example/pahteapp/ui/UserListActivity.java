package com.example.pahteapp.ui;

import static com.example.pahteapp.ui.login.SESSION_ID;
import static com.example.pahteapp.ui.login.USER_INFO;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pahteapp.R;
import com.example.pahteapp.dataaccess.ApiClient;
import com.example.pahteapp.dataaccess.ApiInterface;
import com.example.pahteapp.domain.Authenticate;
import com.example.pahteapp.domain.CreateList;
import com.example.pahteapp.domain.Movie;
import com.example.pahteapp.domain.MovieList;
import com.example.pahteapp.domain.PaginatedUserList;
import com.example.pahteapp.domain.UserList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {
    private final List<UserList> nUserList = new ArrayList<>();
    private ParentListAdapter parentListAdapter;

    private FloatingActionButton mAddButton;
    private ConstraintLayout mAddFormWrap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("PatheApp");
        toolbar.inflateMenu(R.menu.main_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else if(item.getItemId() == R.id.logout) {
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                }

                return false;
            }
        });

        mAddButton = findViewById(R.id.addButton);
        mAddFormWrap = findViewById(R.id.AddFormWrap);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddFormWrap.setVisibility(View.VISIBLE);
                mAddButton.setVisibility(View.GONE);
            }
        });

        Button submitButton = findViewById(R.id.SubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editListName = findViewById(R.id.EditListName);
                addList(editListName.getText().toString());
            }
        });

        setAdapter();
        getAllLists();
    }


    private void addList(String name){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        UserList userList = new UserList();
        userList.setName(name);
        userList.setDescription("test");
        userList.setLanguage("nl");

        Call<Authenticate> call = apiInterface.createUserList(
                "application/json;charset=utf-8",
                "1e2c1f57cbed4d3e0c5dcad5996f2649",
                SESSION_ID,
                userList);

        call.enqueue(new Callback<Authenticate>() {
            @Override
            public void onResponse(Call<Authenticate> call, Response<Authenticate> response) {
                if (!response.isSuccessful()) {
                    Log.e("Error",SESSION_ID + response.toString());

                    return;
                }
                Authenticate addedUserList = response.body();
                Log.d("UserList","add successfull " + addedUserList.toString());
                getAllLists();
            }

            @Override
            public void onFailure(Call<Authenticate> call, Throwable t) {
                Log.e("UserList", t.toString());
            }
        });
    }

    private void editList(){

    }

    private void getAllLists() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<PaginatedUserList> call = apiInterface.getAllListsUser(USER_INFO.getId(), "1e2c1f57cbed4d3e0c5dcad5996f2649", SESSION_ID);

        call.enqueue(new Callback<PaginatedUserList>() {
            @Override
            public void onResponse(Call<PaginatedUserList> call, Response<PaginatedUserList> response) {
                if (!response.isSuccessful()) return;
                PaginatedUserList userLists = response.body();
                nUserList.clear();
                nUserList.addAll(userLists.getResults());
                getAllMoviesInLists();
            }

            @Override
            public void onFailure(Call<PaginatedUserList> call, Throwable t) {
                Log.d("UserList", "failed userId: " + USER_INFO.getId() + " session: " + SESSION_ID + " " + t.toString());
            }
        });
    }

    private void getAllMoviesInLists() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        for(UserList list : nUserList) {
            Call<MovieList> call = apiInterface.getList(list.getId(), "1e2c1f57cbed4d3e0c5dcad5996f2649");
            Log.i("LIST TEST", list.getId() + "");
            call.enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                    if(!response.isSuccessful()) return;
                    MovieList movieList = response.body();
                    List<Movie> allMovies = movieList.getItems();
                    for (UserList userList : nUserList) {
                        if (movieList.getId().equals(userList.getId())) {
                            userList.setMovies(allMovies);
                            parentListAdapter.setUserList(nUserList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {

                }
            });
        }
    }

    private void setAdapter() {
        RecyclerView ParentRecyclerViewItem = findViewById(R.id.listRecyclerview);

        // Initialise the Linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(UserListActivity.this);

        // Pass the arguments
        // to the parentItemAdapter.
        // These arguments are passed
        // using a method ParentItemList()
        parentListAdapter = new ParentListAdapter(this, nUserList);

        // Set the layout manager
        // and adapter for items
        // of the parent recyclerview
        ParentRecyclerViewItem.setAdapter(parentListAdapter);
        ParentRecyclerViewItem.setLayoutManager(layoutManager);
    }
}


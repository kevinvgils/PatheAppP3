package com.example.pahteapp.dataaccess;

import static com.example.pahteapp.ui.login.login.IS_GUEST;
import static com.example.pahteapp.ui.login.login.SESSION_ID;

import android.content.Context;
import android.widget.Toast;

import com.example.pahteapp.domain.login.Authenticate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Logout {

    public static void doLogout(Context applicationContext) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Authenticate> call = apiInterface.logout(SESSION_ID,"1e2c1f57cbed4d3e0c5dcad5996f2649");

        call.enqueue(new Callback<Authenticate>() {
            @Override
            public void onResponse(Call<Authenticate> call, Response<Authenticate> response) {
                Toast.makeText(applicationContext, "Successfully logged out", Toast.LENGTH_SHORT).show();
                IS_GUEST = false;
                SESSION_ID = null;
            }

            @Override
            public void onFailure(Call<Authenticate> call, Throwable t) {

            }
        });
    }
}

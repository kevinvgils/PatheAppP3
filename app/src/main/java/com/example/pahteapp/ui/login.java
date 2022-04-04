package com.example.pahteapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pahteapp.R;
import com.example.pahteapp.dataaccess.ApiClient;
import com.example.pahteapp.dataaccess.ApiInterface;
import com.example.pahteapp.domain.Authenticate;
import com.example.pahteapp.domain.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {
    public static String SESSION_ID = "";
    public static User USER_INFO = null;

    private TextView mGuest;
    private Button mLogin;
    private Authenticate requestToken = new Authenticate();
    private Authenticate requestTokenOnLogin = new Authenticate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getRequestToken();
        loginAsGuest();
        
    }

    // Haal de eerst de request token op die je gaat gebruiken om in te loggen
    private void getRequestToken() {
        Log.d("RequestToken", "Test");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<Authenticate> call = apiInterface.getRequestToken("1e2c1f57cbed4d3e0c5dcad5996f2649");

        call.enqueue(new Callback<Authenticate>() {
            @Override
            public void onResponse(Call<Authenticate> call, Response<Authenticate> response) {
                requestToken = response.body();
                Log.d("Request Token", requestToken.toString());
                // Nadat token is opgehaald initaliseer pas de login functie
                loginAsUser();
            }

            @Override
            public void onFailure(Call<Authenticate> call, Throwable t) {
                Log.d("RequestToken", "Something went wrong");
            }
        });
    }

    // Doe login om je request token the authoriseren
    private void loginAsUser() {
        Log.d("StartLogin", requestToken.toString());
        mLogin = findViewById(R.id.LoginButton);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userNameInput = findViewById(R.id.UserName);
                String userName = userNameInput.getText().toString();

                EditText passWordInput = findViewById(R.id.PassWord);
                String password = passWordInput.getText().toString();
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                Call<Authenticate> call = apiInterface.doLogin("1e2c1f57cbed4d3e0c5dcad5996f2649", userName, password, requestToken.getRequestToken());

                call.enqueue(new Callback<Authenticate>() {
                    @Override
                    public void onResponse(Call<Authenticate> call, Response<Authenticate> response) {
                        if(response.isSuccessful()) {
                            requestTokenOnLogin = response.body();
                            Log.d("LoginSuccess", response.body().toString());
                            getSessionId();
                        } else {
                            Log.d("LoginFailed", "failure " + response.headers());
                        }

                    }

                    @Override
                    public void onFailure(Call<Authenticate> call, Throwable t) {
                            Log.d("LoginFail", t.toString());
                    }
                });
            }
        });
    }

    // Haal sessie op met geauthoriseerde request token
    private void getSessionId() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<Authenticate> call = apiInterface.createSession("1e2c1f57cbed4d3e0c5dcad5996f2649", requestTokenOnLogin.getRequestToken());

        call.enqueue(new Callback<Authenticate>() {
            @Override
            public void onResponse(Call<Authenticate> call, Response<Authenticate> response) {
                if(response.isSuccessful()) {
                    Authenticate session = response.body();
                    SESSION_ID = session.getSessionId();
                    Log.d("SessionCreated", SESSION_ID);
                    getUserInfo();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("SessionFailed", "failure " + response.headers());
                }

            }

            @Override
            public void onFailure(Call<Authenticate> call, Throwable t) {
                Log.d("LoginFail", t.toString());
            }
        });
    }

    private void getUserInfo() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<User> call = apiInterface.getUser("1e2c1f57cbed4d3e0c5dcad5996f2649", SESSION_ID);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                USER_INFO = user;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    // Start guest sessie
    private void loginAsGuest() {
        mGuest = findViewById(R.id.GuestLink);

        mGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                Call<Authenticate> call = apiInterface.createGuestSession("1e2c1f57cbed4d3e0c5dcad5996f2649");

                call.enqueue(new Callback<Authenticate>() {
                    @Override
                    public void onResponse(Call<Authenticate> call, Response<Authenticate> response) {
                        if(response.isSuccessful()) {
                            Authenticate guestSession = response.body();
                            SESSION_ID = guestSession.getGuestSessionId();
                            Log.d("SessionCreated", SESSION_ID);
                            Toast.makeText(getApplicationContext(), "Successfully logged in as guest!", Toast.LENGTH_SHORT).show();
                            Log.d("LOGIN", "Signed in as guest");
                            Intent intent = new Intent(view.getContext(), MainActivity.class);
                            view.getContext().startActivity(intent);
                        } else {
                            Log.d("SessionFailed", "failure " + response.headers());
                        }

                    }

                    @Override
                    public void onFailure(Call<Authenticate> call, Throwable t) {
                        Log.d("LoginFail", t.toString());
                    }
                });
            }
        });
    }


}
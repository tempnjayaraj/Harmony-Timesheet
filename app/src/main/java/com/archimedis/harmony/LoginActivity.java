package com.archimedis.harmony;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity  {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        Button fillCred = findViewById(R.id.button3);
        fillCred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String username =preferences.getString("username", "");
                String password = preferences.getString("password", "");
                System.out.println(username+password);
                EditText username_ = findViewById(R.id.editText2);
                EditText password_ = findViewById(R.id.editTextTextPassword);
                username_.setText(username);
                password_.setText(password);
            }
        });
        Button login = findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){


                EditText username_ = findViewById(R.id.editText2);
                EditText password_ = findViewById(R.id.editTextTextPassword);
                String username = username_.getText().toString();
                String password = password_.getText().toString();
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                JSONObject obj = new JSONObject();
                obj.put("usr", username);
                obj.put("pwd", password);
                RequestBody body = RequestBody.create(mediaType, obj.toJSONString());
                Request request = new Request.Builder()
                        .url("http://115.97.255.108/api/method/login")
                        .method("POST", body)
                        .addHeader("Authorization", "token 3cc00f4eaf15af9:2f74e94e2f285ca")
                        .addHeader("Content-Type", "application/json")
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException{
                        if (response.isSuccessful()) {
                            JSONParser parser = new JSONParser();
                            try {
                                JSONObject result = (JSONObject) parser.parse(response.body().string());
                                String userName = (String) result.get("full_name");
                                System.out.println(result.toJSONString());
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent projectPage = new Intent(LoginActivity.this,ProjectActivity.class);
                                        projectPage.putExtra("fullName",userName);
                                        projectPage.putExtra("username",username);
                                        startActivity(projectPage);
                                    }
                                });
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
            }else{
                    showPopup("அட, இணைய இணைப்பு இல்லைப்பா !");
                }
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void showPopup(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

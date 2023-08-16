package com.archimedis.harmony;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SettingsDialogFragment.SettingsDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView4);
        ImageView settingsButton = findViewById(R.id.imageView7);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsDialog();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Load the RecyclerView layout
                Intent loginPage = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(loginPage);
            }
        });

        ImageView getTimeSheetbtn = findViewById(R.id.seeTimesheetBtn);
        getTimeSheetbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Load the RecyclerView layout
                Intent loginPage = new Intent(MainActivity.this,GetTimesheetsActivity.class);
                startActivity(loginPage);
            }
        });
    }
    private void showPopup(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSettingsChanged(String variable1, String variable2, String variable3, String variable4) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("appurl", variable1);
        editor.putString("username", variable2);
        editor.putString("password", variable3);
        editor.putString("apitoken", variable4);
        editor.apply();
    }
    private void showSettingsDialog() {
        SettingsDialogFragment dialog = new SettingsDialogFragment();
        dialog.show(getSupportFragmentManager(), "SettingsDialog");
    }
}
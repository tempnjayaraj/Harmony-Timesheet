package com.archimedis.harmony;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SuccessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String timesheetName_ = extras.getString("timesheetName");
        String projectName = extras.getString("projectName");
        String fullName = extras.getString("fullName");
        setContentView(R.layout.successpage);
        TextView heading = findViewById(R.id.header);
        TextView timesheetName = findViewById(R.id.timesheet);
        TextView status = findViewById(R.id.status);
        heading.setText("Great "+fullName+"!,\n Timesheet creation success");
        timesheetName.setText(timesheetName_);
        status.setText("You have created TimeSheet for "+projectName);
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent loginPage = new Intent(SuccessActivity.this,GetTimesheetsActivity.class);
                startActivity(loginPage);
            }
        });
    }
}
package com.archimedis.harmony;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Timer;

public class FinalActivity extends AppCompatActivity {
    ArrayList<TImeLog> tImeLogs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras =  getIntent().getExtras();
        tImeLogs = (ArrayList<TImeLog>) extras.get("data");
        String projectName = extras.getString("projectName");
        String projectID = extras.getString("projectID");
        String userName = extras.getString("username");
        String fullName = extras.getString("fullName");
        setContentView(R.layout.finalpage);

        TextView heading = findViewById(R.id.textView);
        heading.setText("Review your Timesheet");
        TextView username = findViewById(R.id.textView2);
        username.setText(fullName);
        TextView projectField = findViewById(R.id.textView3);
        projectField.setText(projectName);
        ListView listView = findViewById(R.id.listView);
        for (int i=0;i<tImeLogs.size();i++){
            System.out.println("->"+tImeLogs.get(i).toString());
        }
        FinalActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ReviewAdapter itemAdapter = new ReviewAdapter(FinalActivity.this, tImeLogs);
                listView.setAdapter(itemAdapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                Button submit = findViewById(R.id.button2);
                submit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        showPopup("I am going to submit");
                    }
                });
            }
        });
    }
    private void showPopup(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        tImeLogs.clear(); // Clear the list of selected TImeLogs
        super.onBackPressed();
    }
}

package com.archimedis.harmony;

import android.content.Intent;
import android.os.Bundle;
import android.sax.Element;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivityActivity extends AppCompatActivity {

ArrayList<TImeLog> timeLogs = new ArrayList<>();
ArrayList<TImeLog> finalLogs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityselector);
        Bundle fromPrevious = getIntent().getExtras();
        String project = fromPrevious.getString("projectName");
        String projectName = fromPrevious.getString("projectID");
        String userName = fromPrevious.getString("username");
        String fullName = fromPrevious.getString("fullName");
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("txt", "")
                .addFormDataPart("doctype", "Project")
                .addFormDataPart("ignore_user_permissions", "0")
                .addFormDataPart("reference_doctype", "Timesheet")
                .addFormDataPart("query", "ecapsule_hrms.overrides.override_timesheet.activity_type_query")
                .addFormDataPart("filters", "{\"project\":\"" + projectName + "\"}")
                .build();
        Request request = new Request.Builder()
                .url("http://115.97.255.108/api/method/frappe.desk.search.search_link")
                .method("POST", body)
                .addHeader("Authorization", "token 3cc00f4eaf15af9:2f74e94e2f285ca")
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
                        TextView username = findViewById(R.id.textView);
                        ListView listView = findViewById(R.id.listView);
                        List<String> activities = new ArrayList<>();
                        JSONObject result = (JSONObject) parser.parse(response.body().string());
                        JSONArray activityList = (JSONArray) result.get("results");
                        for (int i = 0; i < activityList.size(); i++) {
                            JSONObject activityObject = (JSONObject) activityList.get(i);
                            String activity = (String) activityObject.get("value");
                            activities.add(activity);
                        }
                        ActivityActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                for(int i=0;i<activities.size();i++){
                                    TImeLog temp = new TImeLog(false,activities.get(i),"","");
                                    timeLogs.add(temp);
                                }
                                CustomAdapter itemAdapter = new CustomAdapter(ActivityActivity.this,  timeLogs);
                                listView.setAdapter(itemAdapter);
                                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                username.setText("What have you done today in "+project+"?");
                                Button reviewButton = findViewById(R.id.button2);

                                System.out.println(finalLogs);
                                reviewButton.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Intent reviewFinal = new Intent(ActivityActivity.this,FinalActivity.class);
                                        for(int k=0;k<timeLogs.size();k++){
                                            TImeLog temp = timeLogs.get(k);
                                            if (temp.isChecked && !finalLogs.contains(temp)) {
                                                finalLogs.add(temp);
                                            }
                                        }
                                        reviewFinal.putExtra("data",finalLogs);
                                        reviewFinal.putExtra("fullName",fullName);
                                        reviewFinal.putExtra("username",userName);
                                        reviewFinal.putExtra("projectName",project);
                                        reviewFinal.putExtra("projectID",projectName);
                                        startActivity(reviewFinal);
                                    }
                                });


                            }
                        });
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    public void onOptionSelected(int position, boolean isChecked) {
        timeLogs.get(position).setChecked(isChecked);
    }



}

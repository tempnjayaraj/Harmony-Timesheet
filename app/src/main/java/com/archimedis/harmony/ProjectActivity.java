package com.archimedis.harmony;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProjectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projectselector);
        Bundle fromPrevious = getIntent().getExtras();
        String userEmail = fromPrevious.getString("username");
        String fullName = fromPrevious.getString("fullName");

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("txt", "")
                .addFormDataPart("doctype", "Project")
                .addFormDataPart("ignore_user_permissions", "0")
                .addFormDataPart("reference_doctype", "Timesheet")
                .addFormDataPart("query", "ecapsule_hrms.overrides.override_timesheet.project_query")
                .addFormDataPart("filters", "{\"date\":\"2023-08-03\",\"user\":\"" + userEmail + "\"}")
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
                        int[] images = {R.drawable.p1};
                        List<String> descriptions = new ArrayList<>();
                        List<String> projectIDs = new ArrayList<>();
                        JSONObject result = (JSONObject) parser.parse(response.body().string());
                        JSONArray projectList = (JSONArray) result.get("results");
                        for (int i = 0; i < projectList.size(); i++) {
                            JSONObject project = (JSONObject) projectList.get(i);
                            String projectID = (String) project.get("value");
                            descriptions.add(projectID);
                            String projectName = (String) project.get("description");
                            projectIDs.add(projectName);
                        }
                        ProjectActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ItemAdapter itemAdapter = new ItemAdapter(ProjectActivity.this, projectIDs, descriptions);
                                listView.setAdapter(itemAdapter);
                                username.setText("Welcome, "+fullName);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent activityPage = new Intent(ProjectActivity.this,ActivityActivity.class);
                                        activityPage.putExtra("projectID",descriptions.get(position));
                                        activityPage.putExtra("projectName",projectIDs.get(position));
                                        activityPage.putExtra("fullName",fullName);
                                        activityPage.putExtra("username",userEmail);
                                        startActivity(activityPage);
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
}

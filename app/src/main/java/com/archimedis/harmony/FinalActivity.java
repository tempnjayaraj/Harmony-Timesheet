package com.archimedis.harmony;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FinalActivity extends AppCompatActivity {
    ArrayList<TImeLog> tImeLogs;
    private Calendar selectedCalendar;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        Bundle extras =  getIntent().getExtras();
        tImeLogs = (ArrayList<TImeLog>) extras.get("data");
        String projectName = extras.getString("projectName");
        String projectID = extras.getString("projectID");
        String userName = extras.getString("username");
        String fullName = extras.getString("fullName");
        setContentView(R.layout.finalpage);

        TextView heading = findViewById(R.id.header);
        heading.setText("Review your Timesheet");
        TextView username = findViewById(R.id.timesheet);
        username.setText(fullName);
        TextView projectField = findViewById(R.id.status);
        projectField.setText(projectName);
        ListView listView = findViewById(R.id.listView);
        CheckBox wfh = findViewById(R.id.wfh);
        CheckBox halfday = findViewById(R.id.halfday);
        FinalActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ReviewAdapter itemAdapter = new ReviewAdapter(FinalActivity.this, tImeLogs);
                listView.setAdapter(itemAdapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                Button submit = findViewById(R.id.button2);
                selectedCalendar = Calendar.getInstance();
                Button datebtn = findViewById(R.id.datebtn);
                datebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePickerDialog();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)  {
                        LocalDateTime ldt = LocalDateTime.now();
//                        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
//                                .format(ldt);
                        JSONObject timesheet = HarmonyUtil.newTimesheetJSON(fullName,
                                date, projectID, wfh.isChecked(),
                                halfday.isChecked());
                        JSONArray timeLogArray = new JSONArray();
                        for (int i=0;i<tImeLogs.size();i++){
                            TImeLog tobj = tImeLogs.get(i);
                                JSONObject temp = HarmonyUtil.newTimeLog(tobj.getActivity(),tobj.getComment(),new
                                        Float(tobj.getTime()), projectID);
                                timeLogArray.add(temp);
                        }

                        timesheet.put("time_logs", timeLogArray);
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, timesheet.toJSONString());
                        Request request = new Request.Builder()
                                .url("http://115.97.255.108/api/resource/Timesheet")
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
                                        JSONObject data = (JSONObject)result.get("data");
                                        Intent activityPage = new Intent(FinalActivity.this,SuccessActivity.class);
                                        activityPage.putExtra("timesheetName",(String) data.get("name"));
                                        activityPage.putExtra("projectName",projectName);
                                        activityPage.putExtra("fullName",fullName);

                                        startActivity(activityPage);
                                      System.out.println(result.toJSONString());

                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        });
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
    public static JSONObject newTimesheetJSON(String employeName, String date, String projectID, Boolean isWorkFromHome,
                                              Boolean isHalfDay) throws IOException, ParseException {
        JSONObject result = new JSONObject();
        int wfh = 0;
        if (isWorkFromHome) {
            wfh = 1;
        }
        int halfDay = 0;
        if (isHalfDay) {
            halfDay = 1;
        }
        result.put("employee", employeName);
        result.put("title", employeName);
        result.put("employee_name", employeName);
        result.put("date", date);
        result.put("parent_project", projectID);
        result.put("work_from_home", wfh);
        result.put("half_day", halfDay);
        result.put("doctype", "Timesheet");
        result.put("workflow_state","Submitted");
        return result;
    }
    public static JSONObject newTimeLog(String activityName, String comment, Float hours, String projectID) throws IOException, ParseException{
        JSONObject result = new JSONObject();
        result.put("activity_type", activityName);
        result.put("comments", comment);
        result.put("hours", hours);
        result.put("project", projectID);
        return result;
    }
    private void showDatePickerDialog() {
        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH);
        int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                FinalActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Update the selectedCalendar with the chosen date
                        selectedCalendar.set(year, monthOfYear, dayOfMonth);

                        // Update the UI to show the selected date
                        updateDateUI();
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }
    private void updateDateUI() {
        // Format the selected date and display it in the editTextDate
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        date = sdf.format(selectedCalendar.getTime());
        TextView dateText = findViewById(R.id.date);
        dateText.setText(date);
    }
}

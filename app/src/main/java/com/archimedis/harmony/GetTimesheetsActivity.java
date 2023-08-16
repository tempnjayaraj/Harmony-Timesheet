package com.archimedis.harmony;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetTimesheetsActivity extends AppCompatActivity {
    ArrayList<TimeSheet> timesheetList = new ArrayList<TimeSheet>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seetimesheets);
        TextView header = findViewById(R.id.headerText);
        header.setText("Please have a look at your timesheets");
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://115.97.255.108/api/resource/Timesheet")
                .method("GET", null)
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
                        JSONObject result = (JSONObject) parser.parse(response.body().string());
                        JSONArray timesheets = (JSONArray) result.get("data");

                        for(int i=0;i<timesheets.size();i++){
                            JSONObject timesheetObj = (JSONObject) timesheets.get(i);

                            String timesheetID = (String) timesheetObj.get("name");
                            JSONObject timesheetCurrObj = HarmonyUtil.getSingleTimeSheet(timesheetID);
                            JSONObject timesheetCurrObjData = (JSONObject) timesheetCurrObj.get("data");
                            ArrayList<TImeLog> timeLogsData = new ArrayList<TImeLog>();
                            JSONArray timeLogs = (JSONArray) timesheetCurrObjData.get("time_logs");

                            for(int k=0;k<timeLogs.size();k++){
                                JSONObject timeLog = (JSONObject) timeLogs.get(k);
                                String activityType = (String) timeLog.get("activity_type");
                                String comments = (String) timeLog.get("comments");
                                Double hours = (Double) timeLog.get("hours");
                                TImeLog temp = new TImeLog(false,activityType,comments,String.valueOf(hours));
                                temp.setTsname(timesheetID);
                                timeLogsData.add(temp);
                            }
                            System.out.println(timesheetCurrObjData.toJSONString());
                            long halfDayRaw = (long)  timesheetCurrObjData.get("half_day");
                            long wfhRaw = (long)  timesheetCurrObjData.get("work_from_home");
                            Boolean TShalfDay=false;
                            Boolean TSwfh=false;
                            if(halfDayRaw==1){
                                TShalfDay = true;
                            }
                            if(wfhRaw ==1){
                                TSwfh = true;
                            }
                            String TSname = (String) timesheetCurrObjData.get("name");
                            String TSstatus = (String) timesheetCurrObjData.get("workflow_state");
                            String TSDate = (String) timesheetCurrObjData.get("date");
                            String TSprojectName = (String) timesheetCurrObjData.get("project_name");
                            Double TShours = (Double) timesheetCurrObjData.get("total_hours");
                            String employeeName = (String) timesheetCurrObjData.get("title");
                            TimeSheet temp = new TimeSheet(TSname,TSstatus,TSDate,TSprojectName,String.valueOf(TShours),timeLogsData, TSwfh,TShalfDay, employeeName,timesheetCurrObjData);
                            timesheetList.add(temp);
                        }
                        GetTimesheetsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ListView listView = findViewById(R.id.listView);
                                Collections.reverse(timesheetList);
                                TimesheetAdapter adapter = new TimesheetAdapter(GetTimesheetsActivity.this,timesheetList);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        // Get the selected TimeSheet item
                                        TimeSheet selectedTimeSheet = timesheetList.get(position);

                                        // Inflate the dialog layout
                                        LayoutInflater inflater = LayoutInflater.from(GetTimesheetsActivity.this);
                                        View dialogView = inflater.inflate(R.layout.timesheetmodal, null);
                                        TextView title = dialogView.findViewById(R.id.textViewTitle);
                                        TextView status = dialogView.findViewById(R.id.textViewStatus);
                                        TextView employee = dialogView.findViewById(R.id.textViewEmp);
                                        TextView date = dialogView.findViewById(R.id.textViewDate);
                                        TextView project = dialogView.findViewById(R.id.textViewProject);
                                        TextView workFromHome = dialogView.findViewById(R.id.textViewWorkFromHome);
                                        TextView halfDay = dialogView.findViewById(R.id.textViewHalfDay);
                                        employee.setText(selectedTimeSheet.getEmployeeName());
                                        title.setText(selectedTimeSheet.getName());
                                        status.setText(selectedTimeSheet.getWorkflowState());
                                        LocalDate dateRaw = LocalDate.parse(selectedTimeSheet.getDate());
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, ''yy");
                                        String formattedDate = dateRaw.format(formatter);
                                        date.setText(formattedDate);
                                        project.setText(selectedTimeSheet.getProjectName());
                                        if(selectedTimeSheet.wfh){
                                            workFromHome.setText("Work From Home");
                                        }else{
                                            workFromHome.setText("Work From Office");
                                        }
                                        if(selectedTimeSheet.halfday){
                                            halfDay.setText("Half Day");
                                        }else{
                                            halfDay.setText("Full Day");
                                        }

                                        ReviewAdapter adapter = new ReviewAdapter(GetTimesheetsActivity.this,selectedTimeSheet.getTimeLogs());
                                        // Set up the ListView inside the dialog
                                        ListView dialogListView = dialogView.findViewById(R.id.listView);
                                        dialogListView.setAdapter(adapter);

                                        // Create the dialog
                                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GetTimesheetsActivity.this);
                                        dialogBuilder.setView(dialogView);

                                        if (selectedTimeSheet.getWorkflowState().equals("Draft")) {
                                            dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    JSONObject data = selectedTimeSheet.data;
                                                    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                                            .addFormDataPart("doc",data.toJSONString())
                                                            .addFormDataPart("action","Submit")
                                                            .build();
                                                    Request request = new Request.Builder()
                                                            .url("http://115.97.255.108/api/method/frappe.model.workflow.apply_workflow")
                                                            .method("POST", body)
                                                            .addHeader("Authorization", "token 3cc00f4eaf15af9:2f74e94e2f285ca")
                                                            .build();
                                                    OkHttpClient client = new OkHttpClient().newBuilder()
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
                                                                    System.out.println(result.toJSONString());
                                                                    GetTimesheetsActivity.this.runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            recreate();
                                                                        }
                                                                    });
                                                                } catch (ParseException e) {
                                                                    throw new RuntimeException(e);
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                            dialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    OkHttpClient client = new OkHttpClient().newBuilder()
                                                            .build();
                                                    MediaType mediaType = MediaType.parse("text/plain");
                                                    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                                            .addFormDataPart("doctype","Timesheet")
                                                            .addFormDataPart("name", selectedTimeSheet.getName())
                                                            .build();
                                                    Request request = new Request.Builder()
                                                            .url("http://115.97.255.108/api/method/frappe.client.delete")
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
                                                                GetTimesheetsActivity.this.runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        recreate();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        } else if(selectedTimeSheet.getWorkflowState().equals("Pending Approval")){
                                            dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    OkHttpClient client = new OkHttpClient().newBuilder()
                                                            .build();
                                                    MediaType mediaType = MediaType.parse("text/plain");
                                                    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                                            .addFormDataPart("doctype","Timesheet")
                                                            .addFormDataPart("name", selectedTimeSheet.getName())
                                                            .build();
                                                    Request request = new Request.Builder()
                                                            .url("http://115.97.255.108/api/method/frappe.client.delete")
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
                                                                GetTimesheetsActivity.this.runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        recreate();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                        AlertDialog dialog = dialogBuilder.create();
                                        dialog.show();
                                    }
                                });
                                listView.setAdapter(adapter);
                            }
                        });
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Spinner spinnerActivityType = findViewById(R.id.spinnerActivityType);
        spinnerActivityType.setVisibility(View.GONE);
        Button thisMonthBtn = findViewById(R.id.thisMonth);
        thisMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerActivityType.setVisibility(View.GONE);
                ListView listView = findViewById(R.id.listView);
                TimesheetAdapter adapter = new TimesheetAdapter(GetTimesheetsActivity.this,filterMonth(timesheetList));
                listView.setAdapter(adapter);
            }
        });
        Button allButton = findViewById(R.id.all);
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerActivityType.setVisibility(View.GONE);
                ListView listView = findViewById(R.id.listView);
                TimesheetAdapter adapter = new TimesheetAdapter(GetTimesheetsActivity.this,timesheetList);
                listView.setAdapter(adapter);
            }
        });
        Button byProjectButton = findViewById(R.id.byProject);
        byProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> projects = new ArrayList<String>();
                for(int i=0;i<timesheetList.size();i++){
                    TimeSheet temp = timesheetList.get(i);
                    String projectName = temp.getProjectName();
                    if(!projects.contains(projectName)){
                        projects.add(projectName);
                    }
                }
                // Define the list of activity types
                String[] activityTypes = projects.toArray(new String[0]);
                String newString = "-----Select a Project-----";

// Create a new array with a larger size
                String[] newArray = new String[activityTypes.length + 1];

// Add the new string as the first element
                newArray[0] = newString;

// Copy the remaining elements from the original array to the new array
                System.arraycopy(activityTypes, 0, newArray, 1, activityTypes.length);


                // Create an ArrayAdapter using a default spinner layout and the list of activity types
                ArrayAdapter<String> adapter = new ArrayAdapter<>(GetTimesheetsActivity.this,android.R.layout.simple_spinner_item, newArray);

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                spinnerActivityType.setAdapter(adapter);
                spinnerActivityType.setVisibility(View.VISIBLE);

                // Set a listener to handle the user's selection
                spinnerActivityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Retrieve the selected activity type
                        String selectedActivityType = (String) parent.getItemAtPosition(position);
                        ArrayList<TimeSheet> result = new ArrayList<TimeSheet>();
                        for(int i=0;i<timesheetList.size();i++){
                            TimeSheet temp = timesheetList.get(i);
                            if(selectedActivityType.equals(temp.getProjectName())){
                                result.add(temp);
                            }
                        }
                        ListView listView = findViewById(R.id.listView);
                        TimesheetAdapter adapter = new TimesheetAdapter(GetTimesheetsActivity.this,result);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle case where no selection is made
                    }
                });
            }
        });





    }
    private ArrayList<TimeSheet> filterMonth(ArrayList<TimeSheet> list) {
      ArrayList<TimeSheet> result = new ArrayList<TimeSheet>();
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        List<String> datesList = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                String formattedDate = date.format(dateFormatter);
                datesList.add(formattedDate);
            }
        }
        for(int i=0;i<list.size();i++){
          TimeSheet temp = list.get(i);
          if(datesList.contains(temp.getDate())){
              result.add(temp);
          }
      }
        return result;
    }
}

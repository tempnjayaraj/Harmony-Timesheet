package com.archimedis.harmony;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HarmonyUtil {


    public static String getUserName(String username, String password) throws IOException, ParseException {
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
        Response response = client.newCall(request).execute();
        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(response.body().string());
        return (String) result.get("full_name");
    }

    public static JSONObject newTimesheetJSON(String employeName, String date, String projectID, Boolean isWorkFromHome,
                                              Boolean isHalfDay) {
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
        return result;
    }

    public static JSONObject newTimeLog(String activityName, String comment, Float hours, String projectID) {
        JSONObject result = new JSONObject();
        result.put("activity_type", activityName);
        result.put("comments", comment);
        result.put("hours", hours);
        result.put("project", projectID);
        return result;
    }

    public static JSONObject createTimeSheet(JSONObject data) throws IOException, ParseException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, data.toJSONString());
        Request request = new Request.Builder()
                .url("http://115.97.255.108/api/resource/Timesheet")
                .method("POST", body)
                .addHeader("Authorization", "token 3cc00f4eaf15af9:2f74e94e2f285ca")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONParser parser = new JSONParser();
        // System.out.println(response.body().string());
        return (JSONObject) parser.parse(response.body().string());
    }

    public static JSONObject getSingleTimeSheet(String timesheetName) throws IOException, ParseException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://115.97.255.108/api/resource/Timesheet/" + timesheetName + "")
                .method("GET", null)
                .addHeader("Authorization", "token 3cc00f4eaf15af9:2f74e94e2f285ca")
                .build();
        Response response = client.newCall(request).execute();
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(response.body().string());
    }

    public static JSONObject getAllTimeSheets() throws IOException, ParseException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://115.97.255.108/api/resource/Timesheet")
                .method("GET", null)
                .addHeader("Authorization", "token 3cc00f4eaf15af9:2f74e94e2f285ca")
                .build();
        Response response = client.newCall(request).execute();
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(response.body().string());
    }

    public static JSONObject getAllocatedProjects(String useremail) throws IOException, ParseException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("txt", "")
                .addFormDataPart("doctype", "Project")
                .addFormDataPart("ignore_user_permissions", "0")
                .addFormDataPart("reference_doctype", "Timesheet")
                .addFormDataPart("query", "ecapsule_hrms.overrides.override_timesheet.project_query")
                .addFormDataPart("filters", "{\"date\":\"2023-08-03\",\"user\":\"" + useremail + "\"}")
                .build();
        Request request = new Request.Builder()
                .url("http://115.97.255.108/api/method/frappe.desk.search.search_link")
                .method("POST", body)
                .addHeader("Authorization", "token 3cc00f4eaf15af9:2f74e94e2f285ca")
                .build();
        Response response = client.newCall(request).execute();
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(response.body().string());
    }

    public static JSONObject getActivitiesOfProject(String projectID) throws IOException, ParseException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("txt", "")
                .addFormDataPart("doctype", "Project")
                .addFormDataPart("ignore_user_permissions", "0")
                .addFormDataPart("reference_doctype", "Timesheet")
                .addFormDataPart("query", "ecapsule_hrms.overrides.override_timesheet.activity_type_query")
                .addFormDataPart("filters", "{\"project\":\"" + projectID + "\"}")
                .build();
        Request request = new Request.Builder()
                .url("http://115.97.255.108/api/method/frappe.desk.search.search_link")
                .method("POST", body)
                .addHeader("Authorization", "token 3cc00f4eaf15af9:2f74e94e2f285ca")
                .build();
        Response response = client.newCall(request).execute();
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(response.body().string());
    }
}

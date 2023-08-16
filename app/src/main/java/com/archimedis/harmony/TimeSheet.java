package com.archimedis.harmony;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class TimeSheet {
    String name;
    String workflowState;
    String date;
    String projectName;
    String hours;
String employeeName;
    Boolean wfh;
    Boolean halfday;
    ArrayList<TImeLog> timeLogs;

    JSONObject data;
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Boolean getWfh() {
        return wfh;
    }

    public void setWfh(Boolean wfh) {
        this.wfh = wfh;
    }

    public Boolean getHalfday() {
        return halfday;
    }

    public void setHalfday(Boolean halfday) {
        this.halfday = halfday;
    }

    public ArrayList<TImeLog> getTimeLogs() {
        return timeLogs;
    }

    public void setTimeLogs(ArrayList<TImeLog> timeLogs) {
        this.timeLogs = timeLogs;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkflowState() {
        return workflowState;
    }

    public void setWorkflowState(String workflowState) {
        this.workflowState = workflowState;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public TimeSheet(String name, String workflowState, String date, String projectName, String hours, ArrayList<TImeLog> timeLogs, Boolean wfh, Boolean halfday, String empName, JSONObject data) {
        this.name = name;
        this.workflowState = workflowState;
        this.date = date;
        this.projectName = projectName;
        this.hours = hours;
        this.timeLogs = timeLogs;
        this.wfh = wfh;
        this.halfday = halfday;
        this.employeeName = empName;
        this.data = data;
    }
    public TimeSheet(String name, String workflowState, String date, String projectName, String hours, ArrayList<TImeLog> timeLogs) {
        this.name = name;
        this.workflowState = workflowState;
        this.date = date;
        this.projectName = projectName;
        this.hours = hours;
        this.timeLogs = timeLogs;
    }
    public TimeSheet(){

    }
}

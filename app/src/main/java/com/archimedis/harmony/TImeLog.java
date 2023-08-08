package com.archimedis.harmony;

import java.io.Serializable;

public class TImeLog implements Serializable {
    Boolean isChecked;
    String activity;
    String comment;
    String time;

    public TImeLog(Boolean isChecked, String activity, String comment, String time) {
        this.isChecked = isChecked;
        this.activity = activity;
        this.comment = comment;
        this.time = time;
    }
    public TImeLog(){

    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TImeLog{" +
                "isChecked=" + isChecked +
                ", activity='" + activity + '\'' +
                ", comment='" + comment + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}

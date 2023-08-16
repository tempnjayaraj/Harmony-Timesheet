package com.archimedis.harmony;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TimesheetAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TimeSheet> timeSheets;

    public TimesheetAdapter(Context context,ArrayList<TimeSheet> timeSheets){
        this.context = context;
        this.timeSheets = timeSheets;
    }
    @Override
    public int getCount() {
        return timeSheets.size();
    }

    @Override
    public Object getItem(int position) {
        return timeSheets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TimesheetAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.timesheetlist, parent, false);
            viewHolder = new TimesheetAdapter.ViewHolder();
            viewHolder.constraintLayout = convertView.findViewById(R.id.layoutTimeLog);
            viewHolder.status = convertView.findViewById(R.id.TSstatus);
//            viewHolder.name = convertView.findViewById(R.id.TSname);
            viewHolder.project = convertView.findViewById(R.id.TSProject);
            viewHolder.date = convertView.findViewById(R.id.TSdate);
//            viewHolder.hours = convertView.findViewById(R.id.TSHours);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TimesheetAdapter.ViewHolder) convertView.getTag();
        }


        TimeSheet currentItem = timeSheets.get(position);
        if(currentItem.getWorkflowState().equals("Approved")){
            String bg = "#E5F6DF"; // Replace this with your hex color value
            int lightGreen = Color.parseColor(bg);
            viewHolder.constraintLayout.setBackgroundColor(lightGreen);
            String fg = "#008631"; // Replace this with your hex color value
            int darkGreen = Color.parseColor(fg);
            viewHolder.status.setTextColor(darkGreen);
        }else if(currentItem.getWorkflowState().equals("Pending Approval")){
            String bg = "#BCD2E8"; // Replace this with your hex color value
            int lightGreen = Color.parseColor(bg);
            viewHolder.constraintLayout.setBackgroundColor(lightGreen);
            String fg = "#1E3F66"; // Replace this with your hex color value
            int darkGreen = Color.parseColor(fg);
            viewHolder.status.setTextColor(darkGreen);
        }else{
            String bg = "#FFD8B2"; // Replace this with your hex color value
            int lightGreen = Color.parseColor(bg);
            viewHolder.constraintLayout.setBackgroundColor(lightGreen);
            String fg = "#FE8D01"; // Replace this with your hex color value
            int darkGreen = Color.parseColor(fg);
            viewHolder.status.setTextColor(darkGreen);
        }

        viewHolder.status.setText(currentItem.getWorkflowState());
        LocalDate dateRaw = LocalDate.parse(currentItem.getDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, ''yy");
        String formattedDate = dateRaw.format(formatter);
        viewHolder.date.setText(formattedDate);
        viewHolder.project.setText(currentItem.getProjectName());
//        viewHolder.hours.setText(currentItem.getHours());

        return convertView;
    }

    private static class ViewHolder {
      TextView status;
//      TextView name;
      TextView date;
//      TextView hours;
      TextView project;
        ConstraintLayout constraintLayout;
    }
}

package com.archimedis.harmony;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TimeLogAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TImeLog> tImeLogs;
    public TimeLogAdapter(Context context, ArrayList<TImeLog> tImeLogs) {
        this.context = context;
        this.tImeLogs = tImeLogs;
    }

    public TimeLogAdapter(){

    }

    @Override
    public int getCount() {
        return tImeLogs.size(); // Return the number of items in the ArrayList
    }

    @Override
    public Object getItem(int position) {
        return tImeLogs.get(position); // Return the TimeLog object at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position; // Return the position as the ID of the item (not used in this case)
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TimeLogAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.reviewactivity, parent, false);
            viewHolder = new TimeLogAdapter.ViewHolder();
            viewHolder.activity = convertView.findViewById(R.id.textViewActivity);
            viewHolder.comment = convertView.findViewById(R.id.textViewComment);
            viewHolder.time = convertView.findViewById(R.id.textViewFor);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TimeLogAdapter.ViewHolder) convertView.getTag();
        }

        TImeLog currentItem = tImeLogs.get(position);
        viewHolder.activity.setText(currentItem.getActivity());
        viewHolder.comment.setText(currentItem.getComment());
        viewHolder.time.setText(currentItem.getTime()+" hours");


        return convertView;
    }

    private static class ViewHolder {
        TextView activity;
        TextView comment;
        TextView time;

    }
}

package com.archimedis.harmony;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> projectIDs;

    private List<String> descriptions;

    private int[] pictures;

    public ItemAdapter(Context context, List<String> projectIDs, List<String> descriptions) {
        super(context, R.layout.item_list, projectIDs);
        this.context = context;
        this.projectIDs = projectIDs;
        this.descriptions = descriptions;
//        this.pictures = pictures;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewTitle = convertView.findViewById(R.id.textViewTitle);
            viewHolder.textViewDescription = convertView.findViewById(R.id.textViewDescription);
//            viewHolder.imageViewPicture = convertView.findViewById(R.id.imageViewPicture);
            // Initialize other views here if needed
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String projectID = projectIDs.get(position);
        viewHolder.textViewTitle.setText(projectID);
        String description = descriptions.get(position);
        viewHolder.textViewDescription.setText(description);
//        viewHolder.imageViewPicture.setImageResource(pictures[position]);

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
//        ImageView imageViewPicture;

    }
}

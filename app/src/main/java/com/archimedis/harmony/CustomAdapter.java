package com.archimedis.harmony;

import android.content.Context;
import android.sax.Element;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TImeLog> tImeLogs;

    public CustomAdapter(Context context, ArrayList<TImeLog> tImeLogs) {
        this.context = context;
        this.tImeLogs = tImeLogs;
    }

    @Override
    public int getCount() {
        return tImeLogs.size();
    }

    @Override
    public Object getItem(int position) {
        return tImeLogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position; // Return the item's position as the ID
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activitylist, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = convertView.findViewById(R.id.checkBoxItem);
            viewHolder.textView = convertView.findViewById(R.id.textViewItem);
            viewHolder.layoutAdditionalInputs = convertView.findViewById(R.id.layoutAdditionalInputs);
            viewHolder.editTextAdditional1 = convertView.findViewById(R.id.editTextAdditional1);
            viewHolder.editTextAdditional2 = convertView.findViewById(R.id.editTextAdditional2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TImeLog currentItem = tImeLogs.get(position);
        viewHolder.textView.setText(currentItem.getActivity());

        // Set a tag on the CheckBox with the position to identify it in the listener
        viewHolder.checkBox.setTag(position);
        viewHolder.editTextAdditional2.setTag(position);
        viewHolder.editTextAdditional1.setTag(position);
        // Set the CheckBox state and visibility of the input fields based on the current item
        viewHolder.checkBox.setChecked(currentItem.isChecked);
        if (currentItem.isChecked) {
            viewHolder.layoutAdditionalInputs.setVisibility(View.VISIBLE);
        } else {
            viewHolder.layoutAdditionalInputs.setVisibility(View.GONE);
        }

        // Handle the CheckBox state changes
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = (Integer) buttonView.getTag();
                tImeLogs.get(pos).setChecked(isChecked);

                // Update the visibility of the input fields
                viewHolder.layoutAdditionalInputs.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        // Handle the EditText fields changes for the current item
        viewHolder.editTextAdditional1.setText(tImeLogs.get(position).getComment());
        viewHolder.editTextAdditional1.addTextChangedListener(new TextWatcher() {
            private boolean isUserTyping = true;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Update the text in the TImeLog object when the user types in editTextAdditional1
                    tImeLogs.get( (int)viewHolder.editTextAdditional1.getTag()).setComment(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        viewHolder.editTextAdditional2.setText(tImeLogs.get(position).getTime());
        viewHolder.editTextAdditional2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // Update the text in the TImeLog object when the user types in editTextAdditional1
                tImeLogs.get( (int)viewHolder.editTextAdditional2.getTag()).setTime(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        CheckBox checkBox;
        TextView textView;
        LinearLayout layoutAdditionalInputs;
        EditText editTextAdditional1;
        EditText editTextAdditional2;
    }
}


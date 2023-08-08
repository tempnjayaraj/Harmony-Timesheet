package com.archimedis.harmony;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SettingsDialogFragment extends DialogFragment {

    // Define the interface to handle settings changes
    public interface SettingsDialogListener {
        void onSettingsChanged(String variable1, String variable2, String variable3, String variable4);
    }

    private SettingsDialogListener listener;
    private EditText editText1, editText2, editText3, editText4;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SettingsDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SettingsDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_settings, null);
        builder.setView(view)
                .setTitle("Settings")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String variable1 = editText1.getText().toString();
                        String variable2 = editText2.getText().toString();
                        String variable3 = editText3.getText().toString();
                        String variable4 = editText4.getText().toString();

                        // Notify the listener with the updated settings
                        listener.onSettingsChanged(variable1, variable2, variable3, variable4);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);

        // Load the existing settings (if available) into the EditText fields
        SharedPreferences preferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editText1.setText(preferences.getString("variable1", ""));
        editText2.setText(preferences.getString("variable2", ""));
        editText3.setText(preferences.getString("variable3", ""));
        editText4.setText(preferences.getString("variable4", ""));

        return builder.create();
    }
}

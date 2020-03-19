package com.sean.financialtracker.Utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sean.financialtracker.R;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by sean on 28/8/17.
 */

public class BudgetDialog {
    private SharedPreferences settings;
    private Activity activity;

    public BudgetDialog(SharedPreferences settings, Activity activity) {
        this.settings = settings;
        this.activity = activity;
    }

    public void buildDialog() {
        final Activity thisActivity = activity;
        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity, R.style.AlertDialogTheme);
        // Get the layout inflater
        LayoutInflater inflater = thisActivity.getLayoutInflater();

        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.budget_popup, null);
        builder.setView(layout);

        EditText daily = layout.findViewById(R.id.daily_budget);
        EditText weekly = layout.findViewById(R.id.weekly_budget);
        EditText monthly = layout.findViewById(R.id.monthly_budget);

        daily.setText(String.valueOf(settings.getInt("daily_budget", 25)));
        weekly.setText(String.valueOf(settings.getInt("weekly_budget", 100)));
        monthly.setText(String.valueOf(settings.getInt("monthly_budget", 300)));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        // Add action buttons
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                try {
                    SharedPreferences.Editor editor = settings.edit();
                    EditText edited_daily = layout.findViewById(R.id.daily_budget);
                    EditText edited_weekly = layout.findViewById(R.id.weekly_budget);
                    EditText edited_monthly = layout.findViewById(R.id.monthly_budget);
                    editor.putInt("daily_budget", Integer.parseInt(edited_daily.getText().toString()));
                    editor.putInt("weekly_budget", Integer.parseInt(edited_weekly.getText().toString()));
                    editor.putInt("monthly_budget", Integer.parseInt(edited_monthly.getText().toString()));
                    // Commit the edits!
                    editor.commit();
                    activity.finish();
                    activity.startActivity(activity.getIntent());
                    Toast.makeText(activity, "Budgets changed", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(activity, "The budgets must be filled in!", Toast.LENGTH_SHORT).show();
                }
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        //dialog.getWindow().setLayout(1200, 1800);
    }
}

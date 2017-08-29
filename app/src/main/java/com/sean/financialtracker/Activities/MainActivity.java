package com.sean.financialtracker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sean.financialtracker.Data.DBHandler;
import com.sean.financialtracker.Data.DBHandlerImpl;
import com.sean.financialtracker.Data.Expenditure;
import com.sean.financialtracker.Fragments.FragmentAdapter;
import com.sean.financialtracker.R;
import com.sean.financialtracker.Utils.BudgetDialog;
import com.sean.financialtracker.Utils.DecimalDigitsInputFilter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity {

    private DBHandler db;
    private static final String PREFS_NAME = "MyBudget";
    private Activity thisActivity = this;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((EditText)findViewById(R.id.exp_text)).setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});

        //get sum of each expenditure type from DB
        db = new DBHandlerImpl(this);

        SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, 0);
        if (!(settings.contains("daily_budget") || settings.contains("weekly_budget") || settings.contains("monthly_budget"))) {
            createBudgetDialog();
        }

        viewPager = (ViewPager)findViewById(R.id.vpPager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
        Button dailyToggle = (Button) findViewById(R.id.daily_toggle);
        Button weeklyToggle = (Button) findViewById(R.id.weekly_toggle);
        Button monthlyToggle = (Button) findViewById(R.id.monthly_toggle);

        final Button[] buttonArr = {dailyToggle, weeklyToggle, monthlyToggle};
        buttonArr[viewPager.getCurrentItem()].setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {

                for (Button b : buttonArr) b.setTextColor(Color.BLACK);
                buttonArr[position % 3].setTextColor(ContextCompat.getColor(thisActivity, R.color.colorPrimary));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onClickNewExp(View v) {
        try {
            float exp_cost = Float.parseFloat(((EditText) findViewById(R.id.exp_text)).getText().toString());
            String desc = ((EditText) findViewById(R.id.exp_desc)).getText().toString();
            RadioButton selectedTypeButton = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.exp_type)).getCheckedRadioButtonId());
            String selectedType = selectedTypeButton.getText().toString();

            String date = new Timestamp(System.currentTimeMillis()).toString();

            if (exp_cost == 0f) Toast.makeText(this, "Expenditure cannot be zero!", Toast.LENGTH_SHORT).show();
            else if (desc.equals("")) Toast.makeText(this, "Description cannot be empty!", Toast.LENGTH_SHORT).show();
            else {
                Expenditure exp = new Expenditure(1, exp_cost, selectedType, desc, date);
                db.addExp(exp);
                Toast.makeText(this, "Expenditure added", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Expenditure cannot be empty!", Toast.LENGTH_SHORT).show();
        }

    }

    public void changeBudget(View v) {
        createBudgetDialog();
    }

    public void createBudgetDialog() {
        BudgetDialog budgetDialog = new BudgetDialog(getSharedPreferences(PREFS_NAME, 0), this);
        budgetDialog.buildDialog();
    }

    public void onClickChangeTab(View v) {
        switch (v.getId()) {
            case R.id.daily_toggle:
                viewPager.setCurrentItem(0);
                break;
            case R.id.weekly_toggle:
                viewPager.setCurrentItem(1);
                break;
            case R.id.monthly_toggle:
                viewPager.setCurrentItem(2);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        SlidingUpPanelLayout slider = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        if (slider.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else super.onBackPressed();
    }
}

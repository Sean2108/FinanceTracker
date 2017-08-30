package com.sean.financialtracker.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.sean.financialtracker.Data.DBHandler;
import com.sean.financialtracker.Data.DBHandlerImpl;
import com.sean.financialtracker.Data.Expenditure;
import com.sean.financialtracker.Data.ExpenditureSwipeAdapter;
import com.sean.financialtracker.R;

import java.text.NumberFormat;
import java.util.List;

public class ListExpActivity extends AppCompatActivity {

    private DBHandler db;
    private ExpenditureSwipeAdapter adapter;
    NumberFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_exp);
        db = new DBHandlerImpl(this);
        formatter = NumberFormat.getCurrencyInstance();
        String label = getIntent().getStringExtra("EXP_TYPE");

        ((TextView) findViewById(R.id.header)).setText("Expenses on: " + label);

        changeExpTotal(label);

        List<Expenditure> expList = db.getCategoryExp(label);

        adapter = new ExpenditureSwipeAdapter(this, expList);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

    private String checkRecordsPlural(int expCount, float exp, String range) {
        return expCount < 2 ? range + ": " + formatter.format(exp) + " (" + expCount + " record)" : range + ": " + formatter.format(exp) + " (" + expCount + " records)";
    }

    private void changeExpTotal(String label) {
        float dailyExp = db.getCategoryExpSum(label, "daily");
        float weeklyExp = db.getCategoryExpSum(label, "weekly");
        float monthlyExp = db.getCategoryExpSum(label, "monthly");

        int dailyExpCount = db.getCategoryExpCount(label, "daily");
        int weeklyExpCount = db.getCategoryExpCount(label, "weekly");
        int monthlyExpCount = db.getCategoryExpCount(label, "monthly");

        String dailyStr = checkRecordsPlural(dailyExpCount, dailyExp, "Today");
        String weeklyStr = checkRecordsPlural(weeklyExpCount, weeklyExp, "This week");
        String monthlyStr = checkRecordsPlural(monthlyExpCount, monthlyExp, "This month");

        ((TextView) findViewById(R.id.dailyexp)).setText(dailyStr);
        ((TextView) findViewById(R.id.weeklyexp)).setText(weeklyStr);
        ((TextView) findViewById(R.id.monthlyexp)).setText(monthlyStr);
    }
}

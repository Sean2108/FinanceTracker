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

import java.util.List;

public class ListExpActivity extends AppCompatActivity {

    private DBHandler db;
    private ExpenditureSwipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_exp);
        db = new DBHandlerImpl(this);
        String label = getIntent().getStringExtra("EXP_TYPE");

        ((TextView) findViewById(R.id.header)).setText("Expenses on: " + label);

        float dailyExp = db.getCategoryExpSum(label, "daily");

        float weeklyExp = db.getCategoryExpSum(label, "weekly");

        float monthlyExp = db.getCategoryExpSum(label, "monthly");

        ((TextView) findViewById(R.id.dailyexp)).setText("Today: " + dailyExp);
        ((TextView) findViewById(R.id.weeklyexp)).setText("This Week: " + weeklyExp);
        ((TextView) findViewById(R.id.monthlyexp)).setText("This Month: " + monthlyExp);

        List<Expenditure> expList = db.getCategoryExp(label);

        adapter = new ExpenditureSwipeAdapter(this, expList);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }
}

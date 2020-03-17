package com.sean.financialtracker.Activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.sean.financialtracker.Data.DBHandler;
import com.sean.financialtracker.Data.DBHandlerImpl;
import com.sean.financialtracker.Data.Expenditure;
import com.sean.financialtracker.Data.ExpenditureSwipeAdapter;
import com.sean.financialtracker.R;
import com.sean.financialtracker.Utils.FormatListExpUtils;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ListExpActivity extends AppCompatActivity {

    private class ExpenditureCategory {
        private ExpenditureCategory(int resourceId, String queryDateRange, String range) {
            this.resourceId = resourceId;
            this.queryDateRange = queryDateRange;
            this.range = range;
        }

        private int resourceId;
        private String queryDateRange;
        private String range;
    }

    private DBHandler db;
    private ExpenditureSwipeAdapter adapter;
    private FormatListExpUtils formatListExpUtils;
    private ExpenditureCategory[] ExpenditureCategories = {new ExpenditureCategory(R.id.dailyexp, "daily", "Today"),
            new ExpenditureCategory(R.id.weeklyexp, "weekly", "This week"),
            new ExpenditureCategory(R.id.monthlyexp, "monthly", "This month")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_exp);
        db = new DBHandlerImpl(this);
        formatListExpUtils = new FormatListExpUtils(db);
        final String label = getIntent().getStringExtra("EXP_TYPE");

        ((TextView) findViewById(R.id.header)).setText("Expenses on: " + label);

        changeExpTotal(label);

        List<Expenditure> expList = db.getCategoryExp(label);

        adapter = new ExpenditureSwipeAdapter(this, expList);
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);

        adapter.setOnDataChangeListener(new ExpenditureSwipeAdapter.OnDataChangeListener() {
            public void onDataChanged() {
                changeExpTotal(label);
            }
        });
    }

    private void changeExpTotal(String label) {
        for (ExpenditureCategory expenditureCategory : ExpenditureCategories) {
            String expStr = formatListExpUtils.getCategoryExpTotal(label, expenditureCategory.queryDateRange, expenditureCategory.range);
            ((TextView) findViewById(expenditureCategory.resourceId)).setText(expStr);
        }
    }
}

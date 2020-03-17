package com.sean.financialtracker.Utils;

import com.sean.financialtracker.Data.DBHandler;

import java.text.NumberFormat;

public class FormatListExpUtils {

    private NumberFormat formatter;
    private DBHandler db;

    public FormatListExpUtils(DBHandler db) {
        formatter = NumberFormat.getCurrencyInstance();
        this.db = db;
    }

    private String checkRecordsPlural(int expCount, float exp, String range) {
        return expCount == 1 ? range + ": " + formatter.format(exp) + " (" + expCount + " record)" : range + ": " + formatter.format(exp) + " (" + expCount + " records)";
    }

    public String getCategoryExpTotal(String label, String queryDateRange, String range) {
        float exp = db.getCategoryExpSum(label, queryDateRange);
        int count = db.getCategoryExpCount(label, queryDateRange);
        return checkRecordsPlural(count, exp, range);
    }
}

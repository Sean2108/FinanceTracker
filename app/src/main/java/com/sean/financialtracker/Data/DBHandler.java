package com.sean.financialtracker.Data;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by sean on 28/8/17.
 */

public interface DBHandler {
    void onCreate(SQLiteDatabase db);

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    void addExp(Expenditure exp);

    Expenditure getExp(int id);

    List<Expenditure> getAllExp();

    int getExpCount();

    void updateExp(Expenditure exp);

    void deleteExp(Expenditure exp);

    void deleteAllExp();

    float getCategoryExpSum(String type, String queryDateRange);

    List<Expenditure> getCategoryExp(String type);
}

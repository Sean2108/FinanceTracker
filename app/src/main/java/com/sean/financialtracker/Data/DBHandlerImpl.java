package com.sean.financialtracker.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean on 27/8/17.
 */

public class DBHandlerImpl extends SQLiteOpenHelper implements DBHandler {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TrackerDB";
    private static final String TABLE_FINANCES = "tracker";
    private static final String KEY_ID = "id";
    private static final String KEY_COST = "cost";
    private static final String KEY_TYPE = "exp_type";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "exp_date";

    public DBHandlerImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRACKER_TABLE = "CREATE TABLE " + TABLE_FINANCES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_COST + " REAL," + KEY_TYPE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT," + KEY_DATE + " DATE)";
        db.execSQL(CREATE_TRACKER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINANCES);
        onCreate(db);
    }

    @Override
    public void addExp(Expenditure exp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COST, exp.getCost());
        values.put(KEY_TYPE, exp.getType());
        values.put(KEY_DESCRIPTION, exp.getDesc());
        values.put(KEY_DATE, exp.getDate());
        // Inserting Row
        db.insert(TABLE_FINANCES, null, values);
        db.close(); // Closing database connection
    }

    @Override
    public Expenditure getExp(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FINANCES, new String[] { KEY_ID, KEY_COST,
                        KEY_TYPE, KEY_DESCRIPTION, KEY_DATE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Expenditure exp = new Expenditure(Integer.parseInt(cursor.getString(0)), Float.parseFloat(cursor.getString(1)),
                cursor.getString(2), cursor.getString(3), cursor.getString(4));
        cursor.close();
        db.close();
        return exp;
    }

    @Override
    public List<Expenditure> getAllExp() {
        List<Expenditure> expList = new ArrayList<Expenditure>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FINANCES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expenditure exp = new Expenditure();
                exp.setId(Integer.parseInt(cursor.getString(0)));
                exp.setCost(Float.parseFloat(cursor.getString(1)));
                exp.setType(cursor.getString(2));
                exp.setDesc(cursor.getString(3));
                exp.setDate(cursor.getString(4));
                // Adding contact to list
                expList.add(exp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return expList;
    }

    @Override
    public int getExpCount() {
        String countQuery = "SELECT * FROM " + TABLE_FINANCES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return count
        return count;
    }

    @Override
    public void updateExp(Expenditure exp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COST, exp.getCost());
        values.put(KEY_TYPE, exp.getType());
        values.put(KEY_DESCRIPTION, exp.getDesc());
        values.put(KEY_DATE, exp.getDate());
        // updating row
        db.update(TABLE_FINANCES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(exp.getId())});
        db.close();
    }

    @Override
    public void deleteExp(Expenditure exp) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FINANCES, KEY_ID + " = ?",
                new String[] { String.valueOf(exp.getId()) });
        db.close();
    }

    @Override
    public void deleteAllExp() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FINANCES, null, null);
        db.close();
    }

    @Override
    public float getCategoryExpSum(String type, String queryDateRange) {
        String countQuery = "";
        switch(queryDateRange) {
            case "daily":
                countQuery = "SELECT SUM(" + KEY_COST + ") FROM " + TABLE_FINANCES + " WHERE " + KEY_TYPE + " = \'" + type + "\' AND date(" + KEY_DATE + ") = date('now', 'localtime')";
                break;
            case "weekly":
                countQuery = "SELECT SUM(" + KEY_COST + ") FROM " + TABLE_FINANCES + " WHERE " + KEY_TYPE + " = \'" + type + "\' AND date(" + KEY_DATE + ") >= DATE('now', 'weekday 0', '-7 days')";
                break;
            case "monthly":
                countQuery = "SELECT SUM(" + KEY_COST + ") FROM " + TABLE_FINANCES + " WHERE " + KEY_TYPE + " = \'" + type + "\' AND strftime('%m', " + KEY_DATE + ") = strftime('%m', 'now')";
                break;
            default:
                countQuery = "";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        float amount;
        if(cursor.moveToFirst())
            amount = cursor.getFloat(0);
        else
            amount = -1;
        cursor.close();
        db.close();
        // return count
        return amount;
    }

    @Override
    public List<Expenditure> getCategoryExp(String type) {
        List<Expenditure> expList = new ArrayList<Expenditure>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FINANCES + " WHERE " + KEY_TYPE + " = \'" + type + "\' ORDER BY date( " + KEY_DATE + ") DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expenditure exp = new Expenditure();
                exp.setId(Integer.parseInt(cursor.getString(0)));
                exp.setCost(Float.parseFloat(cursor.getString(1)));
                exp.setType(cursor.getString(2));
                exp.setDesc(cursor.getString(3));
                exp.setDate(cursor.getString(4));
                // Adding contact to list
                expList.add(exp);
            } while (cursor.moveToNext());
        }
        // return contact list
        cursor.close();
        db.close();
        return expList;
    }
}

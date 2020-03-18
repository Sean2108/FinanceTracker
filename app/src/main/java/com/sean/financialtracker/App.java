package com.sean.financialtracker;

import android.app.Application;

import com.sean.financialtracker.Data.DBHandler;
import com.sean.financialtracker.Data.DBHandlerImpl;

public class App extends Application {
    private DBHandler dbHandler;

    public void onCreate() {
        super.onCreate();
        dbHandler = new DBHandlerImpl(getApplicationContext());
    }

    public DBHandler getDbHandler() {
        return dbHandler;
    }
}

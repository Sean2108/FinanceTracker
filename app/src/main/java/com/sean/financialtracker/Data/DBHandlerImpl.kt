package com.sean.financialtracker.Data

import android.content.Context
import androidx.room.Room

class DBHandlerImpl(context : Context) : DBHandler {

    private val appDatabase: AppDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "TrackerDB"
    ).allowMainThreadQueries().build()

    override fun addExp(exp: Expenditure) {
        appDatabase.expenditureDao().addExp(exp)
    }

    override fun getExp(id: Int): Expenditure {
        return appDatabase.expenditureDao().getExp(id)
    }

    override fun getAllExp(): List<Expenditure> {
        return appDatabase.expenditureDao().getAllExp()
    }

    override fun getExpCount(): Int {
        return appDatabase.expenditureDao().getExpCount()
    }

    override fun getCategoryExpCount(type: String, queryDateRange: String): Int {
        return appDatabase.expenditureDao().getCategoryExpCount(type, queryDateRange)
    }

    override fun updateExp(exp: Expenditure) {
        appDatabase.expenditureDao().updateExp(exp)
    }

    override fun deleteExp(exp: Expenditure) {
        appDatabase.expenditureDao().deleteExp(exp)
    }

    override fun deleteAllExp() {
        appDatabase.expenditureDao().deleteAllExp()
    }

    override fun getCategoryExpSum(type: String, queryDateRange: String): Float {
        return appDatabase.expenditureDao().getCategoryExpSum(type, queryDateRange)
    }

    override fun getCategoryExp(type: String): List<Expenditure> {
        return appDatabase.expenditureDao().getCategoryExp(type)
    }
}
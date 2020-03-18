package com.sean.financialtracker.Data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Expenditure::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenditureDao(): ExpenditureDao
}
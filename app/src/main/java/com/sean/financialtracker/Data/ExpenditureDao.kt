package com.sean.financialtracker.Data

import androidx.room.*

@Dao
interface ExpenditureDao {
    @Query("SELECT * FROM tracker")
    fun getAllExp(): List<Expenditure>

    @Query("SELECT * FROM tracker WHERE id = :id LIMIT 1")
    fun getExp(id: Int): Expenditure

    fun getCategoryExpCount(type: String, queryDateRange: String): Int {
        return when (queryDateRange) {
            "daily" -> getCategoryExpCountForDay(type)
            "weekly" -> getCategoryExpCountForWeek(type)
            "monthly" -> getCategoryExpCountForMonth(type)
            else -> 0
        }
    }

    @Query("SELECT COUNT(*) FROM tracker")
    fun getExpCount(): Int

    fun getCategoryExpSum(type: String, queryDateRange: String): Float {
        return when (queryDateRange) {
            "daily" -> getCategoryExpSumForDay(type)
            "weekly" -> getCategoryExpSumForWeek(type)
            "monthly" -> getCategoryExpSumForMonth(type)
            else -> -1f
        }
    }

    @Query("SELECT COUNT(*) FROM tracker WHERE exp_type = :type AND date(exp_date) = date('now', 'localtime')")
    fun getCategoryExpCountForDay(type: String): Int

    @Query("SELECT COUNT(*) FROM tracker WHERE exp_type = :type AND date(exp_date) >= DATE('now', 'weekday 0', '-7 days')")
    fun getCategoryExpCountForWeek(type: String): Int

    @Query("SELECT COUNT(*) FROM tracker WHERE exp_type = :type AND strftime( '%m', exp_date)  = strftime('%m', 'now')")
    fun getCategoryExpCountForMonth(type: String): Int

    @Query("SELECT SUM(cost) FROM tracker WHERE exp_type = :type AND date(exp_date) = date('now', 'localtime')")
    fun getCategoryExpSumForDay(type: String): Float

    @Query("SELECT SUM(cost) FROM tracker WHERE exp_type = :type AND date(exp_date) >= DATE('now', 'weekday 0', '-7 days')")
    fun getCategoryExpSumForWeek(type: String): Float

    @Query("SELECT SUM(cost) FROM tracker WHERE exp_type = :type AND strftime( '%m', exp_date)  = strftime('%m', 'now')")
    fun getCategoryExpSumForMonth(type: String): Float

    @Query("SELECT * FROM tracker WHERE exp_type = :type ORDER BY date(exp_date) DESC")
    fun getCategoryExp(type: String): List<Expenditure>

    @Update
    fun updateExp(exp: Expenditure)

    @Insert
    fun addExp(exp: Expenditure)

    @Delete
    fun deleteExp(exp: Expenditure)

    @Query("DELETE FROM tracker")
    fun deleteAllExp()
}
package com.sean.financialtracker.Data

interface DBHandler {
    fun addExp(exp: Expenditure)

    fun getExp(id: Int): Expenditure

    fun getAllExp(): List<Expenditure>

    fun getExpCount(): Int

    fun getCategoryExpCount(type: String, queryDateRange: String): Int

    fun updateExp(exp: Expenditure)

    fun deleteExp(exp: Expenditure)

    fun deleteAllExp()

    fun getCategoryExpSum(type: String, queryDateRange: String): Float

    fun getCategoryExp(type: String): List<Expenditure>
}
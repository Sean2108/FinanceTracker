package com.sean.financialtracker

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.sean.financialtracker.Data.AppDatabase
import com.sean.financialtracker.Data.Expenditure
import com.sean.financialtracker.Data.ExpenditureDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DatabaseTest {
    private lateinit var expenditureDao: ExpenditureDao
    private lateinit var db: AppDatabase

    private fun getDateBeforeCurrentTime(daysBefore: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1 * daysBefore)
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(cal.time)
    }

    private fun getDayOfWeek(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.DAY_OF_WEEK)
    }

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        expenditureDao = db.expenditureDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun getAllExp_whenDbIsEmpty_shouldReturnEmptyList() {
        assert(expenditureDao.getAllExp().isEmpty())
    }

    @Test
    fun addExp_shouldBeAbleToRetrieveAddedRecordWithGetAndGetAll() {
        val exp = Expenditure(null, 1f, "Transport", "Bus", "2020-01-01")
        expenditureDao.addExp(exp)
        val results = expenditureDao.getAllExp()
        assertEquals(results.size, 1)
        assert(exp == results[0])
        assert(exp == expenditureDao.getExp(1))
        assertEquals(1, expenditureDao.getExpCount())
    }

    @Test
    fun updateExp_shouldBeAbleToRetrieveUpdatedRecordWithGetAndGetAll() {
        val exp = Expenditure(1, 1f, "Transport", "Bus", "2020-01-01")
        expenditureDao.addExp(exp)
        val updatedExp = Expenditure(1, 1.1f, "Transport", "Train", "2020-01-01")
        expenditureDao.updateExp(updatedExp)
        assertEquals(1.1f, expenditureDao.getExp(1).cost)
        assertEquals("Train", expenditureDao.getExp(1).desc)
        assertEquals(1, expenditureDao.getExpCount())
    }

    @Test
    fun deleteExp_shouldRemoveRecordCorrectly() {
        val exp = Expenditure(1, 1f, "Transport", "Bus", "2020-01-01")
        val exp2 = Expenditure(2, 1.1f, "Transport", "Train", "2020-01-01")
        expenditureDao.addExp(exp)
        expenditureDao.addExp(exp2)
        expenditureDao.deleteExp(exp)
        val results = expenditureDao.getAllExp()
        assertEquals(1, results.size)
        assert(exp2 == results[0])
    }

    @Test
    fun deleteAllExp_shouldRemoveAllRecords() {
        val expToday1 = Expenditure(null, 1f, "Transport", "Bus", getDateBeforeCurrentTime(0))
        val expToday2 = Expenditure(null, 2f, "Transport", "Train", getDateBeforeCurrentTime(0))
        val expToday3 = Expenditure(null, 3f, "Food", "Chips", getDateBeforeCurrentTime(0))
        val expYesterday1 = Expenditure(null, 4f, "Transport", "Train", getDateBeforeCurrentTime(1))
        val expThisWeek = Expenditure(null, 5f, "Transport", "Train", getDateBeforeCurrentTime(6))
        val expThisMonth = Expenditure(null, 6f, "Transport", "Train", getDateBeforeCurrentTime(7))
        expenditureDao.addExp(expToday1)
        expenditureDao.addExp(expToday2)
        expenditureDao.addExp(expToday3)
        expenditureDao.addExp(expYesterday1)
        expenditureDao.addExp(expThisWeek)
        expenditureDao.addExp(expThisMonth)
        assertEquals(expenditureDao.getAllExp().size, 6)
        expenditureDao.deleteAllExp()
        assert(expenditureDao.getAllExp().isEmpty())
    }

    @Test
    fun getCategoryExp_shouldReturnCorrectExps() {
        val expToday1 = Expenditure(null, 1f, "Transport", "Bus", getDateBeforeCurrentTime(0))
        val expToday2 = Expenditure(null, 2f, "Transport", "Train", getDateBeforeCurrentTime(0))
        val expToday3 = Expenditure(null, 3f, "Food", "Chips", getDateBeforeCurrentTime(0))
        val expYesterday = Expenditure(null, 4f, "Transport", "Train", getDateBeforeCurrentTime(1))
        val expThisWeek = Expenditure(null, 5f, "Transport", "Train", getDateBeforeCurrentTime(6))
        val expThisMonth = Expenditure(null, 6f, "Transport", "Train", getDateBeforeCurrentTime(7))
        val expLongAgo1 = Expenditure(null, 7f, "Transport", "Train", getDateBeforeCurrentTime(32))
        val expLongAgo2 = Expenditure(null, 8f, "Food", "Fish", getDateBeforeCurrentTime(32))
        expenditureDao.addExp(expToday1)
        expenditureDao.addExp(expToday2)
        expenditureDao.addExp(expToday3)
        expenditureDao.addExp(expYesterday)
        expenditureDao.addExp(expThisWeek)
        expenditureDao.addExp(expThisMonth)
        expenditureDao.addExp(expLongAgo1)
        expenditureDao.addExp(expLongAgo2)
        val resultTransport = expenditureDao.getCategoryExp("Transport")
        assertEquals(6, resultTransport.size)
        assert(resultTransport[0] == expToday1)
        assert(resultTransport[1] == expToday2)
        assert(resultTransport[2] == expYesterday)
        assert(resultTransport[3] == expThisWeek)
        assert(resultTransport[4] == expThisMonth)
        assert(resultTransport[5] == expLongAgo1)
        val resultFood = expenditureDao.getCategoryExp("Food")
        assertEquals(2, resultFood.size)
        assert(resultFood[0] == expToday3)
        assert(resultFood[1] == expLongAgo2)
    }

    @Test
    fun getCategoryExpCount_shouldReturnCorrectCounts() {
        val expToday1 = Expenditure(null, 1f, "Transport", "Bus", getDateBeforeCurrentTime(0))
        val expToday2 = Expenditure(null, 2f, "Transport", "Train", getDateBeforeCurrentTime(0))
        val expToday3 = Expenditure(null, 3f, "Food", "Chips", getDateBeforeCurrentTime(0))
        val expYesterday = Expenditure(null, 4f, "Transport", "Train", getDateBeforeCurrentTime(1))
        val expThisWeek = Expenditure(null, 5f, "Transport", "Train", getDateBeforeCurrentTime(6))
        val expThisMonth = Expenditure(null, 6f, "Transport", "Train", getDateBeforeCurrentTime(7))
        val expLongAgo1 = Expenditure(null, 7f, "Transport", "Train", getDateBeforeCurrentTime(32))
        val expLongAgo2 = Expenditure(null, 8f, "Food", "Fish", getDateBeforeCurrentTime(32))
        expenditureDao.addExp(expToday1)
        expenditureDao.addExp(expToday2)
        expenditureDao.addExp(expToday3)
        expenditureDao.addExp(expYesterday)
        expenditureDao.addExp(expThisWeek)
        expenditureDao.addExp(expThisMonth)
        expenditureDao.addExp(expLongAgo1)
        expenditureDao.addExp(expLongAgo2)
        val numberOfTransportExpThisWeek = when(getDayOfWeek()) {
            Calendar.MONDAY -> 2
            Calendar.SUNDAY -> 4
            else -> 3
        }
        assertEquals(2, expenditureDao.getCategoryExpCount("Transport", "daily"))
        assertEquals(numberOfTransportExpThisWeek, expenditureDao.getCategoryExpCount("Transport", "weekly"))
        assertEquals(5, expenditureDao.getCategoryExpCount("Transport", "monthly"))
        assertEquals(1, expenditureDao.getCategoryExpCount("Food", "daily"))
        assertEquals(1, expenditureDao.getCategoryExpCount("Food", "weekly"))
        assertEquals(1, expenditureDao.getCategoryExpCount("Food", "monthly"))
    }

    @Test
    fun getCategoryExpSum_shouldReturnCorrectSums() {
        val expToday1 = Expenditure(null, 1f, "Transport", "Bus", getDateBeforeCurrentTime(0))
        val expToday2 = Expenditure(null, 2f, "Transport", "Train", getDateBeforeCurrentTime(0))
        val expToday3 = Expenditure(null, 3f, "Food", "Chips", getDateBeforeCurrentTime(0))
        val expYesterday = Expenditure(null, 4f, "Transport", "Train", getDateBeforeCurrentTime(1))
        val expThisWeek = Expenditure(null, 5f, "Transport", "Train", getDateBeforeCurrentTime(6))
        val expThisMonth = Expenditure(null, 6f, "Transport", "Train", getDateBeforeCurrentTime(7))
        val expLongAgo1 = Expenditure(null, 7f, "Transport", "Train", getDateBeforeCurrentTime(32))
        val expLongAgo2 = Expenditure(null, 8f, "Food", "Fish", getDateBeforeCurrentTime(32))
        expenditureDao.addExp(expToday1)
        expenditureDao.addExp(expToday2)
        expenditureDao.addExp(expToday3)
        expenditureDao.addExp(expYesterday)
        expenditureDao.addExp(expThisWeek)
        expenditureDao.addExp(expThisMonth)
        expenditureDao.addExp(expLongAgo1)
        expenditureDao.addExp(expLongAgo2)
        val numberOfTransportExpThisWeek = when(getDayOfWeek()) {
            Calendar.MONDAY -> 3f
            Calendar.SUNDAY -> 12f
            else -> 7f
        }
        assertEquals(3f, expenditureDao.getCategoryExpSum("Transport", "daily"))
        assertEquals(numberOfTransportExpThisWeek, expenditureDao.getCategoryExpSum("Transport", "weekly"))
        assertEquals(18f, expenditureDao.getCategoryExpSum("Transport", "monthly"))
        assertEquals(3f, expenditureDao.getCategoryExpSum("Food", "daily"))
        assertEquals(3f, expenditureDao.getCategoryExpSum("Food", "weekly"))
        assertEquals(3f, expenditureDao.getCategoryExpSum("Food", "monthly"))
    }
}
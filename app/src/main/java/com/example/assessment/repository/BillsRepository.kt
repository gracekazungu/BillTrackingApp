package com.example.assessment.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.assessment.BillzApp
import com.example.assessment.database.BillDb
import com.example.assessment.database.UpcomingBillsDao
import com.example.assessment.model.Bill
import com.example.assessment.model.UpcomingBill
import com.example.assessment.utils.Constants
import com.example.assessment.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class BillsRepository{
    private val db=BillDb.getDatabase(BillzApp.appContext)
    private val billDao=db.billDao()
    private val upcomingBillsDao=db.upcomingBIllsDao()


    suspend fun saveBill(bill: Bill){
        withContext(Dispatchers.IO){
            billDao.saveBill(bill)
        }
    }

    fun getAllBills(): LiveData<List<Bill>> {
        return billDao.getAllBills()
    }

    suspend fun insertUpcomingBill(upcomingBill: UpcomingBill){
        withContext(Dispatchers.IO){
            upcomingBillsDao.insertUpcomingBill(upcomingBill)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createRecurringMonthlyBills(){
        withContext(Dispatchers.IO){
            val monthlyBills=billDao.getRecurringBills(Constants.MONTHLY)
            val startDate=DateTimeUtils.getFirstDayOfMonth()
            val endDate=DateTimeUtils.getLastDayOfMonth()


            monthlyBills.forEach{bill->
                val existing=upcomingBillsDao.queryExistingBill(bill.billId,startDate, endDate)
                if(existing.isEmpty()){
                    val newUpcomingBill=UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        billId=bill.billId,
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = DateTimeUtils.createDateFromDay(bill.dueDate),
                        userId = bill.userId,
                        paid = false
                    )
                    upcomingBillsDao.insertUpcomingBill(newUpcomingBill)
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createRecurringWeeklyBills(){
        withContext(Dispatchers.IO){
            val weeklyBills=billDao.getRecurringBills(Constants.WEEKLY)
            val startDate=DateTimeUtils.getFirstDateOfWeek()
            val endDate=DateTimeUtils.getLastDateOfWeek()
            weeklyBills.forEach { bill->
                val existingBill=upcomingBillsDao.queryExistingBill(bill.billId,startDate, endDate)
                if (existingBill.isEmpty()){
                    val newWeeklyBills=UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        billId=bill.billId,
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = DateTimeUtils.getDateOfWeekDay(bill.dueDate),
                        userId = bill.userId,
                        paid = false
                    )
                    upcomingBillsDao.insertUpcomingBill(newWeeklyBills)
                }
            }


        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createRecurringQuarterlyBills() {
        withContext(Dispatchers.IO) {
            val quarterlyBills = billDao.getRecurringBills(Constants.QUARTERLY)
            val currentYear = DateTimeUtils.getCurrentYear()

            for (quarter in 1..4) {
                val startDate = DateTimeUtils.getQuarterStartDate(currentYear, quarter)
                val endDate = DateTimeUtils.getQuarterEndDate(currentYear, quarter)

                quarterlyBills.forEach { bill ->
                    val dueDateAsInt = bill.dueDate.toInt()

                    if (dueDateAsInt in 1..31 && quarter * 3 in 1..12) {
                        val existingBill = upcomingBillsDao.queryExistingBill(bill.billId, startDate, endDate)

                        if (existingBill.isEmpty()) {
                            val newQuarterlyBill = UpcomingBill(
                                upcomingBillId = UUID.randomUUID().toString(),
                                billId = bill.billId,
                                name = bill.name,
                                amount = bill.amount,
                                frequency = bill.frequency,
                                dueDate = DateTimeUtils.createDateFromDayAndMonth(dueDateAsInt, quarter * 3),
                                userId = bill.userId,
                                paid = false
                            )
                            upcomingBillsDao.insertUpcomingBill(newQuarterlyBill)
                        }
                    } else {
                        Log.e("BillsRepository", "Invalid day-of-month or month value for bill: ${bill.billId}")
                    }
                }
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createRecurringAnnuallyBills(){
    withContext(Dispatchers.IO){
        val annualBills=billDao.getRecurringBills(Constants.ANNUAL)
        val currentYear=DateTimeUtils.getCurrentYear()
        val startDate="$currentYear-01-01"
        val endDate="$currentYear-12-31"
        annualBills.forEach { bill->
            val existingBill=upcomingBillsDao.queryExistingBill(bill.billId,startDate, endDate)
            if (existingBill.isEmpty()){
                val newAnnualBills=UpcomingBill(
                    upcomingBillId = UUID.randomUUID().toString(),
                    billId=bill.billId,
                    name = bill.name,
                    amount = bill.amount,
                    frequency = bill.frequency,
                    dueDate = "$currentYear-${bill.dueDate}",
                    userId = bill.userId,
                    paid = false
                )
                upcomingBillsDao.insertUpcomingBill(newAnnualBills)
            }
        }


    }
}

    fun getUpcomingBillsByFrequency(freq:String):LiveData<List<UpcomingBill>>{
        return upcomingBillsDao.getUpcomingBillsByFrequency(freq ,false)
    }


    suspend fun updateUpcomingBill(upcomingBill: UpcomingBill){
        withContext(Dispatchers.IO){
            upcomingBillsDao.updateUpcomingBill(upcomingBill)
        }
    }

    fun getPaidBills():LiveData<List<UpcomingBill>>{
        return upcomingBillsDao.getPaidBills()
    }
}







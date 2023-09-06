package com.example.assessment.repository

import androidx.lifecycle.LiveData
import com.example.assessment.BillzApp
import com.example.assessment.database.BillDb
import com.example.assessment.model.Bill
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BillsRepository{
    val db=BillDb.getDatabase(BillzApp.appContext)
    val billDao=db.billDao()


    suspend fun saveBill(bill: Bill){
        withContext(Dispatchers.IO){
            billDao.saveBill(bill)
        }
    }

    fun getAllBills(): LiveData<List<Bill>> {
        return billDao.getAllBills()
    }

//    fun getBillById(billId:Int): LiveData<Bill> {
//        return database.billDao().getBillById(billId)
//    }
//
//    suspend fun deleteBillById(bill: Bill) {
//        withContext(Dispatchers.IO) {
//            billDao?.deleteBillById(bill)
//        }
//    }


}







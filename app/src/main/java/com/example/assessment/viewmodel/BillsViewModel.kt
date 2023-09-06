package com.example.assessment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assessment.model.Bill
import com.example.assessment.repository.BillsRepository
import kotlinx.coroutines.launch

class BillsViewModel:ViewModel() {
    val billsRepo= BillsRepository()

    fun saveBill(bill: Bill){
        viewModelScope.launch {
            billsRepo.saveBill(bill)
        }
    }
    fun getAllBills(): LiveData<List<Bill>> {
        return billsRepo.getAllBills()
    }

//    fun getBillById(billId:Int):LiveData<Bill>{
//        return billsRepo.getBillById(billId)
//    }
//
//    fun deleteBill(bill: Bill) {
//        viewModelScope.launch {
//            billsRepo.deleteBillById(bill)
//        }
//    }


}
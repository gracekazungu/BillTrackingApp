package com.example.assessment.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "UpcomingBills",
indices = [Index(value=["billId", "dueDate"], unique=true)])
data class UpcomingBill(
    @PrimaryKey val upcomingBillId: String,
    var billId:String,
    var name:String,
    var amount: Double,
    var frequency:String,
    var dueDate:String,
    var userId:String,
    var paid: Boolean
)

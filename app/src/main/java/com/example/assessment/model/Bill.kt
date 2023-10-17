package com.example.assessment.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Bills",
    indices = [Index(value=["name"], unique=true)])
data class Bill(
    @Expose @SerializedName("bill_id") @PrimaryKey var billId:String,
    @Expose var name:String,
    @Expose var amount: Double,
    @Expose var frequency:String,
    @Expose @SerializedName("due_date")var dueDate:String,
    @Expose @SerializedName("user_id")var userId:String,
    var synched:Boolean
)

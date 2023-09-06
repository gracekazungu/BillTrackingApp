package com.example.assessment.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("first_name") var firstname:String,
    @SerializedName("last_name") var lastname:String,
    var email:String,
    @SerializedName("phone_number") var phoneNumber:String,
    @SerializedName("user_Id") var userId: String
)

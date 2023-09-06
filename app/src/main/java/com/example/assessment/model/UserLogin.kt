package com.example.assessment.model

import com.google.gson.annotations.SerializedName

data class UserLogin(
     var email:String,
     var password: String,
//    @SerializedName("user_id") var userId: String

)

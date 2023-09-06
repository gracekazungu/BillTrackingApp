package com.example.assessment.api

import com.example.assessment.model.LoginRequest
import com.example.assessment.model.LoginResponse
import com.example.assessment.model.RegisterRequest
import com.example.assessment.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("/users/register")
   suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

   @POST("/users/login")
   suspend fun loginUser(@Body loginRequest: LoginRequest):Response<LoginResponse>
}
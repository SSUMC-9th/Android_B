package com.example.umctest1

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/signup")
    fun signUp(@Body request: SignUpRequest): Call<AuthResponse>

    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
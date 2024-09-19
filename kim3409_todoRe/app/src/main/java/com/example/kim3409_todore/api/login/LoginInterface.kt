package com.example.kim3409_todore.api.login

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginService {
    @POST("android/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("android/register")
    fun signUp(@Body signUpRequest: SignUpRequest ):  Call<SignUpResponse>

    @POST("android/verifyJWT")
    fun verifyJWT(@Body verifyRequest: VerifyRequest): Call<VerifyResponse>
}

data class LoginRequest(
    val id: String,
    val pw: String
)

data class LoginResponse(
    val token: String?,
    val message: String?
)

data class SignUpRequest(
    val id: String,
    val pw: String
)

data class SignUpResponse(
    val message: String?
)

data class  VerifyRequest(
    val token: String
)

data class VerifyResponse(
    val message: String?,
    val userId: String?
)
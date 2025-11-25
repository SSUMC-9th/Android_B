package com.example.umctest1

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    fun signUp(request: SignUpRequest) {

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.signUp(request).enqueue(object: Callback<AuthResponse> {

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS", response.toString())

                if (!response.isSuccessful) {
                    val error = response.errorBody()?.string()
                    Log.e(
                        "SIGNUP/HTTP_ERROR",
                        "code=${response.code()}, msg=${response.message()}, body=$error"
                    )
                    return
                }

                val resp = response.body()
                if (resp == null) {
                    Log.e("SIGNUP/BODY_NULL", "response body is null")
                    return
                }

                when (resp.code) {
                    "COMMON201" -> {
                        Log.d("SIGNUP", "회원가입 성공: ${resp.message}")
                        signUpView.onSignUpSuccess()
                    }
                    "AUTH400_1" -> {
                        Log.d("SIGNUP", "이미 가입된 이메일: ${resp.message}")
                    }
                    else -> {
                        Log.d("SIGNUP", "기타 응답 코드: ${resp.code}, msg=${resp.message}")
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("SIGNUP/FAILURE", "network failure: ${t.message}", t)
            }
        })

        Log.d("SIGNUP", "HELLO")
    }

    fun login(request: LoginRequest) {

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.login(request).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("LOGIN/SUCCESS", response.toString())

                if (!response.isSuccessful) {
                    val error = response.errorBody()?.string()
                    Log.e(
                        "LOGIN/HTTP_ERROR",
                        "code=${response.code()}, msg=${response.message()}, body=$error"
                    )
                    return
                }

                val resp = response.body()
                if (resp == null) {
                    Log.e("LOGIN/BODY_NULL", "response body is null")
                    return
                }

                when (resp.code) {
                    "COMMON200_1" -> {
                        Log.d("LOGIN", "로그인 성공: ${resp.message}")

                        val name = resp.data?.name
                        val memberId = resp.data?.memberId
                        val accessToken = resp.data?.accessToken

                        Log.d("LOGIN", "name=$name, memberId=$memberId")
                        Log.d("LOGIN", "accessToken=$accessToken")

                    }

                    "AUTH404_1" -> {
                        Log.d("LOGIN", "아이디/비밀번호 오류: ${resp.message}")
                    }

                    else -> {
                        Log.d("LOGIN", "기타 응답 코드: ${resp.code}, msg=${resp.message}")
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LOGIN/FAILURE", "network failure: ${t.message}", t)
            }
        })

        Log.d("LOGIN", "HELLO")
    }

}

package com.example.kim3409_todore.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kim3409_todore.api.RetrofitInstance
import com.example.kim3409_todore.api.login.LoginRequest
import com.example.kim3409_todore.api.login.LoginResponse
import com.example.kim3409_todore.api.login.LoginService
import com.example.kim3409_todore.api.login.SignUpRequest
import com.example.kim3409_todore.api.login.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {
    private val _id = MutableLiveData<String>()
    private val _pw = MutableLiveData<String>()
    private val _loginStatus = MutableLiveData<Boolean>()
    private val _serverMessage = MutableLiveData<String>()
    val serverMessage: LiveData<String> get() = _serverMessage
    val loginStatus: LiveData<Boolean> get() = _loginStatus
    private val _token = MutableLiveData<String?>()
    val token: MutableLiveData<String?> get() = _token
    private val loginService: LoginService = RetrofitInstance.getLocalRetrofitInstance().create(LoginService::class.java)
    fun setId(id: String) {
        _id.value = id
    }

    fun setPw(pw: String) {
        _pw.value = pw
    }
    fun login() {
        val id = _id.value
        val pw = _pw.value

        if (id.isNullOrEmpty() || pw.isNullOrEmpty()) {
            _serverMessage.value = "아이디와 비밀번호를 입력하세요."
            return
        }

        if (id == "test" && pw == "test") {
        } else {
            val loginRequest = LoginRequest(id, pw)
            val call: Call<LoginResponse> = loginService.login(loginRequest)
            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val message = response.body()?.message ?: "로그인 성공"
                        val token = response.body()?.token
                        _token.value = token // 토큰을 LiveData로 전달
                        _serverMessage.value = message
                        _loginStatus.value = true
                    } else {
                        val errorMessage = "${response.errorBody()?.string()}"
                        _serverMessage.value = errorMessage
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    t.printStackTrace()
                    val errorMessage = "로그인 실패"
                    _serverMessage.value = errorMessage
                }
            })
        }
    }

    fun signUp() {
        val id = _id.value
        val pw = _pw.value

        if (id.isNullOrEmpty() || pw.isNullOrEmpty()) {
            return
        }
        val signUpRequest = SignUpRequest(id, pw)
        val call: Call<SignUpResponse> = loginService.signUp(signUpRequest)


        call.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message ?: "회원가입 성공"
                    _serverMessage.value = message
                } else {
                    val errorMessage = "${response.errorBody()?.string()}"
                    _serverMessage.value = errorMessage
                }
            }
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                t.printStackTrace()
                val errorMessage = "회원가입 실패"
                _serverMessage.value = errorMessage
            }
        })
    }


}
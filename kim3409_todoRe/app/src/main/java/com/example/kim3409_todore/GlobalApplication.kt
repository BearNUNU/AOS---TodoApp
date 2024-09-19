package com.example.kim3409_todore

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.kim3409_todore.alarm.AlarmScheduler
import com.example.kim3409_todore.alarm.AlarmService
import com.example.kim3409_todore.api.RetrofitInstance
import com.example.kim3409_todore.api.login.LoginService
import com.example.kim3409_todore.api.login.VerifyRequest
import com.example.kim3409_todore.api.login.VerifyResponse
import com.example.kim3409_todore.data.todo.TodoDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GlobalApplication : Application() {
    val database by lazy { TodoDatabase }
    private val loginService: LoginService = RetrofitInstance.getLocalRetrofitInstance().create(LoginService::class.java)
    @SuppressLint("CommitPrefEdits")
    override fun onCreate() {
        super.onCreate()
        val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkMode = pref.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        AlarmScheduler.scheduleAlarm(this)
        val serviceIntent = Intent(this, AlarmService::class.java)
        startService(serviceIntent)
        
        //JWT로 앱 실행 로그인 검증
        val sharedPreferences: SharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val jwtToken: String? = sharedPreferences.getString("jwt_token", null)

        if (!jwtToken.isNullOrEmpty()) {
            verifyToken(jwtToken)
        } else {
            Log.d("token", "토큰 없음")
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

//        schedulePeriodicWork(this)//workmanager로는 정확한 시간에 작업 하기 어렵다..?
//        1. 확실한 실행과 지연 -> workmanager
//        2. 외부 이벤트에 대한(이메일같은) 반응 -> FCM + Workmanager
//        3. 앱을 떠나도 계속해서 즉시 실행되어야 할것 -> Foreground Service
//        4. 유저 반응과, 정확한 타임에 대한 반응 - > AlarmManager


    }
    private fun verifyToken(token: String) {
        val verifyRequest = VerifyRequest(token)
        loginService.verifyJWT(verifyRequest).enqueue(object : Callback<VerifyResponse> {
            override fun onResponse(call: Call<VerifyResponse>, response: Response<VerifyResponse>) {
                if (response.isSuccessful) {
                    val verifyResponse = response.body()
                    if (verifyResponse?.message == "로그인 성공") {
                        Log.d("token", "토큰 검증 성공")
                        val intent = Intent(this@GlobalApplication, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        Log.d("token", "토큰 검증 실패: ${verifyResponse?.message}")
                        val intent = Intent(this@GlobalApplication, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                } else {
                    Log.e("token", "토큰 검증 실패: ${response.errorBody()?.string()}")
                    val intent = Intent(this@GlobalApplication, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<VerifyResponse>, t: Throwable) {
                Log.e("token", "토큰 검증 실패: ${t.message}")
                val intent = Intent(this@GlobalApplication, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
    }
}



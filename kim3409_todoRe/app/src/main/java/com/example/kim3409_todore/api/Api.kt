package com.example.kim3409_todore.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{
        val TEST_BASE_URL = "https://jsonplaceholder.typicode.com/"
        val Weather_BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"
        val LOCAL_BASE_URL = "http://localhost:3000/api/"
        fun getRetrofitInstance() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(TEST_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }
        fun getWeatherRetrofitInstance():Retrofit {
            return Retrofit.Builder()
                .baseUrl(Weather_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }
        fun getLocalRetrofitInstance(): Retrofit{
            return Retrofit.Builder()
//                .baseUrl(LOCAL_BASE_URL)
                .baseUrl("http://10.0.2.2:3000/api/")//에뮬레이터에서 로컬 서버에 연ㄹ결하는 경우에는 localhost 대신 10.0.2.2:3000을 사용, 10.0.2.2는 에뮬레이터에서 호스트 머신(PC)의 localhost를 가리킨다.
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }
    }
}
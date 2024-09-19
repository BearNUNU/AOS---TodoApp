package com.example.kim3409_todore.api.weather

import com.example.kim3409_todore.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("getUltraSrtFcst?serviceKey=${BuildConfig.WEATHER_API_KEY}")
    fun GetWeather(@Query("numOfRows") num_of_rows : Int,
                   @Query("pageNo") page_no : Int,
                   @Query("dataType") data_type : String,
                   @Query("base_date") base_date : String,
                   @Query("base_time") base_time : String,
                   @Query("nx") nx : String,
                   @Query("ny") ny : String)
            : Call<WEATHER>
}


data class WEATHER (val response : RESPONSE)
data class RESPONSE(val header : HEADER, val body : BODY)
data class HEADER(val resultCode : Int, val resultMsg : String)
data class BODY(val dataType : String, val items : ITEMS, val totalCount : Int)
data class ITEMS(val item : List<ITEM>)
// category : 자료 구분 코드, fcstDate : 예측 날짜, fcstTime : 예측 시간, fcstValue : 예보 값
data class ITEM(val category : String, val fcstDate : String, val fcstTime : String, val fcstValue : String)
package com.example.kim3409_todore.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kim3409_todore.api.RetrofitInstance
import com.example.kim3409_todore.api.weather.ITEM
import com.example.kim3409_todore.api.weather.ITEMS
import com.example.kim3409_todore.api.weather.WEATHER
import com.example.kim3409_todore.api.weather.WeatherService
import com.example.kim3409_todore.data.todo.TodoEntity
import com.example.kim3409_todore.data.todo.TodoRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {
// 만약 ViewModel이 액티비티의 context를 쓰게 되면, 액티비티가 destroy 된 경우에는 메모리 릭이 발생할 수 있다.
// 따라서 Application Context를 사용하기 위해 Applicaion을 인자로 받는다.
    private val repository = TodoRepository(application)
    private val todos = repository.getAll()
    private val _weather = MutableLiveData<String?>()
    val weather: LiveData<String?> get() = _weather


    fun getAll(): LiveData<List<TodoEntity>> {
        return todos
    }
    fun insert(todo: TodoEntity) {
        viewModelScope.launch {
            repository.insert(todo)
        }
    }

    fun update(todo: TodoEntity) {
        viewModelScope.launch {
            repository.update(todo)
        }
    }

    fun delete(todo: TodoEntity) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }

    init {
        fetchWeatherData()
    }
    private fun fetchWeatherData() {
        viewModelScope.launch {
            val weatherService: WeatherService =
                RetrofitInstance.getWeatherRetrofitInstance().create(WeatherService::class.java)
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val hourFormat = SimpleDateFormat("HH", Locale.getDefault())
            val baseDate = dateFormat.format(currentDate)
            val currentHour = hourFormat.format(currentDate)
            val baseTime = "${currentHour}00"
            val weatherCall = weatherService.GetWeather(
                num_of_rows = 1000,
                page_no = 1,
                data_type = "JSON",
                base_date = baseDate, // YYYYMMDD 형식
                base_time = baseTime,     // HHMM 형식
                nx = "55",
                ny = "127"
            )
            weatherCall.enqueue(object : Callback<WEATHER> {
                override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                    if (response.isSuccessful) {
                        val weatherData = response.body()
                        if (weatherData != null) {
                            val closestTemperature = getClosestTemperature(weatherData)
                            _weather.postValue(closestTemperature)
                        }
                        weatherData?.let {
                            Log.d("Weather", "Received weather data: $it")
                        }
                    } else {
                        Log.e("Weather", "Response failed: ${response.code()} ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                    Log.e("Weather", "Network request failed: ${t.message}")
                }
            })
        }}

//    private fun fetchWeatherData() {  // ViewModelScope를 사용했기때문에 enqueue를 사용하지 않아도 비동기 처리, 다만 이렇게 처리하려면 WeatherInterface에서 getWheather를 suspense 함수로 만들어야한다
//        viewModelScope.launch {
//            try {
//                val weatherService: WeatherService =
//                    RetrofitInstance.getWeatherRetrofitInstance().create(WeatherService::class.java)
//                val currentDate = Date()
//                val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
//                val hourFormat = SimpleDateFormat("HH", Locale.getDefault())
//                val baseDate = dateFormat.format(currentDate)
//                val currentHour = hourFormat.format(currentDate)
//                val baseTime = "${currentHour}00"
//
//                val weatherData = weatherService.getWeather(
//                    numOfRows = 1000,
//                    pageNo = 1,
//                    dataType = "JSON",
//                    baseDate = baseDate, // YYYYMMDD 형식
//                    baseTime = baseTime, // HHMM 형식
//                    nx = "55",
//                    ny = "127"
//                )
//
//                // 데이터 처리
//                val closestTemperature = getClosestTemperature(weatherData)
//                _weather.postValue(closestTemperature)
//                Log.d("Weather", "Received weather data: $weatherData")
//            } catch (e: Exception) {
//                Log.e("Weather", "Network request failed: ${e.message}")
//            }
//        }
//    }

    fun getClosestTemperature(weather: WEATHER): String? {
        val currentDate = Date()
        val hourFormat = SimpleDateFormat("HH", Locale.getDefault())
        val currentHour = hourFormat.format(currentDate).toInt()
        val t1hItems = weather.response.body.items.item.filter { it.category == "T1H" }
        var closestItem: ITEM? = null
        var minTimeDifference = Int.MAX_VALUE
        for (item in t1hItems) {
            val itemHour = item.fcstTime.substring(0, 2).toInt()
            val timeDifference = Math.abs(currentHour - itemHour)
            if (timeDifference < minTimeDifference) {
                minTimeDifference = timeDifference
                closestItem = item
            }
        }
        return closestItem?.fcstValue
    }
}
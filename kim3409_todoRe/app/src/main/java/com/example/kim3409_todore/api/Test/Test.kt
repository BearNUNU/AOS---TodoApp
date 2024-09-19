package com.example.kim3409_todore.api.weather.Test

import retrofit2.Response
import retrofit2.http.GET

public interface TestService  {
    @GET("/todos")
    suspend fun getTodos() : Response<TestX>
}
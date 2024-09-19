package com.example.kim3409_todore.api.weather.Test


import com.google.gson.annotations.SerializedName

data class TestItem(
    @SerializedName("completed")
    val completed: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("userId")
    val userId: Int
)
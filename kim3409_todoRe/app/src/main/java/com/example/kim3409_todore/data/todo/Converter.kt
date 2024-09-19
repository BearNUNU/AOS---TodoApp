package com.example.kim3409_todore.data.todo

import com.google.gson.Gson
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.Date

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
    @TypeConverter
    fun fromNotificationsList(notifications: List<Boolean>?): String? {
        return notifications?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toNotificationsList(notificationsString: String?): List<Boolean>? {
        return notificationsString?.let {
            val listType = object : TypeToken<List<Boolean>>() {}.type
            gson.fromJson(it, listType)
        }
    }
    @TypeConverter
    fun fromFile(file: File?): String? {
        return file?.absolutePath
    }

    @TypeConverter
    fun toFile(path: String?): File? {
        return path?.let { File(it) }
    }
}

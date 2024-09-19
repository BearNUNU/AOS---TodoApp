package com.example.kim3409_todore.data.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
import java.util.Date

@Entity(tableName = "todoTable")
data class TodoEntity (
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String? = null,
    @ColumnInfo(name = "dueDate") var dueDate: Date? = null,
//    @ColumnInfo(name = "category") val category: String? = null,
    @ColumnInfo(name = "isCompleted") var isCompleted: Boolean = false,
    @ColumnInfo(name = "alarmDate") var alarmDate: Date? = null,
    @ColumnInfo(name = "imageFile") val imageFile: File? = null
)
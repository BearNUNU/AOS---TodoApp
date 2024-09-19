package com.example.kim3409_todore.data.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TodoEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var instance: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo-database"

                )
                    .build().also { instance = it }
            }
        }
    }
}

//인스턴스가 null인지 판단, nullㅇㅣ면 동기화 블록에서 인스턴스를 생성, context.applicationContext 애플리케이션 컨텍스트를 사용
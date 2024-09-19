package com.example.kim3409_todore.data.todo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity): Long
    //onConflict = OnConflictStrategy.REPLACE는 기본 키가 충돌할 경우 기존 항목을 교체
    @Update
    suspend fun update(todo: TodoEntity)

    @Delete
    suspend fun delete(todo: TodoEntity)

    @Query("SELECT * FROM todoTable")
    fun getAllTodos(): LiveData<List<TodoEntity>>

    @Query("SELECT * FROM todoTable")
    suspend fun getAllTodosSuspend(): List<TodoEntity>

    
//    @Query("SELECT * FROM todoTable ORDER BY dueDate ASC")
//    fun getAllTodosAsc(): LiveData<List<TodoEntity>>
//
//    @Query("SELECT * FROM todoTable ORDER BY dueDate DESC")
//    fun getAllTodosDESC(): LiveData<List<TodoEntity>>
//
    @Query("SELECT * FROM todoTable WHERE id = :id LIMIT 1")
    suspend fun getTodoById(id: Long): TodoEntity?

//    @Query("SELECT * FROM todoTable WHERE title LIKE :searchQuery")
//    fun searchTodosByTitle(searchQuery: String): LiveData<List<TodoEntity>>
//
//    @Query("DELETE FROM todoTable")
//    suspend fun deleteAllTodos()
}
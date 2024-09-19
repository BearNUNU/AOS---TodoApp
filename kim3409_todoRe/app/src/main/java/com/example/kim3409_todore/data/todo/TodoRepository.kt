package com.example.kim3409_todore.data.todo


import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import java.util.Date

class TodoRepository(application: Application){
    private val todoDatabase = TodoDatabase.getInstance(application)
    private val todoDao:TodoDao = todoDatabase.todoDao()
    private val todos: LiveData<List<TodoEntity>> = todoDao.getAllTodos()


    fun getAll(): LiveData<List<TodoEntity>> {
        return todos
    }
    //    val allTodosAsc: LiveData<List<TodoEntity>> = todoDao.getAllTodosAsc()
//    val allTodosDesc: LiveData<List<TodoEntity>> = todoDao.getAllTodosDESC()
    suspend fun insert(todo: TodoEntity) {
        todoDao.insert(todo)
    }
    suspend fun update(todo: TodoEntity) {
        todoDao.update(todo)
    }
    suspend fun delete(todo: TodoEntity) {
        todoDao.delete(todo)
    }

    suspend fun getTodoById(id: Long): TodoEntity? {
        return todoDao.getTodoById(id)
    }
    suspend fun updateTodoById(id: Long, newTitle: String, newDescription: String, newDueDate: Date?, newAlarmDate: Date?) {
        val todo = todoDao.getTodoById(id)
        todo?.let {
            it.title = newTitle
            it.description = newDescription
            it.dueDate =newDueDate
            it.alarmDate = newAlarmDate
            todoDao.update(it)
        }
    }

    suspend fun getAllTodos(): List<TodoEntity> {
        return todoDao.getAllTodosSuspend()
    }
//
//    fun searchTodosByTitle(searchQuery: String): LiveData<List<TodoEntity>> {
//        return todoDao.searchTodosByTitle(searchQuery)
//    }
//
//    suspend fun deleteAllTodos() {
//        todoDao.deleteAllTodos()
//    }
}

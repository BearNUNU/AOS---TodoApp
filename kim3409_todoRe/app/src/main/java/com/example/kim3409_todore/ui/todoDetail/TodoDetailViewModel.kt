package com.example.kim3409_todore.ui.todoDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kim3409_todore.data.todo.TodoEntity
import com.example.kim3409_todore.data.todo.TodoRepository
import kotlinx.coroutines.launch
import java.util.Date

class TodoDetailViewModel(application: Application, val id: Long, val isEdit: Boolean) : AndroidViewModel(application) {
    private val repository = TodoRepository(application)
    private val _todo = MutableLiveData<TodoEntity>()
    private val _isEditMode = isEdit
    val todo: LiveData<TodoEntity> get() = _todo
    val isEditMode = _isEditMode

    init {
        fetchTodoById(id)

    }

    private fun fetchTodoById(id: Long) { //코루틴에 대해서 더 공부 필요 getTodoById가 suspense이기때문에
        viewModelScope.launch {
            _todo.value = repository.getTodoById(id)
        }
    }

    fun updateTodoById(newTitle: String, newDescription: String, newDueDate: Date?, newAlarmDate: Date?) {
        viewModelScope.launch {
            repository.updateTodoById(id, newTitle, newDescription, newDueDate, newAlarmDate)
        }
    }
    fun setTodo(newTodo: TodoEntity) {
        _todo.value = newTodo
    }

    companion object {
        fun provideFactory(application: Application, id: Long, isEdit: Boolean): Factory {
            return Factory(application, id, isEdit)
        }
    }
    class Factory(
        private val application: Application,
        private val id: Long,
        private val isEdit: Boolean,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TodoDetailViewModel::class.java)) {
                return TodoDetailViewModel(application, id, isEdit) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
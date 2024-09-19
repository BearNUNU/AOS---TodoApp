package com.example.kim3409_todore.ui.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kim3409_todore.data.todo.TodoEntity
import com.example.kim3409_todore.data.todo.TodoRepository
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

class EditViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TodoRepository(application)

    private val _todoTitle = MutableLiveData<String>()
    val todoTitle: LiveData<String> get() = _todoTitle
    private val _todoDescription = MutableLiveData<String>()
    val todoDescription: LiveData<String> get() = _todoDescription
    private val _todoDueDate = MutableLiveData<Date>()
    val todoDueDate: LiveData<Date> get() = _todoDueDate
    private val _alarmDate = MutableLiveData<Date>()
    val alarmDate: LiveData<Date> get() = _alarmDate
    private val _imageFile = MutableLiveData<File>()
    val imageFile: LiveData<File> get() = _imageFile


    fun setTodoDueDate(date: Date) {
        _todoDueDate.value = date
    }

    fun setAlarmDate(date: Date) {
        _alarmDate.value = date
    }

    fun setTodoTitle(title: String) {
        _todoTitle.value = title
    }

    fun setTodoDescription(description: String) {
        _todoDescription.value = description
    }

    fun setImageFile(file: File) {
        _imageFile.value = file
    }

    fun insertTodo(onComplete: (Boolean) -> Unit) {
        val title = _todoTitle.value ?: ""
        val description = _todoDescription.value ?: ""
        val dueDate = _todoDueDate.value
        val alarmDate = _alarmDate.value
        val image = _imageFile.value
        if (title.isNotEmpty()) {
            val todo = TodoEntity(
                id = 0,
                title = title,
                description = description,
                dueDate = dueDate,
                alarmDate = alarmDate,
                isCompleted = false,
                imageFile = image
            )

            viewModelScope.launch {
                repository.insert(todo)
                onComplete(true)
            }
        } else {
            onComplete(false)
        }
    }
}


package com.example.kim3409_todore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.kim3409_todore.databinding.ActivityTododetailBinding
import com.example.kim3409_todore.ui.todoDetail.TodoDetailViewModel

class TodoDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTododetailBinding
    private lateinit var viewModel: TodoDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTododetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getLongExtra("id", 0)
        val isEdit = intent.getBooleanExtra("isEdit", false)

        // Use the companion object to create the factory
        viewModel = ViewModelProvider(
            this,
            TodoDetailViewModel.provideFactory(application, id, isEdit)
        ).get(TodoDetailViewModel::class.java)
    }
}
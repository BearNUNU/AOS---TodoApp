package com.example.kim3409_todore.ui.todoDetail

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kim3409_todore.databinding.FragmentTododetailBinding
import com.example.kim3409_todore.ui.bottomDialog.BottomDialogFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodoDetailFragment: Fragment() {
    private var _binding: FragmentTododetailBinding? = null
    private lateinit var todoDetailViewModel: TodoDetailViewModel
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTododetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        todoDetailViewModel = ViewModelProvider(requireActivity()).get(TodoDetailViewModel::class.java)


        val isEditMode = todoDetailViewModel.isEditMode
        binding.todoDetailTitle.isEnabled = isEditMode
        binding.todoDetailContent.isEnabled = isEditMode
        binding.todoDetailConfirmBtn.isVisible = isEditMode
        binding.todoDetailCancelBtn.isVisible = isEditMode


        //viewModel의 fetchTodoById는 코루틴을 사용하여 비동기적으로 데이터를 로드하기 때문에 로드 되기 전에 fragment에서 todo에 접근 가능성이 있음 그래서 observe를 사용해야함
        todoDetailViewModel.todo.observe(viewLifecycleOwner) { todo ->
            todo?.let {
                binding.todoDetailTitle.setText(it.title)
                binding.todoDetailContent.setText(it.description)
                if(isEditMode){binding.dueDateBtn.visibility = View.VISIBLE}
                if(todo.dueDate  != null) {
                    if(isEditMode){binding.alarmBtn.visibility = View.VISIBLE}
                    binding.dueDateText.visibility = View.VISIBLE
                    binding.dueDateText.setText("마감일:${formatDate(todo.dueDate!!)}" )
                }
                if(todo.alarmDate != null) {
                    binding.alarmText.visibility =View.VISIBLE
                    binding.alarmText.setText("알림일:${formatDate(todo.alarmDate!!)}")
                }
                if(todo.imageFile != null){
                    val bitmap = BitmapFactory.decodeFile(todo.imageFile.absolutePath)
                    binding.imageView.setImageBitmap(bitmap)
                    binding.imageView.visibility =View.VISIBLE

                }

            }
        }

        binding.todoDetailConfirmBtn.setOnClickListener {
            val newTitle = binding.todoDetailTitle.text.toString()
            val newDescription = binding.todoDetailContent.text.toString()
            val newDueDate = todoDetailViewModel.todo.value?.dueDate
            val newAlarmDate = todoDetailViewModel.todo.value?.alarmDate
            todoDetailViewModel.updateTodoById(newTitle, newDescription, newDueDate, newAlarmDate)
            requireActivity().finish()
        }

        binding.todoDetailCancelBtn.setOnClickListener {
            requireActivity().finish()
        }
        binding.dueDateBtn.setOnClickListener {
            BottomDialogFragment(dateType = BottomDialogFragment.DateType.DUE_DATE)
                .show(childFragmentManager, "DUE_DATE_DIALOG")
        }

        binding.alarmBtn.setOnClickListener {
            BottomDialogFragment(dateType = BottomDialogFragment.DateType.ALARM_DATE)
                .show(childFragmentManager, "ALARM_DATE_DIALOG")
        }

        return root
    }

    fun onDateTimeSelected(dateType: BottomDialogFragment.DateType, date: Date) {
        when (dateType) {
            BottomDialogFragment.DateType.DUE_DATE -> {
                todoDetailViewModel.todo.value?.let { todo ->
                    val updatedTodo = todo.copy(dueDate = date)
                    todoDetailViewModel.setTodo(updatedTodo)
                }
            }
            BottomDialogFragment.DateType.ALARM_DATE -> {
                todoDetailViewModel.todo.value?.let { todo ->
                    val updatedTodo = todo.copy(alarmDate = date)
                    todoDetailViewModel.setTodo(updatedTodo)
                }
            }
        }
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return dateFormat.format(date)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
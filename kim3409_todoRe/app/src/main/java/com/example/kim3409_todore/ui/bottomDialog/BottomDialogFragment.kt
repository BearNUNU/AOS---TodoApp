package com.example.kim3409_todore.ui.bottomDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kim3409_todore.databinding.FragementBottomDateBinding
import com.example.kim3409_todore.ui.edit.EditFragment
import com.example.kim3409_todore.ui.todoDetail.TodoDetailFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar
import java.util.Date

class BottomDialogFragment(private val dateType: DateType)  : BottomSheetDialogFragment() {
    enum class DateType {
        DUE_DATE,
        ALARM_DATE
    }
    private var _binding: FragementBottomDateBinding? = null
    private val binding get() = _binding!!
    private var step : Int = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragementBottomDateBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener {
            if (showsDialog&& step ==2) {
                step = 1
                binding.bottomLayoutTitle.setText("날짜 선택")
                binding.cancelBtn.setText("취소")
                binding.datePicker.visibility = View.VISIBLE
                binding.timePicker.visibility = View.GONE
            }
            else if(showsDialog&& step ==1){
                step = 1
                dismiss()
            }
        }
        binding.confirmBtn.setOnClickListener {
            if (showsDialog&& step ==2) {
                val selectedDate = getDateFromPickers()
                (parentFragment as? EditFragment)?.onDateTimeSelected(dateType, selectedDate)
                (parentFragment as? TodoDetailFragment)?.onDateTimeSelected(dateType, selectedDate)
                dismiss()
            }
            else if(showsDialog&& step ==1){
                step = 2
                binding.bottomLayoutTitle.setText("시간 선택")
                binding.cancelBtn.setText("뒤로")
                binding.datePicker.visibility = View.GONE
                binding.timePicker.visibility = View.VISIBLE
            }
        }

        binding.bottomLayoutCancelBtn.setOnClickListener {
            if (showsDialog) {
                dismiss()
            }
        }


        return binding.root
    }

    private fun getDateFromPickers(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(
            binding.datePicker.year,
            binding.datePicker.month,
            binding.datePicker.dayOfMonth,
            binding.timePicker.hour,
            binding.timePicker.minute
        )
        return calendar.time
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

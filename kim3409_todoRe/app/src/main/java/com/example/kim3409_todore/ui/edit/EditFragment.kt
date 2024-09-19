package com.example.kim3409_todore.ui.edit

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kim3409_todore.databinding.FragmentEditBinding
import com.example.kim3409_todore.ui.bottomDialog.BottomDialogFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private lateinit var editViewModel: EditViewModel
    private var imageFile = File("")
    private val binding get() = _binding!!

    private val galleryPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openImagePicker()
            } else {
                showPermissionDeniedDialog()
            }
        }

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    val realPath = getRealPathFromUri(it)
                    if (realPath != null) {
                        imageFile = File(realPath)
                        Log.d("detail", imageFile.toString())
                    } else {
                        Log.d("detail", "Invalid file path")
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        editViewModel = ViewModelProvider(this).get(EditViewModel::class.java)
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.titleInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                editViewModel.setTodoTitle(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.contentInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                editViewModel.setTodoDescription(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.apply {
            confirmBtn.setOnClickListener {
                editViewModel.setImageFile(imageFile)
                editViewModel.insertTodo { isSuccess ->
                    if (isSuccess && titleInput.text.isNotEmpty()) {
                        parentFragmentManager.popBackStack()
                    } else {
                        Toast.makeText(
                            context,
                            "제목을 입력하세요",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        editViewModel.todoDueDate.observe(viewLifecycleOwner) { date ->
            if (date != null) {
                binding.dueDateText.visibility = View.VISIBLE
                binding.dueDateText.text = "마감일: ${formatDate(date)}"
                binding.alarmBtn.visibility = View.VISIBLE
            }
        }

        editViewModel.alarmDate.observe(viewLifecycleOwner) { date ->
            if (date != null) {
                binding.alarmText.visibility = View.VISIBLE
                binding.alarmText.text = "알림일: ${formatDate(date)}"
            }
        }

        binding.dueDateBtn.setOnClickListener {
            BottomDialogFragment(dateType = BottomDialogFragment.DateType.DUE_DATE)
                .show(childFragmentManager, "DUE_DATE_DIALOG")
        }

        binding.alarmBtn.setOnClickListener {
            BottomDialogFragment(dateType = BottomDialogFragment.DateType.ALARM_DATE)
                .show(childFragmentManager, "ALARM_DATE_DIALOG")
        }

        binding.addPicBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        return root
    }

    private fun openImagePicker() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(MediaStore.ACTION_PICK_IMAGES).apply {
                type = "image/*"
                putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, 1)
            }
        } else {
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            }
        }
        imageLauncher.launch(intent)
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("앱의 정상적인 작동을 위해 사진 접근 권한이 필요합니다. 권한을 허용해 주세요.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireContext().packageName, null)
                }
                startActivity(intent)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun getRealPathFromUri(uri: Uri): String? {
        var result: String? = null
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            if (idx >= 0) {
                result = cursor.getString(idx)
            }
            cursor.close()
        }
        return result
    }

    fun onDateTimeSelected(dateType: BottomDialogFragment.DateType, date: Date) {
        when (dateType) {
            BottomDialogFragment.DateType.DUE_DATE -> editViewModel.setTodoDueDate(date)
            BottomDialogFragment.DateType.ALARM_DATE -> editViewModel.setAlarmDate(date)
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
package com.example.kim3409_todore.ui.setting

import SettingViewModel
import SettingViewModelFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kim3409_todore.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val settingsViewModel: SettingViewModel by viewModels {
            SettingViewModelFactory(pref)
        }
        settingsViewModel.isDarkMode.observe(viewLifecycleOwner, Observer { isDarkMode ->
            val mode = if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(mode)
            binding.themeswitch.isChecked = isDarkMode
        })


        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.themeswitch.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.setDarkMode(isChecked)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
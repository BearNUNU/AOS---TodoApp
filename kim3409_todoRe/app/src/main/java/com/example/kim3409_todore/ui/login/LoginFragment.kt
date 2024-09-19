package com.example.kim3409_todore.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kim3409_todore.MainActivity
import com.example.kim3409_todore.databinding.FragmentLoginBinding
import com.example.kim3409_todore.ui.home.HomeViewModel
import com.example.kim3409_todore.ui.todoDetail.TodoDetailViewModel
import kotlin.math.log

class LoginFragment: Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.btnLogin.setOnClickListener {
            loginViewModel.setId(binding.idInput.text.toString())
            loginViewModel.setPw(binding.passwordInput.text.toString())
            loginViewModel.login()
        }
        binding.btnSignup.setOnClickListener {
            loginViewModel.setId(binding.idInput.text.toString())
            loginViewModel.setPw(binding.passwordInput.text.toString())
            loginViewModel.signUp()
        }


        loginViewModel.loginStatus.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else {
            }
        }
        loginViewModel.serverMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
        loginViewModel.token.observe(viewLifecycleOwner) { token ->
            if (token != null) {
                val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.remove("jwt_token")
                editor.putString("jwt_token", token)
                editor.apply()
                Log.d("token", "$token")
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
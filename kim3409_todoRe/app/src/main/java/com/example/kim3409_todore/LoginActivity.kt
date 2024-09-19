package com.example.kim3409_todore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kim3409_todore.databinding.ActivityLoginBinding
import com.example.kim3409_todore.databinding.ActivityMainBinding
import com.example.kim3409_todore.ui.login.LoginFragment

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction() // 단일 프래그먼트 navgraph없이 사용
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
}
}

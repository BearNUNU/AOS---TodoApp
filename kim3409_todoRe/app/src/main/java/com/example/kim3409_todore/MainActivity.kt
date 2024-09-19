package com.example.kim3409_todore

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.kim3409_todore.alarm.AlarmService
import com.example.kim3409_todore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var initTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        //FragmentContainerView를 사용하면 기존처럼    val navController = findNavController(R.id.nav_host_fragment_content_main) 이런 식으로 navController 생성이 불가능 계속 못 찾는다는 오류가 발생한다.
        // 위와같은 방법으로 navController를 생성해야함

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_setting //여기에서 빠지면 햄버거메뉴가 아니라 뒤로가기가 appbar에 표시
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentDestination = navController.currentBackStackEntry?.destination?.id
                if (currentDestination == R.id.nav_home) {
                    if (System.currentTimeMillis() - initTime > 3000) {
                        Toast.makeText(this@MainActivity, "앱 종료를 하려면 한 번 더 누르세요", Toast.LENGTH_SHORT).show()
                        initTime = System.currentTimeMillis()
                    } else {
                        finishAffinity()
                    }
                } else {
                    navController.navigate(R.id.nav_home)
                    initTime = 0L
                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
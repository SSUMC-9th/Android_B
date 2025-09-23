package com.example.a4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity // 변경됨: ComponentActivity -> AppCompatActivity
import com.example.a4.databinding.ActivityMainBinding // View Binding 클래스 임포트 (프로젝트에 맞게 패키지 확인 필요)
// HomeFragment, SearchFragment 등 Fragment 임포트 필요

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) // 변경됨: Compose 대신 View Binding 사용

        setBottomNavigationView()

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_home // R.id.fragment_home 등이 menu XML에 정의되어 있어야 함
        }
    }
    
    fun setBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_home -> {
                    // supportFragmentManager.beginTransaction().replace(R.id.main_container, HomeFragment()).commit() // HomeFragment() 등이 정의되어 있어야 함
                    true
                }
                R.id.fragment_search -> {
                    // supportFragmentManager.beginTransaction().replace(R.id.main_container, SearchFragment()).commit()
                    true
                }
                R.id.fragment_favorite -> {
                    // supportFragmentManager.beginTransaction().replace(R.id.main_container, FavoriteFragment()).commit()
                    true
                }
                R.id.fragment_settings -> {
                    // supportFragmentManager.beginTransaction().replace(R.id.main_container, SettingsFragment()).commit()
                    true
                }
                else -> false
            }
        }
    }
}

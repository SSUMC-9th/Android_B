package com.example.flo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication6.R
import com.example.myapplication6.databinding.ActivityMainBinding

class    MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // main_frm 컨테이너에 HomeFragment를 로드합니다.
        // savedInstanceState == null 조건은 Activity가 처음 생성될 때만 Fragment를 추가하도록 합니다.
        // (화면 회전 등으로 Activity가 재생성될 때는 Fragment가 자동으로 복원됩니다.)
        if (savedInstanceState == null) {
            // R.id.main_frm이 activity_main.xml에 정의된 FrameLayout의 ID라고 가정합니다.
            // com.example.real.HomeFragment()를 사용하여 HomeFragment의 인스턴스를 생성합니다.
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitNow()
        }

        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
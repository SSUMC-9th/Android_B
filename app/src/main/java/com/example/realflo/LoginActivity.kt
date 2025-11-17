package com.example.realflo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.example.realflo.databinding.ActivityLoginBinding
import data.local.FloDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: FloDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FloDatabase.getInstance(this)

        // 로그인 버튼
        binding.loginSignInBtn.setOnClickListener {
            login()
        }

        // 회원가입 이동
        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun login() {
        val email = binding.loginIdEt.text.toString()
        val password = binding.loginPasswordEt.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            // DB에서 로그인 시도
            val user = withContext(Dispatchers.IO) {
                db.userDao().login(email, password)
            }

            if (user == null) {
                Toast.makeText(
                    this@LoginActivity,
                    "이메일 또는 비밀번호가 틀렸습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }

            // 로그인 성공 → sharedPreferences 저장
            val sp = getSharedPreferences("user", MODE_PRIVATE)
            sp.edit {
                putInt("userId", user.id)
                putString("userName", user.name)
                putString("userEmail", user.email)
            }

            Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()

            // 메인 화면으로 이동
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}

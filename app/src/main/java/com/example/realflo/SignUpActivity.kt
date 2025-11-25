package com.example.realflo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.realflo.databinding.ActivitySignupBinding
import data.local.FloDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var db: FloDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FloDatabase.getInstance(this)

        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        val email = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val password = binding.signUpPasswordEt.text.toString()
        val name = binding.signUpNameEt.text.toString()

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val db = this@SignUpActivity.db ?: return@launch

            // 이메일 중복 체크
            val existUser = withContext(Dispatchers.IO) {
                db.userDao().getUserByEmail(email)
            }

            if (existUser != null) {
                Toast.makeText(this@SignUpActivity, "이미 존재하는 이메일입니다", Toast.LENGTH_SHORT).show()
                return@launch
            }

            // 새 유저 생성
            val newUser = User(email = email, password = password, name = name)

            withContext(Dispatchers.IO) {
                db.userDao().insert(newUser)
            }

            Toast.makeText(this@SignUpActivity, "회원가입 성공!", Toast.LENGTH_SHORT).show()

            // 로그인 화면으로 이동
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

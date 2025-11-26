package com.example.umctest1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.umctest1.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var binding: ActivityLoginBinding
    private val authService = AuthService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authService.setLoginView(this)

        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginSignInBtn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val idPart = binding.loginIdEt.text.toString()
        val domainPart = binding.loginDirectInputEt.text.toString()
        val password = binding.loginPasswordEt.text.toString()

        if (idPart.isEmpty() || domainPart.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email = "$idPart@$domainPart"

        Log.d("LOGIN", "email=$email, password=$password")

        val request = LoginRequest(
            email = email,
            password = password
        )

        authService.login(request)
    }

    private fun saveJwt(accessToken: String) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("accessToken", accessToken)
        editor.apply()
    }

    override fun onLoginSuccess(code: String, result: LoginData) {
        if (code == "COMMON200_1") {
            saveJwt(result.accessToken)

            Toast.makeText(this, "로그인 성공: ${result.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLoginFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
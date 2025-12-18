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
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.auth.model.Prompt
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var binding: ActivityLoginBinding
    private val authService = AuthService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("KakaoInit", "KeyHash: ${com.kakao.sdk.common.util.Utility.getKeyHash(this)}")

        authService.setLoginView(this)

        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginSignInBtn.setOnClickListener {
            login()
        }

        binding.loginKakakoLoginIv.setOnClickListener{
            startKakaoLogin()
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

    private fun startKakaoLogin() {
        // 카카오계정으로 로그인 공통 콜백 함수
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("KakaoLogin", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i("KakaoLogin", "카카오계정으로 로그인 성공 ${token.accessToken}")
                fetchUserInfo() // 로그인 성공 시 정보 가져오기
            }
        }

        // 2. 카카오톡 설치 여부에 따른 로그인 처리
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("KakaoLogin", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 로그인을 취소한 경우 앱 종료 방지
                    if (error is com.kakao.sdk.common.model.ClientError &&
                        error.reason == com.kakao.sdk.common.model.ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 계정이 없는 경우 등 에러 발생 시 계정 로그인을 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i("KakaoLogin", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    fetchUserInfo()
                }
            }
        } else {
            // 카카오톡이 없으면 웹 브라우저를 통한 계정 로그인
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }

//        UserApiClient.instance.loginWithKakaoAccount(
//            context = this,
//            prompts = listOf(Prompt.LOGIN),
//            callback = callback
//        )
    }

    // 3. 사용자 정보 가져오기 (닉네임, 이메일, 프로필 사진)
    private fun fetchUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("KakaoLogin", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                // 로그에서 정보 확인
                val nickname = user.kakaoAccount?.profile?.nickname
                val email = user.kakaoAccount?.email
                val profileUrl = user.kakaoAccount?.profile?.thumbnailImageUrl

                Log.d("KakaoLogin", "사용자 정보: 닉네임=$nickname, 이메일=$email, 사진=$profileUrl")

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
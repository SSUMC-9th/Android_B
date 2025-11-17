package com.keder.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.keder.flo.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private  lateinit var binding : ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupSignupBtn.setOnClickListener {
            signUp()
            finish()
        }
    }

    private fun getUser() : User{
        val email : String = binding.signupIdEt.text.toString() + "@" + binding.signupInputIdEt.text.toString()
        val pwd : String = binding.signupPasswordEt.text.toString()

        return User(email, pwd)
    }

    private fun signUp(){
        if(binding.signupIdEt.text.toString().isEmpty() ||  binding.signupInputIdEt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signupPasswordEt.text.toString() != binding.signupPwCheckEt.text.toString()){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val user = userDB.userDao().getUsers()
        Log.d("SIGNUPACT", user.toString())
    }
}
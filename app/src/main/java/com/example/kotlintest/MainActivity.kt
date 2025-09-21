package com.example.kotlintest

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlintest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivHappy.setOnClickListener{
            binding.tv1.setTextColor(
                ContextCompat.getColor(binding.root.context, R.color.yello)
            )
            showToast(binding.tv1.text.toString())
        }

        binding.ivJoy.setOnClickListener{
            binding.tv2.setTextColor(
                ContextCompat.getColor(binding.root.context, R.color.blue)
            )
            showToast(binding.tv2.text.toString())
        }

        binding.ivSoso.setOnClickListener{
            binding.tv3.setTextColor(
                ContextCompat.getColor(binding.root.context, R.color.violet)
            )
            showToast(binding.tv3.text.toString())
        }

        binding.ivNervous.setOnClickListener{
            binding.tv4.setTextColor(
                ContextCompat.getColor(binding.root.context, R.color.green)
            )
            showToast(binding.tv4.text.toString())
        }

        binding.ivAngry.setOnClickListener{
            binding.tv5.setTextColor(
                ContextCompat.getColor(binding.root.context, R.color.red)
            )
            showToast(binding.tv5.text.toString())
        }

        binding.tvLaunch.setOnClickListener{
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }
}
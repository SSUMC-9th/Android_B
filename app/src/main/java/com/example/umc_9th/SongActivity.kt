package com.example.umc_9th

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.umc_9th.R
import com.example.umc_9th.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("title")) {
            val title = intent.getStringExtra("title")

            Toast.makeText(this, "전달받은 제목: $title", Toast.LENGTH_SHORT).show()
        }

        binding.ivArrow.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("message", "SongActivity에서 결과가 도착했습니다.")

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
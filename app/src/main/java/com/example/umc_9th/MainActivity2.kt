package com.example.umc_9th

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.umc_9th.R
import com.example.umc_9th.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var songActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val message = data?.getStringExtra("message")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.playerLayout.ivMiniplayer.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)

            val title = binding.playerLayout.miniPlayerTitle.text.toString()

            intent.putExtra("title", title)

            songActivityLauncher.launch(intent)
        }

    }
}
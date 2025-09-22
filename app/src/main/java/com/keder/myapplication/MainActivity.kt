package com.keder.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.keder.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupIntentToggle(binding.textYello, R.string.yello_text)
        setupIntentToggle(binding.textBlue, R.string.blue_text)
        setupIntentToggle(binding.textPurple, R.string.purple_text)
        setupIntentToggle(binding.textGreen, R.string.green_text)
        setupIntentToggle(binding.textRed, R.string.red_text)


        setupColorToggle(binding.imageYello, binding.textYello, R.color.yellow, R.string.yello_text)
        setupColorToggle(binding.imageBlue, binding.textBlue, R.color.blue, R.string.blue_text)
        setupColorToggle(
            binding.imagePurple,
            binding.textPurple,
            R.color.pruple,
            R.string.purple_text
        )
        setupColorToggle(binding.imageGreen, binding.textGreen, R.color.green, R.string.green_text)
        setupColorToggle(binding.imageRed, binding.textRed, R.color.red, R.string.red_text)

    }

    private fun setupColorToggle(
        imageView : android.view.View,
        textView : TextView,
        colorRes : Int,
        messageRes : Int
    ){
        var isChanged = false
        imageView.setOnClickListener {
            val newColor = if(isChanged){
                ContextCompat.getColor(this, R.color.black)
            }else{
                Toast.makeText(this, getString(messageRes), Toast.LENGTH_SHORT).show()
                ContextCompat.getColor(this, colorRes)
            }
            textView.setTextColor(newColor)
            isChanged = !isChanged
        }
    }

        private fun setupIntentToggle(
            textView: TextView,
            messageRes: Int
        ){
            textView.setOnClickListener {
                val intent = Intent(this, SubActivity::class.java).apply{
                    putExtra("content", getString(messageRes))
                }
                startActivity(intent)
            }
        }
}
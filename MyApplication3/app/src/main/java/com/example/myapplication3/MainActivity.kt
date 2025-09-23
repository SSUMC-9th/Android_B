package com.example.myapplication3

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast // Toast를 사용하기 위한 import
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {

    private lateinit var textViews: List<TextView>
    private lateinit var imageViews: List<ImageView>

    private val selectedColor = Color.BLUE
    private val defaultColor = Color.BLACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val ivHappy = findViewById<ImageView>(R.id.iv_happy)
        val tvHappy = findViewById<TextView>(R.id.tv1)

        val ivJoy = findViewById<ImageView>(R.id.iv_joy)
        val tvJoy = findViewById<TextView>(R.id.tv2)

        val ivSoso = findViewById<ImageView>(R.id.iv_soso)
        val tvSoso = findViewById<TextView>(R.id.tv3)

        val ivNervous = findViewById<ImageView>(R.id.iv_nervous)
        val tvNervous = findViewById<TextView>(R.id.tv4)

        val ivAngry = findViewById<ImageView>(R.id.iv_angry)
        val tvAngry = findViewById<TextView>(R.id.tv5)

        textViews = listOf(tvHappy, tvJoy, tvSoso, tvNervous, tvAngry)
        imageViews = listOf(ivHappy, ivJoy, ivSoso, ivNervous, ivAngry)

        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                resetTextViewColors()
                val selectedTextView = textViews[index]
                selectedTextView.setTextColor(selectedColor)

                // 토스트 메시지 추가
                val toastMessage = "${selectedTextView.text} 선택됨!"
                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetTextViewColors() {
        textViews.forEach { textView ->
            textView.setTextColor(defaultColor)
        }
    }
}
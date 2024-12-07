package com.example.DashBoard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playButton: Button = findViewById(R.id.playbutton)
        playButton.setOnClickListener {
            try {
                Log.d(TAG, "Play button clicked. Starting gameplayss.")
                val intent = Intent(this, gameplayss::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Error starting gameplayss", e)
            }
        }
    }
}
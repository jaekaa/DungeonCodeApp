package com.example.DashBoard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class LandscapeMode : AppCompatActivity() {
    private val TAG = "LandscapeMode"
    private var currentLayout = 0
    private var lives = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landscape)

        // Load progress from SharedPreferences
        loadProgress()

        // Handle Play button click
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

        // Handle Button2 click (SalvyActivity)
        val button2: Button = findViewById(R.id.button2)
        button2.setOnClickListener {
            try {
                Log.d(TAG, "Button2 clicked. Starting SalvyActivity.")
                val intent = Intent(this, SalvyActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Error starting SalvyActivity", e)
            }
        }

        // Handle Button3 click (ChameleonActivity)
        val button3: Button = findViewById(R.id.button3)
        button3.setOnClickListener {
            try {
                Log.d(TAG, "Button3 clicked. Starting ChameleonActivity.")
                val intent = Intent(this, ChameleonActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Error starting ChameleonActivity", e)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Check if the user is in LandscapeMode, and if not, show the resume dialog
        if (this !is LandscapeMode && currentLayout != 0) {
            showResumeDialog()
        }
    }

    override fun onStop() {
        super.onStop()
        saveProgress()
    }

    private fun showResumeDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to resume from where you left off?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                // Load the saved progress and navigate to the layout
                loadProgress()
                navigateToLayout(currentLayout)
            }
            .setNegativeButton("No") { _, _ ->
                // Restart the game, reset progress
                restartGame()
            }

        val alert = builder.create()
        alert.show()
    }

    private fun restartGame() {
        // Restart the game from the first layout
        currentLayout = R.layout.salvy_story
        lives = 3
        saveProgress() // Save the reset progress
        navigateToLayout(currentLayout)
    }

    private fun saveProgress() {
        val sharedPreferences = getSharedPreferences("UserProgress", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("SalvyActivityLayout", currentLayout)
        editor.putInt("SalvyLives", lives)
        editor.apply()
        Log.d(TAG, "Progress saved: Layout=$currentLayout, Lives=$lives")
    }

    private fun loadProgress() {
        val sharedPreferences = getSharedPreferences("UserProgress", MODE_PRIVATE)
        currentLayout = sharedPreferences.getInt("SalvyActivityLayout", R.layout.salvy_story)  // Default to starting layout
        lives = sharedPreferences.getInt("SalvyLives", 3)
        Log.d(TAG, "Progress loaded: Layout=$currentLayout, Lives=$lives")
    }

    private fun navigateToLayout(layoutId: Int) {
        // Start the activity for the appropriate layout
        when (layoutId) {
            R.layout.salvy_story -> {
                val intent = Intent(this, SalvyActivity::class.java)
                startActivity(intent)
            }
            // Add other layout handling cases as needed
            else -> {
                Log.e(TAG, "Unknown layout to navigate: $layoutId")
            }
        }
    }
}

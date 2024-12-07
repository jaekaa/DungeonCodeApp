package com.example.DashBoard

import android.content.Intent
import android.os.Bundle
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.app.AlertDialog
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SalvyActivity : AppCompatActivity() {
    private val TAG = "SalvyActivity"
    private var currentLayout = 0
    private var lives = 3 // Player starts with 3 lives

    private lateinit var nameInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var isActiveInput: EditText
    private lateinit var conditionInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Log.d(TAG, "Setting orientation to landscape")
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            Log.d(TAG, "Setting content view to Salvy's story layout (intro)")
            currentLayout = R.layout.salvy_story // Start with the intro layout
            setContentView(currentLayout)
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Check if the player wants to resume
        if (currentLayout != 0) {
            showResumeDialog()
        }
    }

    override fun onStop() {
        super.onStop()

        // Save progress before exiting
        saveProgress()

        // Navigate to LandscapeModeActivity
        val intent = Intent(this, LandscapeMode::class.java)
        startActivity(intent)
        finish()  // Optionally finish the current activity
    }

    private fun showResumeDialog() {
        val currentActivity = this::class.java.simpleName
        if (currentActivity != "LandscapeModeActivity") {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you want to resume from where you left off?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    // Load the saved progress
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "Screen touched. Current layout: $currentLayout")
                when (currentLayout) {
                    R.layout.salvy_story -> navigateToLayout(R.layout.salvy_story2)
                    R.layout.salvy_story2 -> navigateToLayout(R.layout.salvy_story3)
                    R.layout.salvy_story3 -> navigateToLayout(R.layout.salvy_story4)
                    R.layout.salvy_story4 -> navigateToLayout(R.layout.salvy_story5)
                    R.layout.salvy_story5 -> navigateToLayout(R.layout.salvy_lvlstage1_q1an)
                    R.layout.salvy_lvlstage1_q1correct -> navigateToLayout(R.layout.salvy_story6)
                    R.layout.salvy_lvlstage1_q1fail -> retryQuestion(R.layout.salvy_lvlstage1_q1an)
                    R.layout.salvy_story6 -> navigateToLayout(R.layout.salvy_transition)
                    R.layout.salvy_transition -> navigateToLayout(R.layout.salvy_transition2)
                    R.layout.salvy_transition2 -> navigateToLayout(R.layout.salvy_transition3)
                    R.layout.salvy_transition3 -> navigateToLayout(R.layout.salvy_lvlstage2_q2)
                    R.layout.salvy_lvlstage2_q2 -> navigateToLayout(R.layout.salvy_lvlstage2_q2an)
                    R.layout.salvy_lvlstage2_q2correct -> navigateToLayout(R.layout.salvy_story7)
                    R.layout.salvy_lvlstage2_q2fail -> retryQuestion(R.layout.salvy_lvlstage2_q2an)
                    R.layout.salvy_story7 -> navigateToLayout(R.layout.salvy_lvlstage3_q3)
                    R.layout.salvy_lvlstage3_q3 -> navigateToLayout(R.layout.salvy_lvlstage3_q3_1)
                    R.layout.salvy_lvlstage3_q3_1 -> navigateToLayout(R.layout.salvy_lvlstage3_q3_2)
                    R.layout.salvy_lvlstage3_q3_2 -> navigateToLayout(R.layout.salvy_lvlstage3_q3_3)
                    R.layout.salvy_lvlstage3_q3_3 -> navigateToLayout(R.layout.salvy_lvlstage3_q3_4)
                    R.layout.salvy_lvlstage3_q3_4 -> navigateToLayout(R.layout.salvy_lvlstage3_q3an)
                    R.layout.salvy_lvlstage3_q3correct -> navigateToLayout(R.layout.salvy_lvlstage3_q3correct2)
                    R.layout.salvy_lvlstage3_q3fail -> navigateToLayout(R.layout.salvy_story7)
                    R.layout.salvy_lvlstage3_q3correct2 -> navigateToLayout(R.layout.salvy_lvlstage3_q3correct3)
                    R.layout.salvy_lvlstage3_q3correct3 -> navigateToLayout(R.layout.salvy_lvlstage3_q3correct4)
                    R.layout.salvy_lvlstage3_q3correct4 -> {
                        Log.d(TAG, "Navigating to the ending screen")
                        navigateToLayout(R.layout.salvy_storyending)
                    }
                    else -> Log.d(TAG, "Unknown layout, no action taken")
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun setupEndingTouchAreas() {
        val menuArea = findViewById<View>(R.id.menuArea)
        val playAgainArea = findViewById<View>(R.id.playAgainArea)

        menuArea.setOnClickListener {
            Log.d(TAG, "Menu area clicked, navigating to LandscapeMode")
            try {
                val intent = Intent(this, LandscapeMode::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Log.e(TAG, "Error launching LandscapeMode", e)
            }
        }

        playAgainArea.setOnClickListener {
            Log.d(TAG, "Play Again area clicked, navigating to gameplayss")
            try {
                val intent = Intent(this, gameplayss::class.java)
                intent.putExtra("showCharacterSelect", true)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Log.e(TAG, "Error launching gameplayss", e)
            }
        }
    }

    private fun navigateToLayout(layout: Int) {
        currentLayout = layout
        setContentView(currentLayout)
        when (currentLayout) {
            R.layout.salvy_lvlstage1_q1an -> setupLevel1Inputs()
            R.layout.salvy_lvlstage2_q2an -> setupChoiceClickListeners()
            R.layout.salvy_lvlstage3_q3an -> setupLevel3Inputs()
            R.layout.salvy_storyending -> setupEndingTouchAreas()
        }
        updateHearts()
    }

    private fun retryQuestion(layout: Int) {
        navigateToLayout(layout)
    }

    private fun setupLevel1Inputs() {
        nameInput = findViewById(R.id.nameInput)
        ageInput = findViewById(R.id.ageInput)
        isActiveInput = findViewById(R.id.isActiveInput)

        nameInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (nameInput.text.toString() == "name") {
                    ageInput.requestFocus()
                } else {
                    checkAnswer()
                }
                return@setOnEditorActionListener true
            }
            false
        }

        ageInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (ageInput.text.toString() == "age") {
                    isActiveInput.requestFocus()
                } else {
                    checkAnswer()
                }
                return@setOnEditorActionListener true
            }
            false
        }

        isActiveInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkAnswer()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun checkAnswer() {
        val name = nameInput.text.toString()
        val age = ageInput.text.toString()
        val isActive = isActiveInput.text.toString().toLowerCase()

        val isCorrect = name == "name" && age == "age" && isActive == "isactive"
        handleAnswer(isCorrect, R.layout.salvy_lvlstage1_q1correct, R.layout.salvy_lvlstage1_q1fail)
    }

    private fun setupLevel3Inputs() {
        conditionInput = findViewById(R.id.conditionInput)

        conditionInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val condition = conditionInput.text.toString().replace(" ", "")
                val isCorrect = condition == "treasureVisible&&trapDisabled"
                handleAnswer(isCorrect, R.layout.salvy_lvlstage3_q3correct, R.layout.salvy_lvlstage3_q3fail)
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun setupChoiceClickListeners() {
        val choice1 = findViewById<ImageView>(R.id.ch1)
        val choice2 = findViewById<ImageView>(R.id.ch2)
        val choice3 = findViewById<ImageView>(R.id.ch3)
        val choice4 = findViewById<ImageView>(R.id.ch4)

        choice1.setOnClickListener {
            Log.d(TAG, "Choice 1 clicked")
            handleAnswer(false, R.layout.salvy_lvlstage2_q2correct, R.layout.salvy_lvlstage2_q2fail)
        }
        choice2.setOnClickListener {
            Log.d(TAG, "Choice 2 clicked")
            handleAnswer(false, R.layout.salvy_lvlstage2_q2correct, R.layout.salvy_lvlstage2_q2fail)
        }
        choice3.setOnClickListener {
            Log.d(TAG, "Choice 3 clicked")
            handleAnswer(true, R.layout.salvy_lvlstage2_q2correct, R.layout.salvy_lvlstage2_q2fail)
        }
        choice4.setOnClickListener {
            Log.d(TAG, "Choice 4 clicked")
            handleAnswer(false, R.layout.salvy_lvlstage2_q2correct, R.layout.salvy_lvlstage2_q2fail)
        }
    }

    private fun handleAnswer(isCorrect: Boolean, correctLayout: Int, incorrectLayout: Int) {
        Log.d(TAG, "handleAnswer called with isCorrect = $isCorrect")

        if (isCorrect) {
            Log.d(TAG, "Correct answer selected")
            navigateToLayout(correctLayout)
        } else {
            Log.d(TAG, "Incorrect answer selected")
            lives--
            if (lives > 0) {
                navigateToLayout(incorrectLayout)
            } else {
                Log.d(TAG, "Game over")
                navigateToLayout(R.layout.game_over)
            }
        }
    }

    private fun updateHearts() {
        val heartsContainer = findViewById<ViewGroup>(R.id.heartsContainer) ?: return
        Log.d(TAG, "Updating hearts with lives remaining: $lives")

        for (i in 0 until heartsContainer.childCount) {
            val heart = heartsContainer.getChildAt(i) as? ImageView
            if (heart != null) {
                heart.setImageResource(if (i < lives) R.drawable.hart_lives else R.drawable.hart_empty)
            }
        }
    }
}
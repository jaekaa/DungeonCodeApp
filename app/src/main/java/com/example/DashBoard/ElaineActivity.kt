package com.example.DashBoard

import android.content.Intent
import android.os.Bundle
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import android.widget.EditText
import android.view.inputmethod.EditorInfo

class ElaineActivity : AppCompatActivity() {
    private val TAG = "ElaineActivity"
    private var currentLayout = 0
    private var lives = 3 // Player starts with 3 lives

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Log.d(TAG, "Setting orientation to landscape")
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            Log.d(TAG, "Setting content view to Elaine's story layout (intro)")
            currentLayout = R.layout.elaine_story // Start with the intro layout
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

        // Avoid navigating to LandscapeMode if the activity is explicitly finishing
        if (!isFinishing) {
            // Save progress before exiting
            saveProgress()

            // Navigate to LandscapeMode
            val intent = Intent(this, LandscapeMode::class.java)
            startActivity(intent)
        }
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
        currentLayout = R.layout.elaine_story
        lives = 3
        saveProgress() // Save the reset progress
        navigateToLayout(currentLayout)
    }

    private fun saveProgress() {
        val sharedPreferences = getSharedPreferences("ElaineProgress", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("currentLayout", currentLayout)
        editor.putInt("lives", lives)
        editor.apply()
        Log.d(TAG, "Progress saved: Layout=$currentLayout, Lives=$lives")
    }

    private fun loadProgress() {
        val sharedPreferences = getSharedPreferences("ElaineProgress", MODE_PRIVATE)
        currentLayout = sharedPreferences.getInt("currentLayout", R.layout.elaine_story)
        lives = sharedPreferences.getInt("lives", 3)
        Log.d(TAG, "Progress loaded: Layout=$currentLayout, Lives=$lives")
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            Log.d(TAG, "Screen touched. Current layout: $currentLayout")
            when (currentLayout) {
                R.layout.elaine_story -> navigateToLayout(R.layout.elaine_story2)
                R.layout.elaine_story2 -> navigateToLayout(R.layout.elaine_lvlstage1_q1)
                R.layout.elaine_lvlstage1_q1 -> navigateToLayout(R.layout.elaine_lvlstage1_q1an)
                R.layout.elaine_lvlstage1_q1correct -> navigateToLayout(R.layout.elaine_story3)
                R.layout.elaine_lvlstage1_q1fail -> retryQuestion(R.layout.elaine_lvlstage1_q1an)
                R.layout.elaine_story3 -> navigateToLayout(R.layout.elaine_story4)
                R.layout.elaine_story4 -> navigateToLayout(R.layout.elaine_lvlstage2_q2)
                R.layout.elaine_lvlstage2_q2 -> navigateToLayout(R.layout.elaine_lvlstage2_q2map)
                R.layout.elaine_lvlstage2_q2map -> navigateToLayout(R.layout.elaine_lvlstage2_q2search)
                R.layout.elaine_lvlstage2_q2search -> navigateToLayout(R.layout.elaine_lvlstage2_q2clue)
                R.layout.elaine_lvlstage2_q2clue -> navigateToLayout(R.layout.elaine_lvlstage2_q2crack)
                R.layout.elaine_lvlstage2_q2crack -> navigateToLayout(R.layout.elaine_lvlstage2_q2an)
                R.layout.elaine_lvlstage2_q2correct -> navigateToLayout(R.layout.elaine_story5)
                R.layout.elaine_lvlstage2_q2fail -> retryQuestion(R.layout.elaine_lvlstage2_q2an)
                R.layout.elaine_story5 -> navigateToLayout(R.layout.elaine_lvlstage3_q3)
                R.layout.elaine_lvlstage3_q3 -> navigateToLayout(R.layout.elaine_lvlstage3_q3guard)
                R.layout.elaine_lvlstage3_q3guard -> navigateToLayout(R.layout.elaine_lvlstage3_q3guard1)
                R.layout.elaine_lvlstage3_q3guard1 -> navigateToLayout(R.layout.elaine_lvlstage3_q3guard2)
                R.layout.elaine_lvlstage3_q3guard2 -> navigateToLayout(R.layout.elaine_lvlstage3_q3guard3)
                R.layout.elaine_lvlstage3_q3guard3 -> navigateToLayout(R.layout.elaine_lvlstage3_q3guard4)
                R.layout.elaine_lvlstage3_q3guard4 -> navigateToLayout(R.layout.elaine_lvlstage3_q3guard5)
                R.layout.elaine_lvlstage3_q3guard5 -> navigateToLayout(R.layout.elaine_lvlstage3_q3guard6)
                R.layout.elaine_lvlstage3_q3guard6 -> navigateToLayout(R.layout.elaine_lvlstage3_q3an)
                R.layout.elaine_lvlstage3_q3correct -> navigateToLayout(R.layout.elaine_storyending)
                else -> Log.d(TAG, "Unknown layout, no action taken")
            }
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun navigateToLayout(layout: Int) {
        currentLayout = layout
        setContentView(currentLayout)
        when (currentLayout) {
            R.layout.elaine_lvlstage1_q1an -> setupLevel1Inputs()
            R.layout.elaine_lvlstage2_q2an -> setupLevel2Inputs()
            R.layout.elaine_lvlstage3_q3an -> setupLevel3Inputs()
            R.layout.elaine_storyending -> setupEndingTouchAreas()
        }
        updateHearts()
        saveProgress()
    }

    private fun setupLevel1Inputs() {
        val choice1 = findViewById<ImageView>(R.id.choice1)
        val choice2 = findViewById<ImageView>(R.id.choice2)
        val choice3 = findViewById<ImageView>(R.id.choice3)
        val choice4 = findViewById<ImageView>(R.id.choice4)

        val choices = listOf(choice1, choice2, choice3, choice4)

        choices.forEachIndexed { index, choice ->
            choice.setOnClickListener {
                checkAnswer(index + 1)
            }
        }
    }

    private fun checkAnswer(choiceNumber: Int) {
        val isCorrect = choiceNumber == 3
        handleAnswer(
            isCorrect,
            R.layout.elaine_lvlstage1_q1correct,
            R.layout.elaine_lvlstage1_q1fail
        )
    }

    private fun setupLevel2Inputs() {
        val ifInput = findViewById<EditText>(R.id.ifInput)
        val elifInput = findViewById<EditText>(R.id.elifInput)

        ifInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (ifInput.text.toString() == "score") {
                    elifInput.requestFocus()
                } else {
                    checkLevel2Answer()
                }
                return@setOnEditorActionListener true
            }
            false
        }

        elifInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                checkLevel2Answer()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun setupLevel3Inputs() {
        val inputs = listOf(
            findViewById<EditText>(R.id.potionInput),
            findViewById<EditText>(R.id.mapInput),
            findViewById<EditText>(R.id.beesInput),
            findViewById<EditText>(R.id.potionInput2),
            findViewById<EditText>(R.id.mapInput2),
            findViewById<EditText>(R.id.beesInput2)
        )

        val correctAnswers = listOf("potion", "map", "bees", "potion", "map", "bees")

        for (i in inputs.indices) {
            inputs[i].setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    if (inputs[i].text.toString().toLowerCase() == correctAnswers[i]) {
                        if (i < inputs.lastIndex) {
                            inputs[i + 1].requestFocus()
                        } else {
                            checkLevel3Answer()
                        }
                    } else {
                        checkLevel3Answer()
                    }
                    return@setOnEditorActionListener true
                }
                false
            }
        }
    }

    private fun checkLevel2Answer() {
        val ifScore = findViewById<EditText>(R.id.ifInput).text.toString()
        val elifScore = findViewById<EditText>(R.id.elifInput).text.toString()

        if (ifScore == "score" && elifScore == "score") {
            navigateToLayout(R.layout.elaine_lvlstage2_q2correct)
        } else {
            lives--
            if (lives > 0) {
                navigateToLayout(R.layout.elaine_lvlstage2_q2fail)
            } else {
                navigateToLayout(R.layout.game_over)
            }
        }
    }

    private fun checkLevel3Answer() {
        val correctAnswers = listOf("potion", "map", "bees", "potion", "map", "bees")
        val userAnswers = listOf(
            R.id.potionInput, R.id.mapInput, R.id.beesInput,
            R.id.potionInput2, R.id.mapInput2, R.id.beesInput2
        ).map { findViewById<EditText>(it).text.toString() }

        val isCorrect = userAnswers == correctAnswers
        handleAnswer(isCorrect, R.layout.elaine_lvlstage3_q3correct, R.layout.elaine_story5)
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
            heart?.setImageResource(if (i < lives) R.drawable.hart_lives else R.drawable.hart_empty)
        }
    }

    private fun retryQuestion(layout: Int) {
        navigateToLayout(layout)
    }

    private fun setupEndingTouchAreas() {
        val menuArea = findViewById<View>(R.id.menuButton)
        val playAgainArea = findViewById<View>(R.id.playAgainButton)

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

    private fun showInputDialog(title: String, onInput: (String) -> Unit) {
        val input = EditText(this)
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                onInput(input.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
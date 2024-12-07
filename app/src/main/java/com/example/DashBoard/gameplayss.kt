package com.example.DashBoard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.content.pm.ActivityInfo
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class gameplayss : AppCompatActivity() {
    private val TAG = "gameplayss"
    private var charac = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Log.d(TAG, "Setting orientation to landscape")
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            Log.d(TAG, "Setting content view to landscape layout")
            setContentView(R.layout.gameplayestetik)
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
            finish()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (!charac) {
                Log.d(TAG, "Switching to character selection layout")
                setContentView(R.layout.character)
                charac = true
                setupCharacterClickListeners()
            } else {
                Log.d(TAG, "Switching back to gameplay layout")
                setContentView(R.layout.gameplayestetik)
                charac = false
            }
        }
        return super.onTouchEvent(event)
    }

    private fun setupCharacterClickListeners() {
        findViewById<View>(R.id.imageView18)?.setOnClickListener { onCharacterSelected(it) }
        findViewById<View>(R.id.imageView22)?.setOnClickListener { onCharacterSelected(it) }
        findViewById<View>(R.id.imageView23)?.setOnClickListener { onCharacterSelected(it) }
    }

    private fun onCharacterSelected(view: View) {
        val intent = when (view.id) {
            R.id.imageView18 -> Intent(this, ElaineActivity::class.java)
            R.id.imageView22 -> Intent(this, SalvyActivity::class.java)
            R.id.imageView23 -> Intent(this, ChameleonActivity::class.java)
            else -> null
        }
        intent?.let {
            startActivity(it)
            Log.d(TAG, "Navigating to ${it.component?.className}")
        } ?: Log.e(TAG, "Invalid character selected")
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        showExitDialog()
    }

    private fun showExitDialog() {
        val builder = AlertDialog.Builder(this, R.style.PixelDialogTheme)
        builder.setTitle(R.string.DUNGEON_CODE)
            .setMessage("You leaving already? :(")
            .setPositiveButton("Yes") { _, _ -> finish() }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
        builder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "gameplayss is being destroyed")
    }
}
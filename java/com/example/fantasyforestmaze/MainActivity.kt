package com.example.fantasyforestmaze

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.os.CountDownTimer

class MainActivity : ComponentActivity() {

    private lateinit var mazeView: MazeView
    private lateinit var scoreText: TextView
    private lateinit var timerText: TextView
    private var level = 1 // Starting level
    private var score = 0 // Starting score
    private var timerValue = 30 // Time limit in seconds for each level
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Show the splash screen
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set the main activity layout
        setContentView(R.layout.main_activity)

        // Initialize the MazeView
        mazeView = findViewById(R.id.mazeView)

        // Initialize the score and timer TextViews
        scoreText = findViewById(R.id.scoreText)
        timerText = findViewById(R.id.timerText)

        // Set up button click listeners
        findViewById<Button>(R.id.buttonUp).setOnClickListener {
            mazeView.movePlayer(0, -1) // Move up
        }
        findViewById<Button>(R.id.buttonDown).setOnClickListener {
            mazeView.movePlayer(0, 1) // Move down
        }
        findViewById<Button>(R.id.buttonLeft).setOnClickListener {
            mazeView.movePlayer(-1, 0) // Move left
        }
        findViewById<Button>(R.id.buttonRight).setOnClickListener {
            mazeView.movePlayer(1, 0) // Move right
        }
        val buttonRestart: Button = findViewById(R.id.buttonRestart)

        buttonRestart.setOnClickListener {
            mazeView.resetGame() // Call the resetGame method
            buttonRestart.visibility = View.GONE // Hide the Restart button after restarting
        }

        // Start the first level
        startLevel()

        // Listener for maze completion
        mazeView.setOnMazeUpdateListener(object : MazeView.OnMazeUpdateListener {
            override fun onUpdate(score: Int, timeLeft: Int) {
                scoreText.text = "Score: $score"
                timerText.text = "Time: $timeLeft"

                if (timeLeft == 0) {
                    buttonRestart.visibility = View.VISIBLE // Show Restart button
                }
            }
        })


    }

    private fun startLevel() {
        // Reset the timer for the new level
        timer?.cancel()
        timerValue = 30
        startTimer()

        // Generate a new maze for the current level
        mazeView.generateNewMaze()


        // Update the UI
        updateScoreAndTimer()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timerValue * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                timerValue = (millisUntilFinished / 1000).toInt()
                updateScoreAndTimer()
            }

            override fun onFinish() {
                timerValue = 0
                updateScoreAndTimer()
                Toast.makeText(this@MainActivity, "Time's up! Game Over.", Toast.LENGTH_SHORT).show()
                gameOver()
            }
        }.start()
    }

    private fun levelCompleted() {
        timer?.cancel() // Stop the timer

        if (timerValue > 0) {
            // Award points and move to the next level
            score += level * 100
            level++
            Toast.makeText(this, "Level $level Complete! Starting next level.", Toast.LENGTH_SHORT).show()
            startLevel()
        } else {
            Toast.makeText(this, "Time's up! You failed the level.", Toast.LENGTH_SHORT).show()
            gameOver()
        }
    }

    private fun updateScoreAndTimer() {
        scoreText.text = getString(R.string.score_format, score)
        timerText.text = getString(R.string.time_format, timerValue)
    }

    private fun gameOver() {
        // Handle game over logic here (e.g., reset game or show final score)
        Toast.makeText(this, "Game Over! Final Score: $score", Toast.LENGTH_LONG).show()
    }
}

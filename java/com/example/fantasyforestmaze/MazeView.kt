package com.example.fantasyforestmaze

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.math.absoluteValue

class MazeView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var maze: Array<IntArray> = generateMaze(5)  // Initial 5x5 maze
    private val paintWall = Paint().apply { color = Color.BLACK }
    private val paintPlayer = Paint().apply { color = Color.BLUE }
    private val paintExit = Paint().apply { color = Color.GREEN }

    private var playerX = 1
    private var playerY = 1
    private var score = 0
    private var timeLeft = 30

    private var handler = Handler(Looper.getMainLooper())
    private var countdownRunnable: Runnable? = null

    private var gameOver = false  // Track if the game is over

    private val cellSize: Float
        get() = width.toFloat() / maze.size

    // Declare the listener interface
    interface OnMazeUpdateListener {
        fun onUpdate(score: Int, timeLeft: Int)
    }

    // Declare the listener variable
    private var onMazeUpdateListener: OnMazeUpdateListener? = null

    // Setter for the listener
    fun setOnMazeUpdateListener(listener: OnMazeUpdateListener) {
        this.onMazeUpdateListener = listener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the maze
        for (row in maze.indices) {  // Using .indices instead of manually specifying range
            for (col in maze[row].indices) {
                if (maze[row][col] == 1) {
                    canvas.drawRect(
                        col * cellSize,
                        row * cellSize,
                        (col + 1) * cellSize,
                        (row + 1) * cellSize,
                        paintWall
                    )
                } else if (row == maze.size - 2 && col == maze[0].size - 2) {
                    canvas.drawRect(
                        col * cellSize,
                        row * cellSize,
                        (col + 1) * cellSize,
                        (row + 1) * cellSize,
                        paintExit
                    )
                }
            }
        }

        // Draw the player
        canvas.drawCircle(
            playerX * cellSize + cellSize / 2,
            playerY * cellSize + cellSize / 2,
            cellSize / 3,
            paintPlayer
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = (event.x / cellSize).toInt()
            val y = (event.y / cellSize).toInt()

            val dx = (x - playerX).absoluteValue
            val dy = (y - playerY).absoluteValue

            if ((dx == 1 && dy == 0) || (dy == 1 && dx == 0)) {
                val newX = playerX + (x - playerX)
                val newY = playerY + (y - playerY)

                if (maze[newY][newX] == 0) {  // Ensure new position is not a wall
                    playerX = newX
                    playerY = newY
                    invalidate()

                    // Update listener
                    onMazeUpdateListener?.onUpdate(score, timeLeft)

                    // Check if player reached the exit
                    if (playerX == maze[0].size - 2 && playerY == maze.size - 2) {
                        post {
                            score += 100  // Increase score regardless of time remaining
                            Toast.makeText(context, "You Win! Score: $score", Toast.LENGTH_SHORT).show()

                            // Notify listener with updated score and time
                            onMazeUpdateListener?.onUpdate(score, timeLeft)

                            generateNewMaze()  // Generate a new maze
                            playerX = 1  // Reset player position
                            playerY = 1
                            timeLeft = 30  // Reset timer (now starting at 30)
                            invalidate()  // Redraw the new maze
                        }
                    }
                }
            }
        }
        return true
    }

    // Recursive maze generation
    private fun generateMaze(size: Int): Array<IntArray> {
        val maze = Array(size) { IntArray(size) { 1 } }
        val visited = Array(size) { BooleanArray(size) { false } }
        val startX = 1
        val startY = 1
        maze[startY][startX] = 0
        visited[startY][startX] = true

        fun carvePath(x: Int, y: Int) {
            val directions = arrayOf(
                Pair(0, -2), Pair(0, 2), Pair(-2, 0), Pair(2, 0)
            ).toList().shuffled(kotlin.random.Random.Default)  // Shuffling with Random.Default

            for ((dx, dy) in directions) {
                val nx = x + dx
                val ny = y + dy
                if (nx in 1 until size - 1 && ny in 1 until size - 1 && !visited[ny][nx]) {
                    maze[ny][nx] = 0
                    visited[ny][nx] = true
                    maze[y + dy / 2][x + dx / 2] = 0
                    carvePath(nx, ny)
                }
            }
        }

        carvePath(startX, startY)
        maze[size - 2][size - 2] = 0
        return maze
    }

    fun generateNewMaze() {
        val newSize = (maze.size + 2).takeIf { it <= 15 } ?: 7
        maze = generateMaze(newSize)
        playerX = 1
        playerY = 1
        timeLeft = 30 // Starting time for the new maze
        gameOver = false
        if (countdownRunnable == null) startCountdown() // Only start countdown if it's not already running
        invalidate()
    }


    fun movePlayer(dx: Int, dy: Int) {
        val newX = playerX + dx
        val newY = playerY + dy

        if (maze[newY][newX] == 0) {  // Ensure the new position is not a wall
            playerX = newX
            playerY = newY
            invalidate()

            // Check for reaching exit
            if (playerX == maze[0].size - 2 && playerY == maze.size - 2) {
                // Award points based on the current maze level (size - 5)
                val level = (maze.size - 5) / 2 + 1 // Calculate level based on maze size
                score += level * 100 // Add level * 100 to score

                // Notify the listener with the updated score
                onMazeUpdateListener?.onUpdate(score, timeLeft)

                Toast.makeText(context, "You Win! Score: $score", Toast.LENGTH_SHORT).show()

                // Generate a new maze for the next level
                generateNewMaze()
            }
        }
    }


    private fun startCountdown() {
        countdownRunnable = object : Runnable {
            override fun run() {
                if (timeLeft > 0 && !gameOver) {
                    timeLeft--
                    onMazeUpdateListener?.onUpdate(score, timeLeft)  // Notify listener with updated score and time
                    handler.postDelayed(this, 1000)
                } else {
                    gameOver = true
                    post {
                        gameOver = true
                        onMazeUpdateListener?.onUpdate(score, timeLeft)
                        Toast.makeText(context, "Game Over! Tap Restart to play again.", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
        handler.postDelayed(countdownRunnable!!, 1000)
    }
    fun resetGame() {
//        score = 0 // Reset score
        timeLeft = 60 // Reset time
        gameOver = false // Mark game as not over
        playerX = 1 // Reset player position
        playerY = 1
        maze = generateMaze(5) // Restart with the initial maze size
        invalidate() // Redraw the maze
        if (countdownRunnable == null) startCountdown() // Restart the countdown
    }


    override fun performClick(): Boolean {
        super.performClick()  // Calls the superclass implementation
        return true
    }
}
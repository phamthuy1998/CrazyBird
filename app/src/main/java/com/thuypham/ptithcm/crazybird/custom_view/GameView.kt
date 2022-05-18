package com.thuypham.ptithcm.crazybird.custom_view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.thuypham.ptithcm.crazybird.R
import com.thuypham.ptithcm.crazybird.model.Bird
import com.thuypham.ptithcm.crazybird.model.Pipe
import com.thuypham.ptithcm.crazybird.util.Screen.DEF_SCREEN_HEIGHT
import com.thuypham.ptithcm.crazybird.util.Screen.DEF_SCREEN_WIDTH
import com.thuypham.ptithcm.crazybird.util.ScreenUtils

class GameView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private var bird: Bird? = null
    private var screenWidth = 0
    private var screenHeight = 0

    private var handler1: Handler? = null
    private var runnable: Runnable? = null

    private var arrPipe: ArrayList<Pipe> = arrayListOf()
    private var sumPipe = 0

    private var onScoreChangeListener: ((Int) -> Unit?)? = null
    private var score = 0

    private var isGameStared = false
    private var isGameOver = false
    private var isInvokeGameOver = false
    private var onGameOverListener: ((Int) -> Unit?)? = null

    init {
        screenWidth = ScreenUtils.getWidth(context)
        screenHeight = ScreenUtils.getHeight(context)
        initBird()
        initPipe()
        handler1 = Handler()
        runnable = Runnable {
            run {
                Log.d("Thuyyy", "startTimer")
                invalidate()
            }
        }
        Log.d("Thuyyy", "init")
    }

    /**
     * Create a list Pipe data
     *
     * Calculate the position and width, height for it
     */
    private fun initPipe() {
        Log.d("thuyyy", "initPipe")
        arrPipe = arrayListOf()
        sumPipe = 4
        val halfSumPipe = sumPipe / 2
        for (index in 0..sumPipe) {
            if (index < halfSumPipe) {
                val pipe = Pipe().apply {
                    x = screenWidth + index * ((screenWidth + 200 * screenWidth / DEF_SCREEN_WIDTH) / halfSumPipe.toFloat())
                    y = 0f
                    width = 200 * screenWidth / DEF_SCREEN_WIDTH
                    height = screenHeight / 2
                    bitmap(BitmapFactory.decodeResource(resources, R.drawable.pipe_top))
                    randomY()
                }
                this.arrPipe.add(pipe)
            } else {
                val pipe = Pipe().apply {
                    val tempPipe = arrPipe.getOrNull(index - halfSumPipe)
                    width = 200 * screenWidth / DEF_SCREEN_WIDTH
                    height = screenHeight / 2
                    x = tempPipe?.x
                    y = (tempPipe?.y ?: 0f) + (tempPipe?.height ?: 0) + randomDistance(screenHeight)
                    bitmap(BitmapFactory.decodeResource(resources, R.drawable.pipe_bot))
                }
                this.arrPipe.add(pipe)
            }
        }
    }

    /**
     * Initialize data for Bird
     */
    private fun initBird() {
        bird = Bird().apply {
            width = 100 * screenWidth / DEF_SCREEN_WIDTH
            height = 100 * screenHeight / DEF_SCREEN_HEIGHT
            x = (screenWidth / 4f) * screenWidth / DEF_SCREEN_WIDTH
            y = screenHeight / 2f - (height?.div(2f) ?: 0f)
            setBitmaps(
                arrayListOf(
                    BitmapFactory.decodeResource(resources, R.drawable.bird1),
                    BitmapFactory.decodeResource(resources, R.drawable.bird2)
                )
            )
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (isGameStared) {
            var halfSumPipe = sumPipe / 2
            for (index in 0..sumPipe) {
                val pipe = arrPipe[index]

                if (isGameOver) {
                    pipe.speed = 0
                }
                /* Check Bird is collision the pipe or collision the screen, if yes -> stop pipe, invoke over game func*/
                if (bird?.isBirdPressThePipe(pipe) == true || bird?.isBirdOverScreen(screenHeight) == true) {
                    pipe.speed = 0
                    isGameOver = true
                    if (!isInvokeGameOver) {
                        isInvokeGameOver = true
                        onGameOverListener?.invoke(score)
                    }
                }

                /* Calculate score */
                if (bird?.canPlusScore(pipe, index, halfSumPipe) == true) {
                    score++
                    onScoreChangeListener?.invoke(score)
//                    if (score == 5) {
//                        sumPipe = 6
//                        halfSumPipe = 3
//                    } else if (score == 30) {
//                        sumPipe = 6
//                        halfSumPipe = 3
//                    }
                }

                /* Calculate position for y*/
                if (pipe.x ?: 0f < -(pipe.width ?: 0)) {
                    pipe.x = screenWidth.toFloat()
                    if (index < halfSumPipe) {
                        pipe.randomY()
                    } else {
                        val tempPipe = arrPipe.getOrNull(index - halfSumPipe)
                        pipe.y = (tempPipe?.y ?: 0f) + (tempPipe?.height ?: 0) + pipe.randomDistance(screenHeight)
                    }

                }

                /* Draw pipe */
                canvas?.let { pipe.draw(canvas) }
            }
        } else {
            if (bird?.y ?: 0f > screenHeight / 2) {
                bird?.drop = (-15 * screenHeight / DEF_SCREEN_HEIGHT).toFloat()
            }
        }

        if (!isGameOver || bird?.isBirdPressBottomScreen(screenHeight) == false) {
            startTimer()
        }
        drawBird(canvas)
    }

    private fun drawBird(canvas: Canvas?) {
        canvas?.let { bird?.draw(it, screenHeight) }
    }

    private fun stopTimer() {
        runnable?.let { handler1?.removeCallbacks(it) }
        runnable = null
        handler1 = null
        Log.d("Thuyyy", "stopTimer")
    }

    private fun startTimer() {
        runnable?.let { handler1?.postDelayed(it, 10) }
    }

    fun setOnTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            bird?.drop = -20f
        }
        return true
    }

    fun setOnScoreUpdateListener(listener: (score: Int) -> Unit) {
        this.onScoreChangeListener = listener
    }

    fun setOnGameOverListener(listener: (score: Int) -> Unit) {
        this.onGameOverListener = listener
    }


    fun startGame() {
        startTimer()
        isInvokeGameOver = false
        isGameStared = true
        isGameStared = true
        isGameOver = false
    }

    fun stopGame() {
        isGameStared = false
        isGameOver = true
    }

    fun resetGame() {
        isInvokeGameOver = false
        isGameOver = false
        isGameStared = true
        score = 0
        initBird()
        initPipe()
        startTimer()
    }

}
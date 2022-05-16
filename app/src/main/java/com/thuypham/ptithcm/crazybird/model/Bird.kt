package com.thuypham.ptithcm.crazybird.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

data class Bird(
    var arrBitmap: ArrayList<Bitmap>? = null,
    var count: Int = 0,
    var vFlap: Int = 5,
    var idCurrentBitmap: Int = 0,
    var drop: Float = 0f
) : BaseObjectView() {
    fun draw(canvas: Canvas, screenHeight: Int) {
        drop(screenHeight)
        this.bitmap()?.let { canvas.drawBitmap(it, x ?: 0f, y ?: 0f, null) }
    }

    private fun drop(screenHeight: Int) {
        drop += 0.7f
        if (y ?: 0f < screenHeight)
            y = y?.plus(drop)
    }

    fun setBitmaps(bitmaps: ArrayList<Bitmap>) {
        arrBitmap = bitmaps
        bitmaps.forEachIndexed { index, bitmap ->
            bitmaps[index] = Bitmap.createScaledBitmap(bitmap, width ?: 0, height ?: 0, true)
        }
    }

    private fun bitmap(): Bitmap? {
        count++
        if (count == vFlap) {
//            arrBitmap?.forEachIndexed { index, bitmap ->
//                if (index == arrBitmap?.size?.minus(1)) {
//                    idCurrentBitmap = 0
//                    return@forEachIndexed
//                } else if (index == idCurrentBitmap) {
//                    idCurrentBitmap = index + 1
//                    return@forEachIndexed
//                }
//
//            }
            idCurrentBitmap = if (idCurrentBitmap == 0) 1
            else 0
            count = 0
        }
        if (drop < 0) {
            val matrix = Matrix()
            matrix.postRotate(-30f)
            return arrBitmap?.get(idCurrentBitmap)?.let {
                Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, true)
            }
        } else {
            val matrix = Matrix()
            if (drop < 70) {
                matrix.postRotate(drop - 30f)
            } else {
                matrix.postRotate(45f)
            }

            return arrBitmap?.get(idCurrentBitmap)?.let {
                Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, true)
            }
        }
    }

    fun isBirdPressThePipe(pipe: Pipe): Boolean {
        val birdX1 = x ?: 0f + (width ?: 0)
        val birdY1 = y ?: 0f
        val birdY2 = birdY1 + (height ?: 0)

        val pipeX1 = pipe.x ?: 0f
        val pipeX2 = pipeX1 + (pipe.width ?: 0)
        val pipeY1 = pipe.y ?: 0f
        val pipeY2 = pipeY1 + (pipe.height ?: 0)

        return birdX1 in pipeX1..pipeX2 && (birdY1 in pipeY1..pipeY2 || birdY2 in pipeY1..pipeY2)
    }

    fun isBirdOverScreen(screenHeight: Int): Boolean {
        return y ?: 0f + (height ?: 0) >= screenHeight || y ?: 0f <= 0
    }

    fun canPlusScore(pipe: Pipe, index: Int, halfSumPipe: Int): Boolean {
        return x ?: 0f + (width ?: 0) > (pipe.x ?: 0f) + (pipe.width ?: 0) / 2
                && x ?: 0f + (width ?: 0) <= (pipe.x ?: 0f) + (pipe.width ?: 0) / 2 + pipe.speed
                && index < halfSumPipe
    }
}
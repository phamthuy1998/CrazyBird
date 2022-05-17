package com.thuypham.ptithcm.crazybird.model

import android.graphics.Bitmap
import android.graphics.Canvas
import com.thuypham.ptithcm.crazybird.util.Screen
import com.thuypham.ptithcm.crazybird.util.ScreenUtils
import java.util.*
import java.util.concurrent.RecursiveTask

data class Pipe(
    var speed: Int = 10 * ScreenUtils.getWidth() / 1080,
) : BaseObjectView() {

    fun draw(canvas: Canvas) {
        this.x = this.x?.minus(speed)
        this.bitmap?.let { canvas.drawBitmap(it, this.x ?: 0f, this.y ?: 0f, null) }
    }

    fun randomY() {
        val bound = (0 + (height?.div(4) ?: 0)) + 1
        this.y = (0..(height?:0)/4).random().toFloat() - (height?.div(4) ?: 0)
    }

    fun bitmap(bm: Bitmap) {
        this.bitmap = Bitmap.createScaledBitmap(bm, width ?: 0, height ?: 0, true)
    }

    fun randomDistance(screenHeight: Int): Int{
        return 300 * screenHeight / Screen.DEF_SCREEN_HEIGHT + (0..500).random()
    }
}
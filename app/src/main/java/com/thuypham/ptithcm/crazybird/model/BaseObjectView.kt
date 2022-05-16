package com.thuypham.ptithcm.crazybird.model

import android.graphics.Bitmap
import android.graphics.Rect

open class BaseObjectView(
    var x: Float? = null,
    var y: Float? = null,
    var width: Int? = null,
    var height: Int? = null,
    var bitmap: Bitmap? = null,
    var rect: Rect? = null,
) {
    fun rect(): Rect {
        return Rect(x?.toInt() ?: 0, y?.toInt() ?: 0, (x ?: 0f + (width ?: 0)).toInt(), (y ?: 0f + (height ?: 0)).toInt())
    }

}
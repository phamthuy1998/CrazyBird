package com.thuypham.ptithcm.crazybird.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import com.thuypham.ptithcm.crazybird.MainApplication

/**
 * class sử dụng để xử lý về màn hình,độ phân giải
 */
object ScreenUtils {

    /**
     * Hàm sử dụng để lấy size màn hình
     * @param context
     * @return Point
     */
    fun getSize(context: Context?): Point? {
        if (context != null) {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size
        }
        return null
    }

    /**
     * Hàm sử dụng để convert từ dp sang px
     * @param context
     * @param dp(Float) số dp cần convert
     * @return Float
     */
    fun dpToPx(context: Context?, dp: Float): Float {
        if (context == null) return 0f
        val metrics = context.resources.displayMetrics
        return dp * (metrics.densityDpi / 160f)
    }

    /**
     * Hàm sử dụng để convert từ dp sang px
     * @param dp(Float) số dp cần convert
     * @param context
     * @return Int
     */
    fun dpToPx(dp: Float, context: Context?): Int {
        return dpToPx(dp, context?.resources)
    }

    /**
     * Hàm sử dụng để convert từ dp sang px
     * @param dp(Float) số dp cần convert
     * @param resources(Resources)
     * @return Int
     */
    fun dpToPx(dp: Float, resources: Resources?): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources?.displayMetrics).toInt()
    }

    fun dip2px(context: Context?, dipValue: Float): Int {
        if (context == null) return 0
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * Hàm sử dụng để convert từ px sang sp
     * @param context(Context)
     * @param px(Float) số px cần convert
     * @return Float
     */
    fun pxToSp(context: Context?, px: Float): Float {
        if (context == null) return 0f
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return px / scaledDensity
    }

    /**
     * Hàm sử dụng để convert từ sp sang px
     * @param context(Context)
     * @param sp(Float) số sp cần convert
     * @return Float
     */
    fun spToPx(context: Context?, sp: Float): Float {
        if (context == null) return 0f
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return sp * scaledDensity
    }
    fun spToPx(sp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics).toInt()
    }
    fun pxFromDp(context: Context?, dp: Float): Float {
        context?.let {
            return dp / context.resources.displayMetrics.density
        }
        return 0f
    }
    /**
     * Hàm sử dụng để lấy chiều ngang màn hình
     * @param context(Context)
     * @return Int
     */
    fun getWidth(context: Context? = MainApplication.getInstance()): Int {
        val point = getSize(context)
        return point?.x ?: 0
    }

    /**
     * Hàm sử dụng để lấy chiều cao màn hình
     * @param context(Context)
     * @return Int
     */
    fun getHeight(context: Context?): Int {
        val point = getSize(context)
        return point?.y ?: 0
    }

    /**
     * Hàm sử dụng để lấy chiều ngang theo ratio
     * @param context(Context)
     * @param targetWidth(Int) ratio cần lấy
     * @return Float
     */
    fun getWidthRatio(context: Context?, targetWidth: Int): Float {
        return if (context == null) 0f else (getWidth(context) / targetWidth).toFloat()
    }

    /**
     * Hàm sử dụng để lấy chiều cao của status bar
     * @param context(Context)
     * @return Int
     */
    fun getStatusBarHeight(context: Context?): Int {
        if (context == null) return 0
        // status bar height
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    /**
     * Hàm sử dụng để lấy chiều cao của action bar
     * @param context(Context)
     * @return Int
     */
    fun getActionBarHeight(context: Context?): Int {
        if (context == null) return 0
        // action bar height
        var actionBarHeight = 0
        try {
            val styledAttributes = context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
            actionBarHeight = styledAttributes.getDimension(0, 0f).toInt()
            styledAttributes.recycle()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return actionBarHeight
    }

    /**
     * Hàm sử dụng để lấy chiều cao của navigation bar
     * @param context(Context)
     * @return Int
     */
    fun getNavigationBarHeight(context: Context?): Int {
        if (context == null) return 0
        try {
            if (hasNavBar(context.resources)) {
                // navigation bar height
                var navigationBarHeight = 0
                val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    navigationBarHeight = context.resources.getDimensionPixelSize(resourceId)
                }
                //Toast.makeText(SendoApp.appContext, "navigation bar height: $navigationBarHeight", Toast.LENGTH_LONG).show()
                return navigationBarHeight
            }
        } catch (e: Throwable) {
        }
        return 0
    }

    /**
     * Hàm sử dụng để kiểm tra có navigation bar không
     * @param resources(Resources)
     * @return Boolean
     */
    fun hasNavBar(resources: Resources): Boolean {
        val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
        return id > 0 && resources.getBoolean(id)
    }


    /**
     * Hàm sử dụng để lấy chiều cao activity
     * @param context(Context)
     * @return Int
     */
    fun getScreenHeight(activity: Activity?): Int {
        if (activity == null) {
            return 0
        }
        val displaymetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
        return displaymetrics.heightPixels
    }

    /**
     * Hàm sử dụng để kiểm tra có phải tablet
     * @param context(Context)
     * @return Boolean
     */
    fun checkIsTablet(context: Context?): Boolean {
        val display = (context as? Activity)?.windowManager?.defaultDisplay
        val metrics = DisplayMetrics()
        display?.getMetrics(metrics)

        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = Math.sqrt(Math.pow(widthInches.toDouble(), 2.0) + Math.pow(heightInches.toDouble(), 2.0))
        return diagonalInches >= 7.0
    }
    
    fun getScreenBrightnessPercent(context: Context?) = context?.let {
        try {
            val currentValue = android.provider.Settings.System.getInt(it.contentResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS)
            (currentValue * 100f / 255).toInt()
        } catch (e: Exception) { 0 }
    } ?: 0
}

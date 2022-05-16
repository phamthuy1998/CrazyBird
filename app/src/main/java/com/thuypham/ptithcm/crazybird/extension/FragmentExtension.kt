package com.thuypham.ptithcm.crazybird.extension

import android.util.DisplayMetrics
import androidx.fragment.app.Fragment


fun Fragment.getScreenWidth(): Int {
    val displayMetrics = DisplayMetrics()
    requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

fun Fragment.getScreenHeight(): Int {
    val displayMetrics = DisplayMetrics()
    requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

fun Fragment.shareImageToOtherApp(imagePath: String) {
    requireActivity().shareImageToOtherApp(imagePath)
}

package com.thuypham.ptithcm.crazybird.extension

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.thuypham.ptithcm.crazybird.R
import java.util.*
import kotlin.concurrent.schedule

fun View.hideKeyBoard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}


fun View.setOnSingleClickListener(action: (View) -> Unit) {
    setOnClickListener { view ->
        view.isClickable = false
        action(view)
        view.postDelayed({
            view.isClickable = true
        }, 300L)
    }
}


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.showSnackBar(message: String?) {
    val snackBar = Snackbar.make(this, message ?: return, Snackbar.LENGTH_SHORT)
    snackBar.show()
}

fun View.showSnackBar(stringRes: Int) {
    val snackBar = Snackbar.make(this, this.context.getString(stringRes), Snackbar.LENGTH_SHORT)
    snackBar.show()
}


fun View.shakeAnimation(context: Context) {
    val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_shake)
    shake.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            Timer("",false).schedule(2000) {
                startAnimation(shake)
            }
        }

        override fun onAnimationRepeat(p0: Animation?) {
        }

    })
    animation = shake
}


fun View.expand() {
    if (isVisible)
        return //do nothing when view already visible


    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    layoutParams.height = 1
    visibility = View.VISIBLE
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Expansion speed of 1dp/ms
    a.duration = (targetHeight / context.resources.displayMetrics.density).toLong()
    startAnimation(a)
}

fun View.collapse() {

    if (!isVisible)
        return //do nothing when view already gone
    val height = measuredHeight

    val anim: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                isVisible = false
            } else {
                layoutParams.height = (height - (height * interpolatedTime)).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    anim.duration = (height / context.resources.displayMetrics.density).toLong()
    startAnimation(anim)
}

fun ImageView.setImageResourceWithAnimation(c: Context?, @DrawableRes res:Int) {
    val animOut: Animation = AnimationUtils.loadAnimation(c, R.anim.anim_fade_out)
    val animIn: Animation = AnimationUtils.loadAnimation(c, R.anim.anim_fade_in)
    animOut.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
        }

        override fun onAnimationRepeat(animation: Animation?) {
        }

        override fun onAnimationEnd(animation: Animation?) {
            setImageResource(res)
            animIn.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {}

                override fun onAnimationRepeat(animation: Animation?) {}
            })
            startAnimation(animIn)
        }


    })
    startAnimation(animOut)
}



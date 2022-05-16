package com.thuypham.ptithcm.crazybird.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import java.io.File


fun AppCompatActivity.shareDataToOtherApp(content: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}


fun Activity.shareImageToOtherApp(imagePath: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        val file = File(imagePath)
        val uri =
            FileProvider.getUriForFile(this@shareImageToOtherApp, application.packageName, file);
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/jpeg"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

fun Activity.hideSystemBars() {
    val windowInsetsController =
        ViewCompat.getWindowInsetsController(window.decorView) ?: return
    // Configure the behavior of the hidden system bars
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    // Hide both the status bar and the navigation bar
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
}


@Suppress("DEPRECATION")
fun Activity.isNetworkConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE).convert<ConnectivityManager>()
    return if (Build.VERSION.SDK_INT < 23) {
        cm.activeNetworkInfo
            ?.let { ni ->
                ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
            } ?: false
    } else {
        cm.activeNetwork
            ?.let { cm.getNetworkCapabilities(it) }
            ?.let { nc ->
                nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
            ?: false
    }
}


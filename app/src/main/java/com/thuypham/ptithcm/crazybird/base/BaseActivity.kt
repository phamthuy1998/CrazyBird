package com.thuypham.ptithcm.crazybird.base

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.thuypham.ptithcm.crazybird.extension.hideKeyBoard
import com.thuypham.ptithcm.crazybird.extension.isNetworkConnected
import com.thuypham.ptithcm.crazybird.ui.activity.NoNetworkActivity
import com.thuypham.ptithcm.crazybird.ui.dialog.ProgressDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


abstract class BaseActivity<T : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity() {

    protected lateinit var binding: T
    private lateinit var dialog: Dialog
    private val isHandleNetworkState = true
    private lateinit var connectivityManager: ConnectivityManager
    private var isShowNoInternetActivity = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("thuyyy${this::class.java.simpleName}", "onCreate")
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        setupLogic()
        setupView()
        setupDataObserver()

        dialog = ProgressDialog.progressDialog(this)
        if (isHandleNetworkState) {
            if (!isNetworkConnected()) {
                NoNetworkActivity.start(this)
            }
            registerNetworkChange()
        }
    }


    open fun setupLogic() {}
    abstract fun setupView()
    open fun setupDataObserver() {}

    override fun finish() {
        binding.root.hideKeyBoard()
        super.finish()
    }


    fun hideLoading() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    fun showLoading() {
        dialog.show()
    }

    /** Auto hide keyboard when click outside Edit text */
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        val ret = super.dispatchTouchEvent(event)
        event?.let { ev ->
            if (ev.action == MotionEvent.ACTION_UP) {
                currentFocus?.let { view ->
                    if (view is EditText) {
                        val touchCoordinates = IntArray(2)
                        view.getLocationOnScreen(touchCoordinates)
                        val x: Float = ev.rawX + view.getLeft() - touchCoordinates[0]
                        val y: Float = ev.rawY + view.getTop() - touchCoordinates[1]
                        //If the touch position is outside the EditText then we hide the keyboard
                        if (x < view.getLeft() || x >= view.getRight() || y < view.getTop() || y > view.getBottom()) {
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                            view.clearFocus()
                        }
                    }
                }
            }
        }
        return ret
    }

    protected open fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        hideKeyboard()
    }

    /** Register network change */
    private fun registerNetworkChange() {
        this.lifecycleScope.launch(Dispatchers.IO) {
            connectivityManager.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    it.registerDefaultNetworkCallback(networkCallback)
                } else {
                    val builder = NetworkRequest.Builder().build()
                    it.registerNetworkCallback(builder, networkCallback)
                }
            }
        }
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            NoNetworkActivity.start(this@BaseActivity)
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            onNetworkAvailable()
        }
    }

    open fun onNetworkAvailable() {}


    override fun onDestroy() {
        super.onDestroy()
        if (isHandleNetworkState) {
            isShowNoInternetActivity = false
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}
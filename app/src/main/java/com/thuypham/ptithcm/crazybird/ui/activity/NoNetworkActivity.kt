package com.thuypham.ptithcm.crazybird.ui.activity

import android.content.Context
import android.content.Intent
import com.thuypham.ptithcm.crazybird.R
import com.thuypham.ptithcm.crazybird.base.BaseActivity
import com.thuypham.ptithcm.crazybird.databinding.ActivityNoNetworkBinding

class NoNetworkActivity : BaseActivity<ActivityNoNetworkBinding>(R.layout.activity_no_network) {

    companion object {
        var isShow = false
        fun start(context: Context?) {
            if (!isShow)
                context?.let {
                    val intent = Intent(it, NoNetworkActivity::class.java)
                    it.startActivity(intent)
                }
            isShow = true
        }
    }

    override fun setupView() {

    }

    override fun onNetworkAvailable() {
        super.onNetworkAvailable()
        finish()
    }

    override fun onBackPressed() {

    }

    override fun onDestroy() {
        super.onDestroy()
        isShow = false
    }
}
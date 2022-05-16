package com.thuypham.ptithcm.crazybird

import android.app.Application

class MainApplication : Application() {
    companion object {
        private lateinit var instance: MainApplication
        fun getInstance() = instance
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }
}
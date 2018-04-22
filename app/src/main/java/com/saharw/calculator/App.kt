package com.saharw.calculator

import android.app.Application
import android.util.Log

/**
 * Created by saharw on 22/04/2018.
 */
class App : Application(){

    private val TAG = "App"

    override fun onCreate() {
        Log.d(TAG, "onCreateView()")
        super.onCreate()
    }
}
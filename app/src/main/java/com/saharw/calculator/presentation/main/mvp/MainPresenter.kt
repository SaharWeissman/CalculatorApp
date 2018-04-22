package com.saharw.calculator.presentation.main.mvp

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.saharw.calculator.presentation.base.IPresenter

/**
 * Created by saharw on 22/04/2018.
 */
class MainPresenter(val activity: AppCompatActivity, val view: MainView, val layoutId: Int, val model: MainModel) : IPresenter {

    private val TAG = "MainPresenter"

    override fun onCreate() {
        Log.d(TAG, "onCreate()")
        view.onCreate(activity, layoutId)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
    }
}
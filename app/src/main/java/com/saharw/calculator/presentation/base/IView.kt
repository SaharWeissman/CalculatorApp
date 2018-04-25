package com.saharw.calculator.presentation.base

import android.support.v7.app.AppCompatActivity

/**
 * Created by saharw on 22/04/2018.
 */
interface IView {
    fun onViewCreate(activity : AppCompatActivity, layoutId: Int)
    fun onViewResume()
    fun onViewDestroy()
}
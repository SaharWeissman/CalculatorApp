package com.saharw.calculator.presentation.main

import android.util.Log
import com.saharw.calculator.R
import com.saharw.calculator.presentation.base.BaseActivity
import com.saharw.calculator.presentation.base.IPresenter
import com.saharw.calculator.presentation.main.dagger.DaggerMainActivityComponent
import com.saharw.calculator.presentation.main.dagger.MainActivityComponent
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private val TAG = "MainActivity"

    @Inject
    lateinit var mPresenter : IPresenter

    override fun initActivity() {
        Log.d(TAG, "initActivity")
        DaggerMainActivityComponent.builder().mainActivityModule(MainActivityComponent.MainActivityModule(this, R.id.fragment_container)).build().inject(this)
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
        mPresenter.onCreate()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        mPresenter.onDestroy()
    }
}

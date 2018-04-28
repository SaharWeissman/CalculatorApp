package com.saharw.calculator.presentation.main.mvp

import android.app.Activity
import android.util.Log
import com.saharw.calculator.presentation.base.IModel
import io.reactivex.subjects.PublishSubject
import com.saharw.calculatorsdk.core.Calculator

/**
 * Created by saharw on 22/04/2018.
 */
class MainModel(val activity: Activity) : IModel {

    private val TAG = "MainModel"
    private lateinit var mCalc : Calculator
    var mEvalResult = PublishSubject.create<Float>()

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        mCalc = Calculator()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
    }

    fun eval(infixExpression : String) {
        Log.d(TAG, "eval(): expression = $infixExpression")
        var result = mCalc.eval(infixExpression)
        Log.d(TAG, "eval(): result = $result")
        mEvalResult.onNext(result)
    }
}
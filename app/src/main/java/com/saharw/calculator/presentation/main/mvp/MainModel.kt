package com.saharw.calculator.presentation.main.mvp

import android.app.Activity
import com.saharw.calculator.presentation.base.IModel

/**
 * Created by saharw on 22/04/2018.
 */
class MainModel(val activity: Activity) : IModel {
    override fun onCreate() {
        Calcu
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    /private val TAG = "MainModel"
//    private lateinit var mCalc : Calculator
//    var mEvalResult = PublishSubject.create<Float>()
//
//    override fun onCreate() {
//        Log.d(TAG, "onCreate")
//        mCalc = Calculator()
//    }
//
//    override fun onDestroy() {
//        Log.d(TAG, "onDestroy")
//    }
//
//    fun eval(infixExpression : String) {
//        Log.d(TAG, "eval(): expression = $infixExpression")
//        var result = mCalc.eval(infixExpression)
//        Log.d(TAG, "eval(): result = $result")
//        mEvalResult.onNext(result)
//    }
}
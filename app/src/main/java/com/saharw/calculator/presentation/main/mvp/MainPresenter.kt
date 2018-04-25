package com.saharw.calculator.presentation.main.mvp

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.saharw.calculator.entities.CalculatorButton
import com.saharw.calculator.presentation.base.IPresenter
import io.reactivex.schedulers.Schedulers

/**
 * Created by saharw on 22/04/2018.
 */
class MainPresenter(private val activity: AppCompatActivity, val view: MainView, private val layoutId: Int, val model: MainModel) : IPresenter {

    private val TAG = "MainPresenter"
    private lateinit var expressionSb : StringBuilder

    override fun onCreate() {
        Log.d(TAG, "onCreate")

        // view
        view.onViewCreate(activity, layoutId)
        view.mButtonClickSubject.subscribeOn(Schedulers.computation()).subscribe { onCalcBtnClicked(it) }

        // model
        model.onCreate()
        model.mEvalResult.subscribeOn(Schedulers.computation()).subscribe { onEvalResultReady(it) }

        // init fields of presenter
        expressionSb = StringBuilder()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        view.onViewDestroy()
        model.onDestroy()
    }

    override fun onResume() {
        Log.d(TAG, "onResume()")
        view.onViewResume()
    }

    private fun onCalcBtnClicked(calcButton: CalculatorButton?) {
        Log.d(TAG, "onCalcBtnClicked: btn.val = ${calcButton?.`val`}")

    }

    private fun onEvalResultReady(result: Float?) {
        Log.d(TAG, "onEvalResultReady: $result")
    }
}
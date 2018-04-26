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
    private lateinit var mExpressionSb: StringBuilder
    private var mPendingOperator : Char = ' '

    override fun onCreate() {
        Log.d(TAG, "onCreate")

        // view
        view.onViewCreate(activity, layoutId)
        view.mButtonClickSubject.subscribeOn(Schedulers.computation()).subscribe { onCalcBtnClicked(it) }

        // model
        model.onCreate()
        model.mEvalResult.subscribeOn(Schedulers.computation()).subscribe { onEvalResultReady(it) }

        // init fields of presenter
        mExpressionSb = StringBuilder()
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

        /*
         * logic will be this:
         * determine type of expression, can be:
         * 1. just numeric (=contains only digits) => display in main output
         * 2. numeric + operator (only "left side" of equation) => display numeric in main output & display operator in top output
         * 3. numeric + operator + numeric => equation is ready, show left numeric + operand on top & display right operand on main console
         * 4. either '=' was pressed / another operator was pressed => eval expression, display result in main output & new operator on top
         */

    }

    private fun onEvalResultReady(result: Float?) {
        Log.d(TAG, "onEvalResultReady: $result")
    }
}
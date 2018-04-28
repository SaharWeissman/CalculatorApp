package com.saharw.calculator.presentation.main.mvp

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.saharw.calculator.entities.CalculatorButton
import com.saharw.calculator.presentation.base.IPresenter
import com.saharw.calculator.util.ExpressionUtil
import io.reactivex.schedulers.Schedulers
import com.saharw.calculator.extensions.clear

/**
 * Created by saharw on 22/04/2018.
 */
class MainPresenter(private val activity: AppCompatActivity, val view: MainView, private val layoutId: Int, val model: MainModel) : IPresenter {

    private val TAG = "MainPresenter"
    private lateinit var mExpressionSb: StringBuilder
    private var mPendingOperator : Char = ' '
    private val VALUE_EMPTY: String = "0"

    private var mWasCreated = false
    private var mWasResumed = false

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        if(!mWasCreated) {

            mWasCreated = true

            // view
            view.onViewCreate(activity, layoutId)
            view.mButtonClickSubject.subscribeOn(Schedulers.computation()).subscribe { onCalcBtnClicked(it) }

            // model
            model.onCreate()
            model.mEvalResult.subscribeOn(Schedulers.computation()).subscribe { onEvalResultReady(it) }

            // init fields of presenter
            mExpressionSb = StringBuilder()
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        view.onViewDestroy()
        model.onDestroy()
    }

    override fun onResume() {
        Log.d(TAG, "onResume()")
        if(!mWasResumed) {
            mWasResumed = true
            view.onViewResume()
        }
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

        // update sb
        if(calcButton != null) {

            // check if this is CLEAR button
            if(calcButton.`val` == view.clearButtonStr){
                Log.d(TAG, "clear button was clicked")
                onClearInvoked(0f, ' ')
            }else {

                // check if need to clear data - if current displayed number is result of previous computation
                if(mPendingOperator == '='){
                    onClearInvoked(0f, ' ')
                }

                if(mPendingOperator == ' ') {
                    mExpressionSb.append(calcButton.`val`)
                }else {
                    mExpressionSb.append(mPendingOperator + calcButton.`val`)
                    mPendingOperator = ' '
                }
                var expType = ExpressionUtil.getExpressionType(mExpressionSb.toString())
                when (expType.type) {
                    ExpressionUtil.EXP_TYPE_DISPLAY -> {
                        Log.d(TAG, "case \"EXP_TYPE_DISPLAY\"")
                        var value = expType.value
                        if(value?.isEmpty()!!){
                            value = VALUE_EMPTY
                        }
                        view.setMainOutputValue(value?.toFloat()!!)
                        view.setTopOutputValue(expType.pendingOperator)

                        if (mExpressionSb.last() == '=') {
                            mExpressionSb.deleteCharAt(mExpressionSb.length - 1)
                        }
                    }
                    ExpressionUtil.EXP_TYPE_EVAL_ALL -> {
                        Log.d(TAG, "case \"EXP_TYPE_EVAL_ALL\"")
                        model.eval(expType.value!!)
                    }
                    ExpressionUtil.EXP_TYPE_EVAL_PENDING_OP -> {
                        Log.d(TAG, "case \"EXP_TYPE_EVAL_PENDING_OP\"")
                        mPendingOperator = expType.pendingOperator
                        model.eval(expType.value!!)
                    }
                    ExpressionUtil.EXP_TYPE_ERROR -> {
                        Log.d(TAG, "case \"EXP_TYPE_ERROR\"")
                    }
                    else -> {
                        Log.e(TAG, "case \"else\"")
                    }
                }
            }
        }
    }

    private fun onEvalResultReady(result: Float?) {
        Log.d(TAG, "onEvalResultReady: $result")
        view.setMainOutputValue(result?: 0f)
        mExpressionSb.clear()
        mExpressionSb.append(result)
        if(mPendingOperator == ' ') {
            mPendingOperator = '='
        }
        view.setTopOutputValue(mPendingOperator)
    }

    private fun onClearInvoked(clearValue : Float, clearPendingOpChar: Char) {
        mExpressionSb.clear()
        mPendingOperator = clearPendingOpChar
        view.setMainOutputValue(clearValue)
        view.setTopOutputValue(clearPendingOpChar)
    }
}

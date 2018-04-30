package com.saharw.calculator.presentation.main.mvp

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.saharw.calculator.entities.CalculatorButton
import com.saharw.calculator.extensions.clear
import com.saharw.calculator.presentation.base.IPresenter
import com.saharw.calculator.util.ExpressionUtil
import io.reactivex.schedulers.Schedulers

/**
 * Created by saharw on 22/04/2018.
 */
class MainPresenter(private val activity: AppCompatActivity, val view: MainView, private val layoutId: Int, val model: MainModel) : IPresenter {

    private val TAG = "MainPresenter"
    private lateinit var mExpressionSb: StringBuilder
    private var mPendingOperator : String = ""
    private val VALUE_EMPTY: String = "0"
    private val IGNORED_STRINGS = arrayOf("="," ","")

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
                Log.d(TAG, "onCalcBtnClicked(): clear button was clicked")
                onClearInvoked(0f, "")
            }else {

                // check if pending operator exist
                if(mPendingOperator != null && !IGNORED_STRINGS.contains(mPendingOperator)){

                    // if exists - need to append first the operator to expression than new char
                    mExpressionSb.append(mPendingOperator + calcButton.`val`)
                    mPendingOperator = ""
                }else {

                    /*
                    special handling
                     */
                    var shouldAppend = onSpecialCaseHandle(calcButton.`val`)

                    if(shouldAppend) {
                        mExpressionSb.append(calcButton.`val`)
                    }else {
                        return
                    }
                }
                var expTypeRes = ExpressionUtil.getExpressionType(mExpressionSb.toString().trim())
                if(expTypeRes != null){
                    Log.d(TAG, "onCalcBtnClicked(): expTypeRes = $expTypeRes")
                    when(expTypeRes.type){
                        ExpressionUtil.EXP_TYPE_DISPLAY -> {
                            Log.d(TAG, "onCalcBtnClicked(): case \"EXP_TYPE_DISPLAY\"")
                            if(expTypeRes.value != null) {
                                view.setMainOutputValue(expTypeRes.value!!)
                            }
                            if(expTypeRes.pendingOperator != null){
                                view.setTopOutputValue(expTypeRes.pendingOperator.toString())
                            }
                        }
                        ExpressionUtil.EXP_TYPE_EVAL -> {
                            Log.d(TAG, "onCalcBtnClicked(): case \"EXP_TYPE_EVAL\"")
                            if(expTypeRes.pendingOperator != null){
                                mPendingOperator = expTypeRes.pendingOperator!!
                                view.setTopOutputValue(expTypeRes.pendingOperator.toString())
                            }
                            if(expTypeRes.value != null) {
                                model.eval(expTypeRes.value!!)
                            }
                        }
                        ExpressionUtil.EXP_TYPE_ERROR -> {
                            Log.e(TAG, "onCalcBtnClicked(): case \"EXP_TYPE_ERROR\"")
                        }
                    }
                }else {
                    Log.e(TAG, "onCalcBtnClicked(): expTypeRes is null! clearing...")
                    onClearInvoked(0f, "")
                }
            }
        }
    }

    private fun onSpecialCaseHandle(s: String) : Boolean {
        var shouldAppend : Boolean
        if(mPendingOperator == "="){
            if(s[0].isDigit()) {
                Log.d(TAG, "onSpecialCaseHandle: input is digit & pending op os \"=\", clearing...")
                onClearInvoked(0f, "")
            }else {
                mPendingOperator = " "
            }
        }
        shouldAppend = when(s[0]){
            '.' -> {
                if(mExpressionSb.last() == '.' || tryingToAppendDecimalToFloat(mExpressionSb.toString())){
                    Log.d(TAG, "onSpecialCaseHandle: input is \".\" & last char in expression is \".\" or expression contains \".\", not appending")
                    false
                }else {
                    true
                }
            }else -> {
                true
            }
        }
        return shouldAppend
    }

    private fun tryingToAppendDecimalToFloat(expression: String): Boolean {
        var res = false
        val regex :Regex
        if(Regex(ExpressionUtil.EXP_REGEX_NUM_OP_NUM).matches(expression)){
            regex = Regex(ExpressionUtil.EXP_REGEX_NUM_OP_NUM)
            var matchResult = regex.find(expression, 0)
            if(matchResult != null) {
                var operator = matchResult.groupValues[ExpressionUtil.EXP_REGEX_GROUP_IDX].toCharArray()[0]
                var rightOperand = expression.substring(expression.lastIndexOf(operator) + 1, expression.length)
                res = rightOperand.contains('.')
            }
        }else if(Regex(ExpressionUtil.EXP_REGEX_NUM).matches(expression)){
            res = expression.contains('.')
        }
        return res
    }

    private fun onEvalResultReady(result: Float?) {
        Log.d(TAG, "onEvalResultReady: $result")
        var resStr = if(result != null){result}else{"0"}
        view.setMainOutputValue(resStr.toString())
        mExpressionSb.clear()
        mExpressionSb.append(result)
    }

    private fun onClearInvoked(clearValue : Float, clearPendingOpChar: String) {
        mExpressionSb.clear()
        mPendingOperator = clearPendingOpChar
        view.setMainOutputValue(clearValue.toString())
        view.setTopOutputValue(clearPendingOpChar)
    }
}

package com.saharw.calculator.util

import android.util.Log

/**
 * Created by saharw on 26/04/2018.
 */
object ExpressionUtil {

    private val TAG = "ExpressionUtil"
    private val OPERATORS_REGEX = "[\\+|\\/|\\-|\\*|\\=]"

    const val EXP_TYPE_ERROR = -1
    const val EXP_TYPE_DISPLAY = 0
    const val EXP_TYPE_EVAL_ALL = 1
    const val EXP_TYPE_EVAL_PENDING_OP = 2

    fun validateExpression() {
        //TODO: impl.
    }

    fun getExpressionType(exp: String) : ExpressionTypeResult {
        Log.d(TAG, "getExpressionType: exp = $exp")
        var res : ExpressionTypeResult

        if(Regex(OPERATORS_REGEX).findAll(exp,0).iterator().hasNext()){
            Log.d(TAG, "operators regex matched exp: $exp")

            //verify only 1 operator
            var lengthOperators = exp.filter { !it.isDigit() && it != '.'}.length
            if(lengthOperators > 1 && exp.first() != '-') {
                Log.d(TAG, "length operators > 1 (=$lengthOperators)")
                res = ExpressionTypeResult(EXP_TYPE_EVAL_PENDING_OP, exp.substring(0, exp.length-1), exp.last())
            }else {

                // check if last char is digit - if yes display, if not eval
                if(exp.last().isDigit()) {
                    var matchResult = Regex(OPERATORS_REGEX).findAll(exp,0).iterator().next()
                    var operatorChar = matchResult.value.toCharArray()[0]
                    res = ExpressionTypeResult(EXP_TYPE_DISPLAY, exp.substring(exp.lastIndexOf(operatorChar)+1, exp.length), operatorChar)
                }else{
                    val pendingOperator = if(exp.last() != '='){exp.last()}else{' '}
                    res = ExpressionTypeResult(EXP_TYPE_DISPLAY, exp.substring(0, exp.length-1), pendingOperator)
                }
            }
        } else {
            res = ExpressionTypeResult(EXP_TYPE_DISPLAY, exp, ' ')
        }

        return  res
    }

    data class ExpressionTypeResult(val type : Int, val value: String?, val pendingOperator: Char)
}
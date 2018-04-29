package com.saharw.calculator.util

import android.util.Log

/**
 * Created by saharw on 26/04/2018.
 */
object ExpressionUtil {

    private val TAG = "ExpressionUtil"
    private val EXP_REGEX_EVAL = "(-)?([0-9|.])+(\\+|-|/|\\*)(-)?([0-9|.])+(\\+|-|/|\\*|=)"
    private val EXP_REGEX_NUM_OP_NUM = "(-)?([0-9|.])+(\\+|-|/|\\*)(-)?([0-9|.])+"
    private val EXP_REGEX_NUM_OP = "(-)?([0-9|.])+(\\+|-|/|\\*)"
    private val EXP_REGEX_NUM = "(-)?([0-9|.])+"

    private val EXP_REGEX_GROUP_IDX = 3

    const val EXP_TYPE_ERROR = -1
    const val EXP_TYPE_DISPLAY = 0
    const val EXP_TYPE_EVAL = 1

    val OPERATORS_LIST = arrayListOf("*","+","/","-")

    private val DISPLAY_TYPE_1 = 10
    private val DISPLAY_TYPE_2 = 11
    private val DISPLAY_TYPE_3 = 12

    fun validateExpression() {
        //TODO: impl.
    }

    fun getExpressionType(exp: String) : ExpressionTypeResult? {
        Log.d(TAG, "getExpressionType: exp = $exp")

        // check which regex the expression matches to
        var regex : Regex?
        var expType : Int
        var res : ExpressionTypeResult? = null
        if(Regex(EXP_REGEX_EVAL).matches(exp)){
            Log.d(TAG, "getExpressionType: exp type = eval ($exp)")
            regex = Regex(EXP_REGEX_EVAL)
            expType = EXP_TYPE_EVAL
        }else if (Regex(EXP_REGEX_NUM_OP_NUM).matches(exp)){
            Log.d(TAG, "getExpressionType: exp type = num_op_num ($exp)")
            regex = Regex(EXP_REGEX_NUM_OP_NUM)
            expType = DISPLAY_TYPE_1
        }else if (Regex(EXP_REGEX_NUM_OP).matches(exp)){
            Log.d(TAG, "getExpressionType: exp type = num_op($exp)")
            regex = Regex(EXP_REGEX_NUM_OP)
            expType = DISPLAY_TYPE_2
        }else if(Regex(EXP_REGEX_NUM).matches(exp)) {
            Log.d(TAG, "getExpressionType: exp type = num ($exp)")
            regex = Regex(EXP_REGEX_NUM)
            expType = DISPLAY_TYPE_3
        }else {
            Log.e(TAG, "getExpressionType: exp does not match any regex!($exp)")
            regex = null
            expType = EXP_TYPE_ERROR
        }

        if(regex != null){
            when(expType){
                DISPLAY_TYPE_3 -> { // just a number, e.g. '2.4'
                    Log.d(TAG, "getExpressionType: case \"DISPLAY_TYPE_3\"")
                    res = ExpressionTypeResult(EXP_TYPE_DISPLAY, exp)
                }
                DISPLAY_TYPE_2 -> { // number & operator, e.g. '2.4+'
                    Log.d(TAG, "getExpressionType: case \"DISPLAY_TYPE_2\"")
                    var displayExpr = exp.substring(0, exp.length-1) // take expression without last char of operator
                    var pendingOp = exp.last()
                    Log.d(TAG, "getExpressionType: case \"DISPLAY_TYPE_2\": displayExpr = $displayExpr, pendingOp = $pendingOp")
                    res = ExpressionTypeResult(EXP_TYPE_DISPLAY, displayExpr, pendingOp.toString())
                }
                DISPLAY_TYPE_1 -> { // number & operator & number, e.g. '2.4+44'
                    Log.d(TAG, "getExpressionType: case \"DISPLAY_TYPE_1\"")
                    var matchResult = regex.find(exp, 0)
                    if(matchResult != null) {
                        var pendingOp = matchResult.groupValues[EXP_REGEX_GROUP_IDX].toCharArray()[0]
                        var displayExpr = exp.substring(exp.lastIndexOf(pendingOp)+1, exp.length) // take expression part from 1 char after operator until end
                        res = ExpressionTypeResult(EXP_TYPE_DISPLAY, displayExpr, pendingOp.toString())
                    }else {
                        Log.e(TAG, "getExpressionType: case \"DISPLAY_TYPE_1\": match result is null!")
                    }
                }
                EXP_TYPE_EVAL -> {
                    var evalExp = exp.substring(0, exp.length-1)
                    res = ExpressionTypeResult(expType, evalExp, exp.last().toString())
                }else -> {
                    Log.e(TAG, "getExpressionType: case \"else\"...")
                }
            }
        }
        return res
    }

    data class ExpressionTypeResult(val type : Int, val value: String?, val pendingOperator: String? = null) {
        override fun toString(): String {
            var sb = StringBuilder()
            sb.append("ExpressionTypeResult{")
            sb.append("type = $type,")
            sb.append("value = $value,")
            sb.append("pendingOperator = $pendingOperator}")
            return sb.toString()
        }
    }
}
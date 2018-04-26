package com.saharw.calculator.util

/**
 * Created by saharw on 26/04/2018.
 */
object ExpressionUtil {

    const val EXP_TYPE_ERROR = -1
    const val EXP_TYPE_NUMERIC = 0
    const val EXP_TYPE_NUMERIC_OPERAND = 1
    const val EXP_TYPE_NUMERIC_OPERAND_NUMERIC = 2

    fun validateExpression() {
        //TODO: impl.
    }

    fun getExpressionType(exp: String) : Int {
        var res = EXP_TYPE_ERROR
        //TODO: impl.
        return  res
    }
}
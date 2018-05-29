package com.saharw.calculator.tests

import android.support.test.filters.SmallTest
import android.util.Log
import com.saharw.calculatorsdk.core.Calculator
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by saharw on 29/05/2018.
 */
@SmallTest
class CalculatorAddAndroidUnitTest {

    private val TAG = "com.saharw.calculator.tests.CalculatorAddAndroidUnitTest"
    private lateinit var mCalc : Calculator

    @Before // before each test method
    fun setUp() {
        Log.d(TAG, "setUp")
        mCalc = Calculator()
    }

    @After // after each test method
    fun tearDown(){
        Log.d(TAG, "tearDown")
    }

    @Test
    fun testPositiveNumAddition() {
        Log.d(TAG, "testPositiveNumAddition")
        var exp = "1+2"
        assertEquals(mCalc.eval(exp), 3f)
    }

    @Test
    fun testNegativeNumAddition() {
        Log.d(TAG, "testNegativeNumAddition")
        var exp = "(-1)+(-2)"
        assertEquals(mCalc.eval(exp), -3f)
    }

    @Test
    fun testMixedAdditions() {
        Log.d(TAG, "testMixedAdditions")
        var exp = "1+(-2)"
        assertEquals(mCalc.eval(exp), -10f)
    }
}
package com.saharw.calculator.presentation.main.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import com.saharw.calculator.R
import com.saharw.calculator.entities.CalcButtonsAdapter
import com.saharw.calculator.entities.CalculatorButton
import com.saharw.calculator.presentation.base.IView
import io.reactivex.subjects.PublishSubject

/**
 * Created by saharw on 22/04/2018.
 */
class MainView : Fragment(), IView, View.OnClickListener {

    private lateinit var hostingActivity: AppCompatActivity
    private val TAG = "MainView"

    // UI components
    private lateinit var mButtonsGridView: GridView
    private lateinit var mOutputMain: TextView
    private lateinit var mOutputTop: TextView

    var mButtonClickSubject = PublishSubject.create<CalculatorButton>()
    var clearButtonStr = "CLR"

    private var mBtnsList = listOf(
            CalculatorButton("1"), CalculatorButton("2"), CalculatorButton("3"), CalculatorButton("+"),
            CalculatorButton("4"), CalculatorButton("5"), CalculatorButton("6"), CalculatorButton("-"),
            CalculatorButton("7"), CalculatorButton("8"), CalculatorButton("9"), CalculatorButton("/"),
            CalculatorButton("."), CalculatorButton("0"), CalculatorButton("="), CalculatorButton("*"),
            CalculatorButton(clearButtonStr)
    )
    private lateinit var mBtnsGridAdapater: CalcButtonsAdapter

    override fun onViewCreate(activity: AppCompatActivity, layoutId: Int) {
        Log.d(TAG, "onCreateView")
        this.hostingActivity = activity
        hostingActivity.supportFragmentManager.beginTransaction().add(layoutId, this).addToBackStack(TAG).commit()
    }

    override fun onViewResume() {
        Log.d(TAG, "onViewResume")
    }


    override fun onViewDestroy() {
        Log.d(TAG, "onViewDestroy")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        val view = inflater?.inflate(R.layout.main_fragment, container, false)
        if (view != null) {
            initUIComponents(view)
        }
        return view
    }

    private fun initUIComponents(view: View) {
        Log.d(TAG, "initUIComponents")

        mOutputMain = view.findViewById(R.id.txtV_output_main)
        mOutputTop = view.findViewById(R.id.txtV_output_top)

        mButtonsGridView = view.findViewById(R.id.gridv_btns_area)
        mBtnsGridAdapater = CalcButtonsAdapter(mBtnsList, activity, R.layout.calc_button_item, this)
        mButtonsGridView.adapter = mBtnsGridAdapater
    }

    override fun onClick(v: View?) {
        var viewHolder = v?.tag as CalcButtonsAdapter.ViewHolder
        var calcButton = viewHolder.mCalcButton
        Log.d(TAG, "onClick: btn.txt = ${calcButton.`val`}")
        mButtonClickSubject.onNext(calcButton)
    }

    fun setMainOutputValue(resStr: String){
        Log.d(TAG, "setMainOutputValue: resStr = $resStr")
        var resStr = resStr.trim()
        activity.runOnUiThread {

            // check first if res is a number
            var isOnlyDigitsOrDecimalDot = resStr.length == resStr.filter { it.isDigit() || it == '.' }.length
            if(isOnlyDigitsOrDecimalDot){

                // check if float or can be casted to int
                if(resStr.contains(".")){
                    mOutputMain.text = resStr.toFloat().toString()
                }else {
                    mOutputMain.text = resStr.toInt().toString()
                }
            }else {
                mOutputMain.text = resStr
            }
        }
    }

    fun setTopOutputValue(resStr : String) {
        Log.d(TAG, "setTopOutputValue: resStr = $resStr")
        activity.runOnUiThread {
            mOutputTop.text = resStr
        }
    }
}
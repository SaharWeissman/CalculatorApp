package com.saharw.calculator.presentation.main.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saharw.calculator.R
import com.saharw.calculator.presentation.base.IView

/**
 * Created by saharw on 22/04/2018.
 */
class MainView : Fragment(), IView{

    lateinit var hostingActivity : AppCompatActivity
    private val TAG = "MainView"

    override fun onCreate(activity: AppCompatActivity, layoutId : Int) {
        Log.d(TAG, "onCreateView")
        this.hostingActivity = activity
        hostingActivity.supportFragmentManager.beginTransaction().add(layoutId, this).addToBackStack(TAG).commit()
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        val view = inflater?.inflate(R.layout.main_fragment, container, false)
        return view
    }
}
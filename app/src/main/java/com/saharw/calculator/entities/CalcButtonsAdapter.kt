package com.saharw.calculator.entities

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import com.saharw.calculator.R

/**
 * Created by saharw on 25/04/2018.
 */
class CalcButtonsAdapter(val data: List<CalculatorButton>, private val ctxt: Context, private val layoutId: Int, private val onClickListener: View.OnClickListener) :
        BaseAdapter() {
    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0L
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder : ViewHolder
        var convertView = convertView
        if(convertView == null){
            convertView = (ctxt as Activity).layoutInflater.inflate(layoutId, parent, false)
            viewHolder = ViewHolder()
            viewHolder.mBtn = convertView!!.findViewById(R.id.calc_button)
            convertView.tag = viewHolder
            convertView.setOnClickListener(onClickListener)
        }else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.mBtn.text = data[position].`val`
        viewHolder.mCalcButton = data[position]
        return convertView
    }

    class ViewHolder {
        lateinit var mBtn : Button
        lateinit var mCalcButton : CalculatorButton
    }
}
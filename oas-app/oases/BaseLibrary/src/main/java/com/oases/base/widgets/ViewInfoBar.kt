package com.oases.base.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.oases.base.R
import kotlinx.android.synthetic.main.layout_info_bar.view.*

class ViewInfoBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var mName: CharSequence? = null
    private var mValue: CharSequence? = null


    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewInfoBar)
        mName = typedArray.getText(R.styleable.ViewInfoBar_viewName)
        mValue = typedArray.getText(R.styleable.ViewInfoBar_viewValue)
        initView()
        typedArray.recycle()
    }

    //初始化视图
    private fun initView() {
        View.inflate(context, R.layout.layout_view_info_bar, this)

        mName?.let {
            mNameTv.text = it
        }

        mValue?.let {
            mValueTv.text = it
        }
    }

    fun setValue(text:String) {
        mValueTv.text = text
    }

    fun getValue():String = mValueTv.text.toString()
}

package com.oases.base.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.oases.base.R

class RegisterBarText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        initView()
    }

    private  fun initView(){
        View.inflate(context, R.layout.register_bar_text, this)
    }
}
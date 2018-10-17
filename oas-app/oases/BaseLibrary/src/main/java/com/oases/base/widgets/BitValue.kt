package com.oases.base.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.oases.base.R
import kotlinx.android.synthetic.main.layout_bit_value.view.*


class BitValue @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private var mIcon: Drawable? = null
    private var mName: CharSequence? = null
    private var mValue: CharSequence? = null
    private var mEqualsValue: CharSequence? = null


    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BitValue)
        mIcon = typedArray.getDrawable(R.styleable.BitValue_icon)
        mName = typedArray.getText(R.styleable.BitValue_bitName)
        mValue = typedArray.getText(R.styleable.BitValue_bitValue)
        mEqualsValue = typedArray.getText(R.styleable.BitValue_bitEqualsValue)
        initView()
        typedArray.recycle()
    }

    //初始化视图
    private fun initView() {
        View.inflate(context, R.layout.layout_bit_value, this)

        mName?.let {
            mNameTv.text = it
        }

        mValue?.let {
            mValueTv.text = it
        }

        mIcon?.let {
            mIconIv.setImageDrawable(it)
        }

        mEqualsValue?.let {
            mEqualsValueTv.text = it
        }
    }

    fun setValue(value: Double){
        mValueTv.text = value.toString()
    }

    fun setEqualsValue(value: Double){
        mEqualsValueTv.text = "≈ ¥ " + value.toString()
    }
    fun getValue():String{
        return  mValueTv.text.toString()
    }
    fun getEqualsValue():String{
        return  mEqualsValueTv.text.toString()
    }

}

package com.oases.base.widgets

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.oases.base.R
import kotlinx.android.synthetic.main.exchange_item.view.*
import kotlinx.android.synthetic.main.layout_label_textview.view.*

class ExchangeItem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var mLeftTopText: CharSequence? = null
    private var mRightTopText: CharSequence? = null
    private var mLeftBottomText: CharSequence? = null
    private var mRightBottomText: CharSequence? = null
    private var mNotShowRight:  Boolean = false
    private var mRightTopTextColor: Int ?= 0


    //初始化属性
    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExchangeItem)
        mLeftTopText = typedArray.getText(R.styleable.ExchangeItem_leftTop)
        mRightTopText = typedArray.getText(R.styleable.ExchangeItem_rightTop)
        mLeftBottomText = typedArray.getText(R.styleable.ExchangeItem_leftBottom)
        mRightBottomText = typedArray.getText(R.styleable.ExchangeItem_rightBottom)
        mNotShowRight  = typedArray.getBoolean(R.styleable.ExchangeItem_notShowRight, false)
        mRightTopTextColor = typedArray.getColor(R.styleable.ExchangeItem_rightTopTextColor, ContextCompat.getColor(context, R.color.common_black))
        initView()
        typedArray.recycle()
    }

    //初始化视图
    private fun initView() {
        View.inflate(context, R.layout.exchange_item, this)

        mLeftTopText?.let {
            mLeftTopTv.text = it
        }

        mRightTopText?.let {
            mRightTopTv.text = it
        }

        if (mNotShowRight){
            mRightTopTv.setCompoundDrawables(null, null, null, null)
        }

        mLeftBottomText?.let {
            mLeftBottomTv.text = it
        }

        mRightBottomText?.let {
            mRightBottomTv.text = it
        }

        mRightTopTextColor?.let{
            mRightTopTv.setTextColor(it)
        }
    }

    /*
        设置内容文本
     */
    fun setLeftTopText(text:String) {
        mLeftTopTv.text = text
    }

    fun setLeftBottomText(text:String) {
        mLeftBottomTv.text = text
    }

    fun setRightTopText(text:String) {
        mRightTopTv.text = text
    }

    fun setRightBottomText(text:String) {
        mRightBottomTv.text = text
    }

    fun getRightTopText(): String{
        return  mRightTopTv.text.toString()
    }
    fun setRightTopTextColor(){
        var text=mRightTopTv.text.toString()
        if(text.startsWith("-")){
            mRightTopTv.setTextColor(ContextCompat.getColor(context, R.color.common_red))
        }else{
            mRightTopTv.setTextColor(ContextCompat.getColor(context, R.color.common_green))
        }
    }
    fun setRightTopMoveDown(){
        mRightTopTv.setPaddingRelative(0,0,0,10)
        mRightTopTv.setTextSize(20.toFloat())
    }

    fun setTimeTextSize(){
        mLeftBottomTv.setTextSize(10.toFloat())
        mRightBottomTv.setTextSize(10.toFloat())
    }

    fun setTimeTextMoveDown(){
        mLeftBottomTv.setPaddingRelative(0,15,0,0)
        mRightBottomTv.setPaddingRelative(0,15,0,0)
    }
}
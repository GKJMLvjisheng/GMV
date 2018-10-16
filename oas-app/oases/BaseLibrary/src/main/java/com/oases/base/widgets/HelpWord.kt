package com.oases.base.widgets

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.oases.base.R

import kotlinx.android.synthetic.main.layout_help_word.view.*

class HelpWord @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var mText: CharSequence? = null
    private var mState: State = State.NORMAL
   // private var mBackgroundColor: Int
  //  private lateinit var
    //初始化属性
    enum class State{
       NORMAL,
       PENDING,
       CLICKED,
       SELECTED
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HelpWord)
        this.mText = typedArray.getText(R.styleable.HelpWord_text)
        initView()
        typedArray.recycle()
    }

    //初始化视图
    private fun initView() {
        View.inflate(context, R.layout.layout_help_word, this)

        mText?.let {
           mHelpWordTv.text = it
        }
    }

    fun setText(text:String){
        mHelpWordTv.text = text
    }
    fun getTextWidth(text:String):Int{
        val bounds = Rect()
        val textPaint = mHelpWordTv.getPaint()
        textPaint.getTextBounds(text, 0, text.length, bounds)
        return bounds.height()
    }

    fun getText():String{
        return mHelpWordTv.text.toString()
    }
    fun setClickable(){
       mHelpWordTv.isClickable = false
    }
    fun setState(state:State){
    //    Log.d("zbb", "enter set state")
      //  Log.d("zbb", "${mHelpWordTv.text} set state $state ")
        var isClickable = false
        when (state){
            State.NORMAL -> mHelpWordTv.setBackgroundResource(R.drawable.help_word_background)//mHelpWordTv.setBackgroundResource(R.drawable.help_word_background)
            State.PENDING -> {
                mHelpWordTv.setBackgroundResource(R.drawable.help_word_background_pending)
              //  isClickable = false
            }
            State.CLICKED -> {
                mHelpWordTv.setBackgroundResource(R.drawable.help_word_background_clicked)
                isClickable = true
            }
            State.SELECTED -> mHelpWordTv.setBackgroundResource(R.drawable.help_word_background_selected)
        }
        mHelpWordTv.isClickable = isClickable
    }
/*
    fun setBackgoundColor(state:State){
        Log.d("zbb", "set background $state")
        when (state){
            State.NORMAL -> mHelpWordTv.setBackgroundColor(R.drawable.help_word_background)
            State.PENDING -> mHelpWordTv.setBackgroundColor(R.drawable.help_word_background_pending)
            State.CLICKED -> mHelpWordTv.setBackgroundColor(R.drawable.help_word_background_clicked)
            State.SELECTED -> mHelpWordTv.setBackgroundColor(R.drawable.help_word_background_selected)
        }
    }*/
    /*
    overridefun  setClickable(clickable:Boolean){
        mHelpWordTv.isClickable = clickable
    }*/
    fun getSize():Int{
        return mHelpWordTv.width
    }
}
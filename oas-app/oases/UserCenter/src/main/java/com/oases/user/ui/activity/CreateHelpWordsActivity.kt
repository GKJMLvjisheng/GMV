package com.oases.user.ui.activity


import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.oases.base.ui.activity.BaseActivity
import com.oases.base.widgets.HelpWord
import com.oases.user.R
import kotlinx.android.synthetic.main.activity_create_help_words.*
import org.jetbrains.anko.startActivity


class CreateHelpWordsActivity : BaseActivity(), View.OnClickListener{
    private lateinit var helpWords:Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_help_words)
        
        initView()
    }

    private fun initView() {
        mNextBtn.setOnClickListener(this)
        createHelpWordsContainer()
    }

    private fun createHelpWordsContainer()
    {
        helpWords = intent.getStringArrayExtra("help_words")

        val containerMargin = mContainerLayout.getLayoutParams() as ViewGroup.MarginLayoutParams
        var mHelpWordsContainerWidth = resources.displayMetrics.widthPixels - containerMargin.leftMargin - containerMargin.rightMargin
        var line = createHelpWordsLine()

        for (word in helpWords){
            val wordTv = HelpWord(this)
            wordTv.setText(word)
            line.addView(wordTv)
            line.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            if (line.measuredWidth > mHelpWordsContainerWidth) {
                mHelpWordsContainer.addView(line)
                line.removeView(wordTv)
                line = createHelpWordsLine()
                line.addView(wordTv)
            }
        }
        mHelpWordsContainer.addView(line)
    }

    private fun createHelpWordsLine():LinearLayout{
        val line = LinearLayout(this)
        var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        line.measure(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 8, 0, 0)
        layoutParams.gravity = Gravity.CENTER
        line.layoutParams = layoutParams
        return line
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.mNextBtn -> {
                startActivity<ConfirmHelpWordsActivity>("help_words" to helpWords)
            }
        }
    }
}

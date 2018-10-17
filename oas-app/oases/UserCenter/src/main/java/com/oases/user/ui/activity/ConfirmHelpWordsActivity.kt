package com.oases.user.ui.activity


import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.ViewGroup
import android.widget.LinearLayout
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.base.widgets.HelpWord
import com.oases.user.R
import com.oases.user.data.protocol.UserInfo
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.LoginPresenter
import com.oases.user.presenter.RegisterConfirmPresenter
import com.oases.user.presenter.view.LoginView
import com.oases.user.presenter.view.RegisterConfirmView
import kotlinx.android.synthetic.main.activity_confirm_help_words.*
import org.jetbrains.anko.forEachChild
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class ConfirmHelpWordsActivity : BaseMvpActivity<RegisterConfirmPresenter>(), RegisterConfirmView, View.OnClickListener {
    private lateinit var helpWords:Array<String>
 //   private var lastView:HelpWord? = null
    private var clickHistory = ArrayList<HelpWord>()
    private  var rollScroll = Runnable{
        fun run(){
            mScrollView.fullScroll(View.FOCUS_RIGHT)
        }}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_help_words)
        initView()
        mConfirmNextBtn.onClick(this)
    }

    private fun initView() {
        createHelpWordsContainer()
    }
    override fun injectComponent() {
        DaggerUserComponent
                .builder()
                .activityComponent(mActivityComponent)
                .userModule(UserModule())
                .build()
                .inject(this)
        mPresenter.mView = this
    }

    override fun onRegisterConfirmResult(result: Boolean) {
        setResult(Activity.RESULT_OK)
        startActivity(intentFor<RegisterSucceedActivity>())
        finish()
    }

    override fun onClick(view: View?) {
        Log.d("zbb", view?.id.toString())
        when (view!!.id)
        {
            in 0..12 -> {
                var clickedHelpWord = view as HelpWord
                if (clickedHelpWord.getText() == clickHistory.lastOrNull()?.getText()){
                    mHelpWordsSelectedList.removeViewAt(mHelpWordsSelectedList.childCount - 1)
                    clickedHelpWord.setState(HelpWord.State.NORMAL)
                    clickHistory.removeAt(clickHistory.lastIndex)
                    clickHistory.lastOrNull()?.setState(HelpWord.State.PENDING)
                    return
                }

                var selectedWord = HelpWord(this)
                selectedWord.setText(clickedHelpWord.getText())
                selectedWord.setState(HelpWord.State.SELECTED)
                mHelpWordsSelectedList.addView(selectedWord)

                clickedHelpWord.setState(HelpWord.State.PENDING)
                clickHistory.lastOrNull()?.setState(HelpWord.State.CLICKED)
                clickHistory.add(clickedHelpWord)

               mScrollView.requestChildFocus(selectedWord, selectedWord)
            }

            mConfirmNextBtn.id -> {
                var  selectedWords = ArrayList<String>()
                mHelpWordsSelectedList.forEachChild {
                    selectedWords.add((it as HelpWord).getText())}
                Log.d("zbb", "select words ${selectedWords.toString()}")
                Log.d("zbb", "help words ${helpWords.toList().toString()}")
                if (selectedWords == helpWords.toList()){
                    mPresenter.registerConfirm(AppPrefsUtils.getString(BaseConstant.USER_UUID))
                }else {
                    toast("Wrong help word sequence")
                }
            }
        }
    }


    private fun createHelpWordsContainer()
    {
        helpWords = intent.getStringArrayExtra("help_words")
        var shuffHelpWords = helpWords.sortedArray()
       // var shuffHelpWords = helpWords.copyOf()

        val containerMargin = mContainerLayout.getLayoutParams() as ViewGroup.MarginLayoutParams

        var mHelpWordsContainerWidth = resources.displayMetrics.widthPixels - containerMargin.leftMargin - containerMargin.rightMargin
        var line = createHelpWordsLine()
        for ((index, word) in shuffHelpWords.withIndex()){
            val wordTv = HelpWord(this)
            wordTv.setText(word)
            wordTv.id = index
            wordTv.setOnClickListener(this)
            line.addView(wordTv)
            line.measure(makeMeasureSpec(0, UNSPECIFIED), makeMeasureSpec(0, UNSPECIFIED))
            Log.d("zbb ", "${line.measuredWidth}")
            if (line.measuredWidth > mHelpWordsContainerWidth) {
                mHelpWordsContainer.addView(line)
                line.removeView(wordTv)
                line = createHelpWordsLine()
                line.addView(wordTv)
            }
        }
        mHelpWordsContainer.addView(line)
    }

    private fun createHelpWordsLine(): LinearLayout {
        val line = LinearLayout(this)
        var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 8, 0, 0)
        layoutParams.gravity = Gravity.CENTER_VERTICAL
        line.layoutParams = layoutParams
        return line
    }
}

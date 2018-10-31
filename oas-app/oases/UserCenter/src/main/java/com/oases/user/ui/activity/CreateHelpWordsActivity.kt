package com.oases.user.ui.activity


import android.app.AlertDialog
import android.os.Bundle
import android.support.design.R.id.container
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.oases.base.common.BaseConstant
import com.oases.base.ui.activity.BaseActivity
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.base.widgets.HelpWord
import com.oases.user.R
import com.oases.user.data.protocol.confirmOldPwdResp
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.CreateHelpWordPresenter
import com.oases.user.presenter.SetPasswordPresenter
import com.oases.user.presenter.view.CreateHelpWordView
import com.oases.user.presenter.view.SetPasswordView
import kotlinx.android.synthetic.main.activity_create_help_words.*
import kotlinx.android.synthetic.main.activity_set_password.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class CreateHelpWordsActivity : BaseMvpActivity<CreateHelpWordPresenter>(), CreateHelpWordView, View.OnClickListener{
    private lateinit var helpWords:Array<String>
    private lateinit var alertDialog:AlertDialog.Builder
    private lateinit var dialog:AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_help_words)
        
        initView()
    }

    private fun initView() {
        mNextBtn.setOnClickListener(this)
        var times:String = getIntent().getStringExtra("register_times")
        Log.i("zhtr","times3=".plus(times))
        if(times.toInt()> 1){
            clearView()
            getPasswordVerify()
        }else{
            createHelpWordsContainer()
        }
    }

    private fun getPasswordVerify(){
        var view1:View = LayoutInflater.from(this).inflate(R.layout.user_password_verify_dialog, null)
        var submitBtn = view1.findViewById(R.id.mSubmitBtn) as Button
        var mCancelBtn = view1.findViewById(R.id.mCancelBtn) as Button
        var editText = view1.findViewById(R.id.mPwd) as EditText

        alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("密码验证")
        alertDialog.setView(view1)
        dialog = alertDialog?.create() as AlertDialog
        submitBtn.setOnClickListener {
            //验证密码
            var mOldPwd:String = editText.getText().toString()
            var userName = AppPrefsUtils.getString(BaseConstant.USER_NAME)
            if(TextUtils.isEmpty(mOldPwd)){
                editText.setError("密码不能为空")
            }else{
                mPresenter.verifyPassword(userName,mOldPwd)
               /* if(mOldPwd == AppPrefsUtils.getString(BaseConstant.USER_PASSWORD)){
                        toast("密码验证成功")
                        dialog.dismiss()
                        showView()
                        createHelpWordsContainer()
                }else{
                    toast("密码验证失败")
                }*/
            }
        }
        mCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun createHelpWordsContainer()
    {
        helpWords = getIntent().getStringArrayExtra("help_words")

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
    override fun injectComponent() {
        DaggerUserComponent
                .builder()
                .activityComponent(mActivityComponent)
                .userModule(UserModule())
                .build()
                .inject(this)
        mPresenter.mView = this
    }
    override fun onConfirmOldPwdResult(result: confirmOldPwdResp) {
        if (result.state=="true"){
            toast("密码验证成功")
            dialog.dismiss()
            showView()
            createHelpWordsContainer()
        }else{
            toast("密码验证失败")
        }
    }
    private fun clearView(){
        mProcessBottom.visibility = View.GONE
        mNote.visibility = View.GONE
        mNextBtn.visibility = View.GONE
    }
    private fun showView(){
        mProcessBottom.visibility = View.VISIBLE
        mNote.visibility = View.VISIBLE
        mNextBtn.visibility = View.VISIBLE
    }
}

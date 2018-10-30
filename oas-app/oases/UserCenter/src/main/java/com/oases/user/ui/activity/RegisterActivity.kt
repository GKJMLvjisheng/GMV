package com.oases.user.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.R
import com.oases.user.data.protocol.RegisterResp
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.RegisterPresenter
import com.oases.user.presenter.view.RegisterView
import com.oases.user.utils.getDeviceId
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.startActivity
import java.util.regex.Pattern

class RegisterActivity: BaseMvpActivity<RegisterPresenter>(), RegisterView, View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
    }

    private fun initView() {
        mRegisterBtn.onClick(this)
        mPwdEt.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                return@OnEditorActionListener true
            }
            false
        })
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

    override fun onRegisterResult(result: RegisterResp) {
        Log.d("zbb", "register successfully")
        AppPrefsUtils.putString(BaseConstant.USER_NAME, mNameEt.text.toString())
        AppPrefsUtils.putString(BaseConstant.USER_PASSWORD, mPwdEt.text.toString())
        AppPrefsUtils.putString(BaseConstant.USER_UUID, result.uuid)
        //AppPrefsUtils.putString(BaseConstant.USER_INVITE_CODE,mInviteFrom.text.toString())
        startActivity<CreateHelpWordsActivity>("help_words" to result.mnemonicList.toTypedArray())
    }

    override fun onClick(view:View){
        when(view.id){
            R.id.mRegisterBtn -> {
                if (isInputOk())
                {
                    mPresenter.register(mNameEt.text.toString(), mPwdEt.text.toString(), mInviteFrom.text.toString(), getDeviceId(this))
                }
            }
        }
    }
    fun isInputOk():Boolean{
        val userName = mNameEt.getText().toString()
        val password = mPwdEt.getText().toString()
        val confirmPwd = mPwdConfirmEt.getText().toString()

        if (TextUtils.isEmpty(userName)) {
            mNameEt.setError(getString(R.string.error_field_required))
            mNameEt.requestFocus()
            return false
        }
        if(!regularExpressionValidate(userName,BaseConstant.SMALL_ENGLISH_NUMBER)){
            mNameEt.setError(getString(R.string.error_username_incorrect))
            mNameEt.requestFocus()
            return false
        }

       if (TextUtils.isEmpty(password))
        {
            mPwdEt.setError(getString(R.string.error_empty_password))
            mPwdEt.requestFocus()
            return false
        }

        else if (!isPasswordValid(password)) {
            mPwdEt.setError(getString(R.string.error_invalid_password))
            mPwdEt.requestFocus()
            return false
        }

        else if (confirmPwd != password) {
           mPwdConfirmEt.setError(getString(R.string.error_password_is_not_equal))
           mPwdConfirmEt.requestFocus()
            return false
        }
        return true
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    private  fun regularExpressionValidate(value:String,exp:String):Boolean{
        val pattern: Pattern = Pattern.compile(exp)
        return pattern.matcher(value.toString()).matches()
    }

}
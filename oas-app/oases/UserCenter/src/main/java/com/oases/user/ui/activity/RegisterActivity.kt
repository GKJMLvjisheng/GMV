package com.oases.user.ui.activity

import android.content.Intent
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
        var uuid : String = AppPrefsUtils.getString(BaseConstant.USER_UUID)
        //Log.i("zhtr","uuid=".plus(uuid))
        if(uuid.isNullOrBlank()){
            AppPrefsUtils.putInt(BaseConstant.USER_REGISTER_CLICK_NUMBERS, 0) //用户点击获取助记词后有uuid，第一次进入注册页面为0
        }
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
        var times:Int = AppPrefsUtils.getInt(BaseConstant.USER_REGISTER_CLICK_NUMBERS)
        times++
        AppPrefsUtils.putInt(BaseConstant.USER_REGISTER_CLICK_NUMBERS,times)
        //AppPrefsUtils.putString(BaseConstant.USER_INVITE_CODE,mInviteFrom.text.toString())
        //startActivity<CreateHelpWordsActivity>("help_words" to result.mnemonicList.toTypedArray(),"register_times" to times.toString())
        var intent = Intent(this,CreateHelpWordsActivity::class.java)
        var bundle =Bundle()
        bundle.putStringArray("help_words",result.mnemonicList.toTypedArray())
        bundle.putString("register_times",times.toString())
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onClick(view:View){
        when(view.id){
            R.id.mRegisterBtn -> {
                if (isInputOk())
                {
                    var uuid : String? = AppPrefsUtils.getString(BaseConstant.USER_UUID)
                    var name : String = AppPrefsUtils.getString(BaseConstant.USER_NAME)
                    if(name != mNameEt.text.toString()){ //不同用户注册
                        AppPrefsUtils.putInt(BaseConstant.USER_REGISTER_CLICK_NUMBERS, 0)
                        uuid = null
                    }
                    mPresenter.register(mNameEt.text.toString(), mPwdEt.text.toString(), mInviteFrom.text.toString(), null)//getDeviceId(this)注册不传imei
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
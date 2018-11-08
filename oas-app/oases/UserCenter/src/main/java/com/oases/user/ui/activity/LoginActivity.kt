package com.oases.user.ui.activity

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.oases.base.common.AppManager
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.provider.common.isLogined
import com.oases.provider.router.RouterPath
import com.oases.user.R
import com.oases.user.data.protocol.UserInfo
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.LoginPresenter
import com.oases.user.presenter.view.LoginView
import com.oases.user.utils.getDeviceId
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*
import android.util.Base64

@Route(path = RouterPath.UserCenter.PATH_LOGIN)
class LoginActivity : BaseMvpActivity<LoginPresenter>(), LoginView{
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var pressTime:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
    }

    private fun initView() {

        if (AppPrefsUtils.getString(BaseConstant.USER_NAME) != ""){
            mUserName.setText(AppPrefsUtils.getString(BaseConstant.USER_NAME))
        }

        if( AppPrefsUtils.getBoolean(BaseConstant.PASSWORD_IS_REMEMBER)){
            rememberPassword.setChecked(true)
            var pass = AppPrefsUtils.getString(BaseConstant.USER_PASSWORD)
            //var afterP = Base64.decode(AppPrefsUtils.getString(BaseConstant.USER_PASSWORD),0).toString()
            //Log.i("zbbcun2",pass)
            //Log.i("zbbcun3",afterP)
            mPwd.setText(pass)
        }

        mLoginBtn.setOnClickListener {
            attemptLogin()
        }
        mRegisterBtn.setOnClickListener { startActivity<RegisterActivity>() }
      //  mUserName.onRightDrawableClickListener{mUserName.text.clear()}

        mPwd.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE ) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        mForgetPwdBtn.onClick {
            startActivity<ForgetPwdOneActivity>()
        }
        rememberPassword.onClick{
            if(rememberPassword.isChecked()){
                AppPrefsUtils.putBoolean(BaseConstant.PASSWORD_IS_REMEMBER,true)
            }else{
                AppPrefsUtils.putBoolean(BaseConstant.PASSWORD_IS_REMEMBER,false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("zbb", "login resume")
        AppPrefsUtils.putInt(BaseConstant.USER_REGISTER_CLICK_NUMBERS, 0)

        checkLogined()
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

    override fun onLoginResult(result:UserInfo) {
            toast("登录成功")
            if(rememberPassword.isChecked()){
               // Log.i("zbbcun1", Base64.encode(mPwd.text.toString().toByteArray(),0).toString())
                AppPrefsUtils.putString(BaseConstant.USER_PASSWORD,mPwd.text.toString())
            }else{
                AppPrefsUtils.putString(BaseConstant.USER_PASSWORD,"")
            }
            setResult(Activity.RESULT_OK)
            checkLogined()
    }

   override fun onLoginFailed(e: Throwable) {

    }


    private fun attemptLogin() {
        mUserName.setError(null)
        mPwd.setError(null)

        // Store values at the time of the login attempt.
        val userName = mUserName.getText().toString()
        val password = mPwd.getText().toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPwd.setError(getString(R.string.error_invalid_password))
            focusView = mPwd
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mUserName.setError(getString(R.string.error_field_required))
            focusView = mUserName
            cancel = true
        } else if (!isNameValid(userName)) {
            mUserName.setError(getString(R.string.error_invalid_name))
            focusView = mUserName
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mPresenter.login(mUserName.text.toString(), mPwd.text.toString(), getDeviceId(this))
        }
    }

    private fun isNameValid(@Suppress("UNUSED_PARAMETER")name: String): Boolean {
        //TODO: Replace this with your own logic
        return true
    }

    private fun isPasswordValid(@Suppress("UNUSED_PARAMETER")password: String): Boolean {
        //TODO: Replace this with your own logic
        return true
    }

    private fun checkLogined(){
        //getActiveActivity()
        if (isLogined()){
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onBackPressed() {
       // super.onBackPressed()
        /*val time = System.currentTimeMillis()
       // setResult(Activity.RESULT_CANCELED)
        //finish()
        if (time - pressTime > 2000) {
            toast("再按一次退出程序")
            pressTime = time
        } else {
            Log.d("zbb", "exit app")
            AppPrefsUtils.putString(BaseConstant.USER_TOKEN, "")
            finish()
            AppManager.instance.exitApp(this)
        }*/

    }

    private fun getActiveActivity(){
       var stack: Stack<Activity> = AppManager.instance.getAllActivity()
        for(s in stack){
            Log.i("zbb","now activity".plus(s))
        }
    }


}

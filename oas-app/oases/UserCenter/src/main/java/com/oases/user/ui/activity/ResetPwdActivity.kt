package com.oases.user.ui.activity

import android.os.Bundle
import android.text.TextUtils
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.user.R
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.ResetPwdPresenter
import com.oases.user.presenter.view.ResetPwdView
import kotlinx.android.synthetic.main.activity_reset_pwd.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast

class ResetPwdActivity : BaseMvpActivity<ResetPwdPresenter>(), ResetPwdView {

    var userName:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pwd)
        var bundle = this.intent.extras
        userName=(bundle.get("userName").toString())
        mUserName.setText(userName)
        /*if (userName.length <= 4){
        mUserName.setText(userName)
        }
        if (userName.length > 4){
            // 可以用 StringBuilder 这个类，里面有一个接口replace，如下
            val userNameShow = StringBuilder(userName)
            userNameShow.replace(2, userName.length-3, "*")
            mUserName.setText(userNameShow)
        }*/
        initView()
    }

    private fun initView() {
        mResetPwdBtn.onClick {
            if(isInputOk()) {
                mPresenter.resetPwd(userName,mNewPwd.text.toString())
            }
        }
    }

    private fun isInputOk():Boolean{
        val newPwd= mNewPwd.getText().toString()
        val newPwdConfirm= mNewPwdConfirm.getText().toString()
        if (TextUtils.isEmpty(newPwd)) {
            mNewPwd.setError("密码不能为空")
            mNewPwd.requestFocus()
            return false
        }else if(!isNewPwdValid(newPwd)){
            mNewPwd.setError("密码太短")
            mNewPwd.requestFocus()
            return false
        }else if (newPwdConfirm != newPwd) {
            mNewPwdConfirm.setError(getString(R.string.error_password_is_not_equal))
            mNewPwdConfirm.requestFocus()
            return false
        }
        return true
    }

    private fun isNewPwdValid(newPwd: String): Boolean {
        return newPwd.length > 4
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

    override fun onResetPwdResult(result: Int) {
        if (result==1){
            toast("密码重置失败")
        }else {
            toast("密码重置成功，请登陆")
            startActivity(intentFor<LoginActivity>().singleTop().clearTop())
        }
    }
}

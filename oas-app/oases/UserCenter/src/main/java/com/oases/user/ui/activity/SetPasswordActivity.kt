package com.oases.user.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.oases.base.common.BaseConstant
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.R
import com.oases.user.data.protocol.confirmOldPwdResp
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.SetPasswordPresenter
import com.oases.user.presenter.view.SetPasswordView
import kotlinx.android.synthetic.main.activity_set_password.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast

class SetPasswordActivity : BaseMvpActivity<SetPasswordPresenter>(), SetPasswordView {

    var userName = AppPrefsUtils.getString(BaseConstant.USER_NAME)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_password)
        initView()
    }

    private fun initView(){
        mHeadBar.onClickRightTv {
            if (isInputOk()) {
                mPresenter.confirmOldPwd(userName,mOldPwd.text.toString())
            }
        }
    }

    private fun isInputOk():Boolean{
        val oldPwd = mOldPwd.getText().toString()
        val newPwd= mNewPwd.getText().toString()
        val newPwdConfirm= mNewPwdConfirm.getText().toString()
        if (TextUtils.isEmpty(oldPwd)) {
            mOldPwd.setError("旧密码不能为空")
            mOldPwd.requestFocus()
            return false
        }else if (TextUtils.isEmpty(newPwd)) {
            mNewPwd.setError("密码不能为空")
            mNewPwd.requestFocus()
            return false
        }else if(!isNewPwdValid(newPwd)){
            mNewPwd.setError("密码太短")
            mNewPwd.requestFocus()
            return false
        }else if (newPwdConfirm != newPwd) {
            mNewPwdConfirm.setError(getString(com.oases.user.R.string.error_password_is_not_equal))
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

    override fun onConfirmOldPwdResult(result: confirmOldPwdResp) {
        if (result.state=="true"){
             mPresenter.resetPwd(userName, mNewPwd.text.toString())
        }else{
            toast("原密码输入错误")
        }
    }

    override fun onResetPwdResult(result: Int) {
        if (result==1){
            toast("密码重置失败")
        }else {
            toast("密码重置成功")
            startActivity(intentFor<PasswordInSecurityActivity>().singleTop().clearTop())
        }
    }

}

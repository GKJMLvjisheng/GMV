package com.oases.user.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.user.R
import com.oases.user.data.protocol.ForgetPwdOneResp
import com.oases.user.data.protocol.SendMobileResp
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.ForgetPwdOnePresenter
import com.oases.user.presenter.view.ForgetPwdOneView
import kotlinx.android.synthetic.main.activity_forget_pwd_one.*
import org.jetbrains.anko.toast

class ForgetPwdOneActivity : BaseMvpActivity<ForgetPwdOnePresenter>(), ForgetPwdOneView {

    var userName :String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pwd_one)
        initView()
    }

    private fun initView(){
        mForgetPwdOneBtn.onClick {
            mPresenter.inquireUserNameByMobile(mOriginalPhoneNumber.text.toString())
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

    override fun onInquireUserNameByMobileResult(result: ForgetPwdOneResp) {
        if (result.name=="empty"){
            toast("该手机号未绑定账号，无法进行重置密码")
        }else{
            userName=result.name
            toast("正在发送验证码")
            intentMobileOne ()
        }
    }

    private fun intentMobileOne (){
        var intent = Intent(this@ForgetPwdOneActivity,ForgetPwdTwoActivity::class.java)
        var bundle =Bundle()
        bundle.putString("newMobile",mOriginalPhoneNumber.text.toString())
        bundle.putString("userName",userName)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}

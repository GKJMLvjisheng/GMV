package com.oases.user.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.user.R
import com.oases.user.data.protocol.CheckPhoneCodeResp
import com.oases.user.data.protocol.SendMobileResp
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.PhoneStepTwoPresenter
import com.oases.user.presenter.view.CheckPhoneCodeView
import kotlinx.android.synthetic.main.activity_forget_pwd_two.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ForgetPwdTwoActivity : BaseMvpActivity<PhoneStepTwoPresenter>(), CheckPhoneCodeView, View.OnClickListener{

    private var mCurrentNum = 60
    private val TIME: Long = 1000
    var  NewMobile:String = ""
    var  userName:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pwd_two)
        var bundle = this.intent.extras
        NewMobile=(bundle.get("newMobile").toString())
        userName=(bundle.get("userName").toString())
        mOriginalPhoneNumber.setText(NewMobile)
        initView()
        mTimeBtn.setOnClickListener(this)
        mTimeBtn.performClick()   //自动点击事件
    }

    private fun initView() {

        mForgetPwdTwoBtn.onClick{
           // mPresenter.checkPhoneCode(mPhoneCode.text.toString())
            mPresenter.checkPhoneCode(mPhoneCode.text.toString())


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

    override fun onSendMobileResult(result: SendMobileResp) {
        if (result.state=="true") {
            toast("验证码已发送")
        }else{
            toast("验证码发送失败")
        }
    }

    override fun onCheckPhoneCodeResult(result: CheckPhoneCodeResp) {
        if (result.state=="true") {
            intentMobileTwo ()
        } else {
            toast("验证码填写错误")
        }
    }

    private fun intentMobileTwo (){
        var intent = Intent(this@ForgetPwdTwoActivity,ResetPwdActivity::class.java)
        var bundle =Bundle()
        bundle.putString("userName",userName)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mTimeBtn -> {
                if(mTimeBtn.isEnabled){
                    mPhoneCode.postDelayed(mRefreshRunnable,TIME)
                    mPresenter.sendMobile(NewMobile)
                }
            }
        }

    }

    private val mRefreshRunnable: Runnable = object : Runnable {
        override fun run() {
            mTimeBtn.text= mCurrentNum.toString() + "s后重发验证码"
            mTimeBtn.visibility = View.GONE
            if (mCurrentNum == 0) {
                mPhoneCode.removeCallbacks(this)
                mTimeBtn.isEnabled = true
                mTimeBtn.text = "重新发送验证码"
                mCurrentNum = 60
            } else {
                mCurrentNum -= 1
                mTimeBtn.isEnabled = false
                mPhoneCode.postDelayed(this, TIME)
            }
            mTimeBtn.visibility = View.VISIBLE
        }
    }
}

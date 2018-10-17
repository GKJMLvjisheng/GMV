package com.oases.user.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.user.R
import com.oases.user.data.protocol.CheckMailCodeResp
import com.oases.user.data.protocol.SendMailResp
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.MailStepTwoPresenter
import com.oases.user.presenter.view.CheckMailCodeView
import kotlinx.android.synthetic.main.activity_mail_step_two.*
import org.jetbrains.anko.toast

class MailStepTwoActivity : BaseMvpActivity<MailStepTwoPresenter>(), CheckMailCodeView ,View.OnClickListener{

    private var mCurrentNum = 60
    private val TIME: Long = 1000
    var  NewMail:String = ""
    var  mailJumpFlag:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_step_two)
        var bundle = this.intent.extras
        NewMail=(bundle.get("newMail").toString())
        mailJumpFlag =(bundle.get("mailJumpFlag").toString())
        mMailShow1.setText("已向邮箱")
        mMailShow2.setText(NewMail)
        mMailShow3.setText("发送验证码")
        initView()
        mTimeMailBtn.setOnClickListener(this)
        mTimeMailBtn.performClick()   //自动点击事件
    }

    private fun initView() {

        mChangeMailStepTwoBtn.onClick{
            mPresenter.checkMailCode(mMailCode.text.toString())
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

     override fun onSendMailResult(result: SendMailResp) {
        if (result.state=="true") {
         //   intentMailOne ()
            //finish()
           // startActivity<MailStepTwoActivity>()
            toast("验证码已发送")
        }else{
            toast("验证码发送失败")
        }
    }

    override fun onCheckMailCodeResult(result: CheckMailCodeResp) {
        if (result.state=="true") {
            intentMailTwo ()
            //finish()
            //startActivity<DoneMailActivity>()
        } else {
            toast("验证码填写错误")
        }
    }

    private fun intentMailTwo (){
        var intent = Intent(this@MailStepTwoActivity,DoneMailActivity::class.java)
        var bundle =Bundle()
        bundle.putString("newMail",NewMail)
        bundle.putString("mailJumpFlag",mailJumpFlag)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mTimeMailBtn-> {
                if(mTimeMailBtn.isEnabled){
                    mMailCode.postDelayed(mRefreshRunnable,TIME)
                    mPresenter.sendMail(NewMail)
/*                    mMailShow1.setText("已向邮箱")
                    mMailShow2.setText(NewMail)
                    mMailShow3.setText("发送验证码")*/
                }
            }
        }

    }

    private val mRefreshRunnable: Runnable = object : Runnable {
        override fun run() {
            mTimeMailBtn.text= mCurrentNum.toString() + "s后重发验证码"
            mTimeMailBtn.visibility = View.GONE
            if (mCurrentNum == 0) {
                mMailCode.removeCallbacks(this)
                mTimeMailBtn.isEnabled = true
                mTimeMailBtn.text = "重新发送验证码"
                mCurrentNum = 60
            } else {
                mCurrentNum -= 1
                mTimeMailBtn.isEnabled = false
                mMailCode.postDelayed(this, TIME)
            }
            mTimeMailBtn.visibility = View.VISIBLE
        }
    }

}

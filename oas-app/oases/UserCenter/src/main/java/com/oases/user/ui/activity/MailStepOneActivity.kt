package com.oases.user.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.user.R
import com.oases.user.data.protocol.SendMailResp
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.MailStepOnePresenter
import com.oases.user.presenter.view.MailStepOneView
import kotlinx.android.synthetic.main.activity_mail_step_one.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

@Route(path = "/userCenter/MailStepOneActivity")
class MailStepOneActivity : BaseMvpActivity<MailStepOnePresenter>(), MailStepOneView {
   // private val sendUser = 1
   var  mailJumpFlag:String = ""
    var  originalMail:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_step_one)
        mailJumpFlag = intent.extras.getString("mailJumpFlag")
        originalMail = intent.extras.getString("originalMail")
        if (originalMail!="未设置"&& originalMail!=""){
            mShowMailStatus.setText("请输入要更新的邮箱地址")
            mShowCurrentMail.setText("当前已绑定的邮箱地址为："+originalMail)
        }
        initView()
    }


    private fun initView() {
        mChangeMailStepOneBtn.onClick {
            if(isInputOk()) {
                mPresenter.checkMail(mNewMail.text.toString())
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

    override fun onCheckMailResult(result: Int) {
        Log.d("result",result.toString())
        if (result==1) {
            toast("邮箱已存在")
        } else {
            toast("正在发送验证码")
            //mPresenter.sendMail(mNewMail.text.toString())
            intentMailOne ()
        }
    }

    /*override fun onSendMailResult(result: SendMailResp) {
        if (result.state=="true") {
            intentMailOne ()
            //finish()
           // startActivity<MailStepTwoActivity>()
        }else{
            intentMailOne ()
            toast("验证码发送失败，请重新获取")
        }
    }*/

    private fun isInputOk():Boolean{
        val newMail= mNewMail.getText().toString()

        if (TextUtils.isEmpty(newMail)) {
            mNewMail.setError("邮箱不能为空")
            mNewMail.requestFocus()
            return false
        }
        return true
    }

    private fun intentMailOne (){
        var intent = Intent(this@MailStepOneActivity,MailStepTwoActivity::class.java)
        var bundle =Bundle()
        bundle.putString("newMail",mNewMail.text.toString())
        bundle.putString("mailJumpFlag",mailJumpFlag)
        intent.putExtras(bundle)
        startActivity(intent)
        //startActivityForResult(intent, sendUser)
    }

}

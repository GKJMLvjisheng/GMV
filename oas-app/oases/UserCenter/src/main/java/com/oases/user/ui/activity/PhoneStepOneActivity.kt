package com.oases.user.ui.activity

import android.app.FragmentManager
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.provider.router.RouterPath
import com.oases.user.R
import com.oases.user.data.protocol.SendMobileResp
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.PhoneStepOnePresenter
import com.oases.user.presenter.view.PhoneStepOneView
import kotlinx.android.synthetic.main.activity_phone_step_one.*
import kotlinx.android.synthetic.main.activity_user_info.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

@Route(path = "/userCenter/PhoneStepOneActivity")
class PhoneStepOneActivity : BaseMvpActivity<PhoneStepOnePresenter>(), PhoneStepOneView {

    var  phoneJumpFlag:String = ""
    var  originalPhoneNumber:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_step_one)
        phoneJumpFlag = intent.extras.getString("phoneJumpFlag")
        originalPhoneNumber = intent.extras.getString("originalPhoneNumber")
        if (originalPhoneNumber!="未设置"&& originalPhoneNumber!=""){
            mShowPhoneStatus.setText("请输入要更新的手机号")
            mShowCurrentPhone.setText("当前已绑定的手机号为："+originalPhoneNumber)
        }
        initView()
    }


    private fun initView() {
        mChangeMobileStepOneBtn.onClick {
            if(isInputOk()) {
            mPresenter.checkPhone(mNewPhoneNumber.text.toString())
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

    override fun onCheckPhoneResult(result: Int) {
        Log.d("result",result.toString())
        if (result==1) {
            toast("手机号已存在")
           } else {
            toast("正在发送验证码")
           // mPresenter.sendMobile(mNewPhoneNumber.text.toString())
            intentMobileOne ()
           }
    }

    /*override fun onSendMobileResult(result: SendMobileResp) {
        if (result.state=="true") {
            intentMobileOne ()
            //finish()
           // startActivity<PhoneStepTwoActivity>()
        }else{
            intentMobileOne ()
            toast("验证码发送失败,请重新获取")

        }
    }*/

    private fun isInputOk():Boolean{
        val newPhoneNumber= mNewPhoneNumber.getText().toString()

        if (TextUtils.isEmpty(newPhoneNumber)) {
            mNewPhoneNumber.setError("手机号不能为空")
            mNewPhoneNumber.requestFocus()
            return false
        }else if(!isMobileValid(newPhoneNumber)){
            mNewPhoneNumber.setError("请输入格式正确的手机号")
            mNewPhoneNumber.requestFocus()
            return false
        }
        return true
    }

     private fun isMobileValid (newPhoneNumber:String) : Boolean{
         val telRegex = "[1][34578]\\d{9}"          //"[1]"代表第1位为数字1，"[4578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
         return newPhoneNumber.matches(telRegex.toRegex())
    }

    private fun intentMobileOne (){
        var intent = Intent(this@PhoneStepOneActivity,PhoneStepTwoActivity::class.java)
        var bundle =Bundle()
        bundle.putString("newMobile",mNewPhoneNumber.text.toString())
        bundle.putString("phoneJumpFlag",phoneJumpFlag)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}

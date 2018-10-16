package com.oases.user.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.view.postDelayed
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.user.R
import com.oases.user.data.protocol.CheckPhoneCodeResp
import com.oases.user.data.protocol.SendMobileResp
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.PhoneStepTwoPresenter
import com.oases.user.presenter.view.CheckPhoneCodeView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_phone_step_one.*
import kotlinx.android.synthetic.main.activity_phone_step_two.*
import org.jetbrains.anko.toast
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

class PhoneStepTwoActivity :  BaseMvpActivity<PhoneStepTwoPresenter>(), CheckPhoneCodeView,View.OnClickListener{

    private var mCurrentNum = 60
    private val TIME: Long = 1000
    var  NewMobile:String = ""
    var phoneJumpFlag:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_step_two)
        var bundle = this.intent.extras
        NewMobile=(bundle.get("newMobile").toString())
        phoneJumpFlag =(bundle.get("phoneJumpFlag").toString())
        mPhoneShow1.setText("已向手机号")
        mPhoneShow2.setText(NewMobile)
        mPhoneShow3.setText("发送验证码")
        initView()
        mTimeBtn.setOnClickListener(this)
        mTimeBtn.performClick()   //自动点击事件
    }

    private fun initView() {

        mChangeMobileStepTwoBtn.onClick{
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
            //finish()
            // startActivity<PhoneStepTwoActivity>()
        }else{
            toast("验证码发送失败")
        }
    }

    override fun onCheckPhoneCodeResult(result:CheckPhoneCodeResp) {
        if (result.state=="true") {
            intentMobileTwo ()
            //finish()
            //startActivity<DonePhoneActivity>()
        } else {
            toast("验证码填写错误")
        }
    }

    private fun intentMobileTwo (){
        var intent = Intent(this@PhoneStepTwoActivity,DonePhoneActivity::class.java)
        var bundle =Bundle()
        bundle.putString("newMobile",NewMobile)
        bundle.putString("phoneJumpFlag",phoneJumpFlag)
        intent.putExtras(bundle)
        startActivity(intent)
    }

       override fun onClick(v: View?) {
           when (v?.id) {
                R.id.mTimeBtn -> {
                    if(mTimeBtn.isEnabled){
                        mPhoneCode.postDelayed(mRefreshRunnable,TIME)
                        mPresenter.sendMobile(NewMobile)
/*                        mPhoneShow1.setText("已向手机号")
                        mPhoneShow2.setText(NewMobile)
                        mPhoneShow3.setText("发送验证码")*/
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

package com.oases.user.ui.activity

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.R
import com.oases.user.data.protocol.DoneMailResp
import com.oases.user.data.protocol.UserInfo
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.DoneMailPresenter
import com.oases.user.presenter.view.DoneMailView
import kotlinx.android.synthetic.main.activity_done_mail.*
import kotlinx.android.synthetic.main.activity_done_phone.*
import org.jetbrains.anko.*

class DoneMailActivity : BaseMvpActivity<DoneMailPresenter>(), DoneMailView {

    var  NewMail:String = ""
    var  mailJumpFlag:String = ""
    //private val sourceCode:String = "CHEACKEMAIL"     //绑定邮箱6
    private val sourceCode:Int = 6
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done_mail)
        var bundle = this.intent.extras
        NewMail=(bundle.get("newMail").toString())
        mailJumpFlag=(bundle.get("mailJumpFlag").toString())
        //setResult(RESULT_OK, intent)
        initView()
    }


    private fun initView(){
        mDoneMailBtn.onClick {
            mPresenter.doneMail( NewMail)
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


    override fun onDoneMailResult(result: DoneMailResp) {
        AppPrefsUtils.putString(BaseConstant.USER_MAIL_ADDRESS,NewMail)
        //if (mailJumpFlag=="fromUserInfo") {
        //    toast("修改完成")
        //    startActivity(intentFor<UserInfoActivity>().singleTop().clearTop())
        //}

        //if (mailJumpFlag=="fromComputingPower") {
            mPresenter.getReward(sourceCode)
        //    toast("邮箱已关联,奖励只在第一次参与活动时获得哦！")
        //    ARouter.getInstance().build("/computingPower/ComputingPowerMainActivity").navigation()
       // }
    }

    override fun onGetRewardResult(result: Int) {
        if (result == 0){
            if (mailJumpFlag=="fromUserInfo") {
                toast("修改完成，奖励只在第一次修改时获得哦！")
                startActivity(intentFor<UserInfoActivity>().singleTop().clearTop())
            }
            if (mailJumpFlag=="fromComputingPower") {
                toast("邮箱已关联,奖励只在第一次参与活动时获得哦！")
                ARouter.getInstance().build("/computingPower/ComputingPowerMainActivity").navigation()
             }
        }else{
            //toast("活动参与失败")
            if (mailJumpFlag=="fromUserInfo") {
                toast("邮箱修改失败")
            }
            if (mailJumpFlag=="fromComputingPower") {
                toast("邮箱关联失败")
            }
        }
    }

}

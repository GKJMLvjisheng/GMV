package com.oases.user.ui.activity

import android.app.Activity
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.R
import com.oases.user.data.protocol.DonePhoneResp
import com.oases.user.data.protocol.UserInfo
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.DonePhonePresenter
import com.oases.user.presenter.view.DonePhoneView
import kotlinx.android.synthetic.main.activity_done_phone.*
import kotlinx.android.synthetic.main.activity_user_info.*
import org.jetbrains.anko.*

class DonePhoneActivity : BaseMvpActivity<DonePhonePresenter>(), DonePhoneView {

    var  NewMobile:String = ""
    var  phoneJumpFlag:String = ""
    //private val sourceCode:String = "CHEACKMOBILE"     //绑定手机是5
    private val sourceCode:Int = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done_phone)
        var bundle = this.intent.extras
        NewMobile=(bundle.get("newMobile").toString())
        phoneJumpFlag=(bundle.get("phoneJumpFlag").toString())

        initView()
    }


    private fun initView(){
        mDonePhoneBtn.onClick {
            mPresenter.donePhone( NewMobile)
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

     override fun onDonePhoneResult(result: DonePhoneResp) {
         AppPrefsUtils.putString(BaseConstant.USER_PHONE_NUMBER,NewMobile)
        // if (phoneJumpFlag=="fromUserInfo") {
         //    toast("修改完成")
         //    startActivity(intentFor<UserInfoActivity>().singleTop().clearTop())
         //}

         //if (phoneJumpFlag=="fromComputingPower") {
             mPresenter.getReward(sourceCode)
         //    toast("手机号已关联，奖励只在第一次参与活动时获得哦！")
         //    ARouter.getInstance().build("/computingPower/ComputingPowerMainActivity").navigation()
        // }
    }

    override fun onGetRewardResult(result: Int) {
        if (result == 0) {
            if (phoneJumpFlag == "fromUserInfo") {
                toast("修改完成,奖励只在第一次修改时获得哦！")
                startActivity(intentFor<UserInfoActivity>().singleTop().clearTop())
            }
            if (phoneJumpFlag == "fromComputingPower") {
                toast("手机号已关联,奖励只在第一次参与活动时获得哦！")

                ARouter.getInstance().build("/computingPower/ComputingPowerMainActivity").navigation()
            }
        }else{
            //toast("手机号填写失败")
            if (phoneJumpFlag == "fromUserInfo") {
                toast("手机号修改失败")
            }
            if (phoneJumpFlag == "fromComputingPower") {
                toast("手机号关联失败")
            }
        }
    }

}

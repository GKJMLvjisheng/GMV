package com.oases.computingpower.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.computingpower.R
import com.oases.computingpower.injection.component.DaggerComputingPowerComponent
import com.oases.computingpower.injection.module.ComputingPowerModule
import com.oases.computingpower.presenter.ComputingPowerMainPresenter
import com.oases.computingpower.presenter.WatchingWeChatPresenter
import com.oases.computingpower.presenter.view.ComputingPowerMainView
import com.oases.computingpower.presenter.view.WatchingWeChatView
import kotlinx.android.synthetic.main.activity_watching_wechat_public_account.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast

class WatchingWeChatPublicAccountActivity: BaseMvpActivity<WatchingWeChatPresenter>(),
        WatchingWeChatView {

    private val sourceCode:Int = 4   //关注公众号的活动序号为4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watching_wechat_public_account)
        mConfirm.onClick {
            mPresenter.verifyWeChat(mCode.text.toString())
        }
    }

    override fun injectComponent() {
        DaggerComputingPowerComponent
                .builder()
                .activityComponent(mActivityComponent)
                .computingPowerModule(ComputingPowerModule())
                .build()
                .inject(this)
        Log.d("zbb", "MyPoints injected")
        mPresenter.mView = this
    }

    override fun onVerifyResult(result: Int) {
        if (result == 0){
            toast("验证成功")
            //mPresenter.getReward(sourceCode)
            startActivity(intentFor<ComputingPowerMainActivity>().singleTop().clearTop())
        }
        else{
            toast("验证错误")
        }
    }

    /*override fun onGetRewardResult(result: Int) {
        if (result ==0) {
            toast("活动参与成功")
            startActivity(intentFor<ComputingPowerMainActivity>().singleTop().clearTop())
        }else{
            toast("活动参与失败")
        }
    }*/
}

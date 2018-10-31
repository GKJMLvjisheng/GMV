package com.oases.user.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseActivity
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.provider.router.RouterPath
//import com.oases.provider.router.RouterPath
import com.oases.user.R
import com.oases.user.data.protocol.UserInfo
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.LoginPresenter
import com.oases.user.presenter.view.LoginView
import com.oases.user.utils.getDeviceId
import kotlinx.android.synthetic.main.activity_register_succeed.*
import org.jetbrains.anko.*

class RegisterSucceedActivity : BaseMvpActivity<LoginPresenter>(), View.OnClickListener, LoginView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_succeed)
        initView()
    }

    private fun initView() {
        mToHeadPageBtn.onClick(this)
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
    override fun onClick(view: View?) {
        mPresenter.login(AppPrefsUtils.getString(BaseConstant.USER_NAME), AppPrefsUtils.getString(BaseConstant.USER_PASSWORD), getDeviceId(this))

    }

    override fun onLoginResult(result: UserInfo) {
        if(result == null){
            toast("登陆失败，该手机已绑定别的账号")
        }else{
            setResult(Activity.RESULT_OK)
        }
        startActivity(intentFor<LoginActivity>().singleTop().clearTop())
        finish()
    }

    override fun onLoginFailed(e: Throwable) {
        startActivity(intentFor<LoginActivity>().singleTop().clearTop())
        finish()
    }
}

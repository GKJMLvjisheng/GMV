package com.oases.user.presenter

import android.util.Log
import android.widget.Toast
import com.oases.base.common.BaseConstant
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.data.protocol.UserInfo
import com.oases.user.presenter.view.LoginView
import com.oases.user.presenter.view.RegisterView
import com.oases.user.service.UserService
import com.oases.user.utils.putUserInfo
import javax.inject.Inject

class LoginPresenter @Inject constructor() : BasePresenter<LoginView>() {

    @Inject
    lateinit var userService: UserService

    /*
        登录
     */
    fun login(name: String, pwd: String, imei: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.login(name, pwd, imei).execute(object : BaseSubscriber<UserInfo>(mView) {
            override fun onNext(t: UserInfo) {
                AppPrefsUtils.putString(BaseConstant.USER_TOKEN, t.token)
                putUserInfo(t)
                mView.onLoginResult(t)
            }
        }, lifecycleProvider)
    }

}
package com.oases.user.presenter

import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.user.data.protocol.UserInfo
import com.oases.user.presenter.view.RegisterConfirmView
import com.oases.user.service.UserService
import com.oases.user.utils.putUserInfo

import javax.inject.Inject

class RegisterConfirmPresenter @Inject constructor(): BasePresenter<RegisterConfirmView>() {
    @Inject
    lateinit var userService: UserService

    /*
        登录
     */
    fun registerConfirm(uuid:String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.registerConfirm(uuid).execute(object : BaseSubscriber<Boolean>(mView) {
            override fun onNext(t: Boolean) {
                mView.onRegisterConfirmResult(t)
            }
        }, lifecycleProvider)
    }
}
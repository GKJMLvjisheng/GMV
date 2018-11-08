package com.oases.user.presenter

import com.oases.base.common.BaseConstant
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.data.protocol.*
import com.oases.user.presenter.view.*
import com.oases.user.service.UserService
import javax.inject.Inject

class DoneMailPresenter @Inject constructor() : BasePresenter<DoneMailView>() {

    @Inject
    lateinit var userService: UserService


    fun  doneMail(email: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.doneMail(email).execute(object : BaseSubscriber<DoneMailResp>(mView) {
            override fun onNext(t: DoneMailResp) {
              //  AppPrefsUtils.putString(BaseConstant.USER_NAME, t.name)
                AppPrefsUtils.putString(BaseConstant.USER_MAIL_ADDRESS, t.email)
             //   putUserInfo(t)
                mView.onDoneMailResult(t)
            }
        }, lifecycleProvider)
    }

    fun getReward(sourceUuid: String) {
    //fun getReward(sourceCode: Int) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.getReward(sourceUuid).execute(object : BaseSubscriber<Int>(mView) {
        //userService.getReward(sourceCode).execute(object : BaseSubscriber<Int>(mView) {
            override fun onNext(t: Int) {
                mView.onGetRewardResult(t)
            }
        }, lifecycleProvider)
    }

}
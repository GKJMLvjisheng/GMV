package com.oases.user.presenter

import com.oases.base.common.BaseConstant
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.data.protocol.CheckPhoneCodeResp
import com.oases.user.data.protocol.DonePhoneResp
import com.oases.user.data.protocol.SendMobileResp
import com.oases.user.data.protocol.UserInfo
import com.oases.user.presenter.view.*
import com.oases.user.service.UserService
import com.oases.user.utils.putUserInfo
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_user_info.*
import javax.inject.Inject

class DonePhonePresenter @Inject constructor() : BasePresenter<DonePhoneView>() {

    @Inject
    lateinit var userService: UserService

    /*fun  donePhone(mobile: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.donePhone(mobile).execute(object : BaseSubscriber<DonePhoneResp>(mView) {
            override fun onNext(t: DonePhoneResp) {
                AppPrefsUtils.putString(BaseConstant.USER_NAME, t.name)
                AppPrefsUtils.putString(BaseConstant.USER_PHONE_NUMBER, t.mobile)
                mView.onDonePhoneResult(t)
            }
        }, lifecycleProvider)
    }*/

    fun  donePhone(mobile: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.donePhone(mobile).execute(object : BaseSubscriber<DonePhoneResp>(mView) {
            override fun onNext(t: DonePhoneResp) {
                //AppPrefsUtils.putString(BaseConstant.USER_NAME, t.name)
                AppPrefsUtils.putString(BaseConstant.USER_PHONE_NUMBER, t.mobile)
                mView.onDonePhoneResult(t)
            }
        }, lifecycleProvider)
    }

    //fun getReward(sourceCode: String) {
    fun getReward(sourceCode: Int) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.getReward(sourceCode).execute(object : BaseSubscriber<Int>(mView) {
            override fun onNext(t: Int) {
                mView.onGetRewardResult(t)
            }
        }, lifecycleProvider)
    }

}
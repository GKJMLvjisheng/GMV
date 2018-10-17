package com.oases.user.presenter

import android.util.Log
import com.oases.base.common.BaseConstant
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.data.protocol.UserInfo
import com.oases.user.presenter.view.NickNameView
import com.oases.user.service.UserService
import com.oases.user.utils.putUserInfo
import javax.inject.Inject

class NickNamePresenter @Inject constructor() : BasePresenter<NickNameView>() {

    @Inject
    lateinit var userService: UserService

    /*
        修改昵称
     */
    fun  updateNickName(NickName: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.updateNickName(NickName).execute(object : BaseSubscriber<UserInfo>(mView) {
            override fun onNext(t: UserInfo) {

                Log.d("NickName", "update success${t.toString()}")
               // putUserInfo(t)
                AppPrefsUtils.putString(BaseConstant.USER_NICK_NAME,t.nickname)
                mView.onUpdateNickNameResult(t)
            }
        }, lifecycleProvider)
    }

}
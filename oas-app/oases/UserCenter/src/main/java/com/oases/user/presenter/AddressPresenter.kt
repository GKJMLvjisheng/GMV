package com.oases.user.presenter

import android.util.Log
import com.oases.base.common.BaseConstant
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.data.protocol.UserInfo
import com.oases.user.presenter.view.AddressView
import com.oases.user.service.UserService
import com.oases.user.utils.putUserInfo
import javax.inject.Inject

class AddressPresenter @Inject constructor() : BasePresenter<AddressView>() {

    @Inject
    lateinit var userService: UserService

    /*
        修改昵称
     */
    fun  updateAddress(address: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.updateAddress(address).execute(object : BaseSubscriber<UserInfo>(mView) {
            override fun onNext(t: UserInfo) {

                Log.d("Address", "update success${t.toString()}")
                //putUserInfo(t)
                AppPrefsUtils.putString(BaseConstant.USER_ADDRESS,t.address)
                mView.onUpdateAddressResult(t)
            }
        }, lifecycleProvider)
    }

}
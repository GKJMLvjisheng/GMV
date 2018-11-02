package com.oases.user.presenter

import android.util.Log
import com.oases.base.common.BaseConstant
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.data.protocol.BackupWalletStatusResp
import com.oases.user.data.protocol.PrivateKeyMnemonicResp
import com.oases.user.data.protocol.UserInfo
import com.oases.user.presenter.view.EncryptedMnemonicView
import com.oases.user.presenter.view.NickNameView
import com.oases.user.presenter.view.PasswordInSecurityView
import com.oases.user.presenter.view.PrivateKeyMnemonicView
import com.oases.user.service.UserService
import com.oases.user.utils.putUserInfo
import javax.inject.Inject

class EncryptedMnemonicPresenter @Inject constructor() : BasePresenter<EncryptedMnemonicView>() {

    @Inject
    lateinit var userService: UserService



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
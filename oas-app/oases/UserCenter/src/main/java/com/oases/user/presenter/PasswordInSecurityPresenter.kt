package com.oases.user.presenter

import android.util.Log
import com.oases.base.common.BaseConstant
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.data.protocol.BackupWalletStatusResp
import com.oases.user.data.protocol.UserInfo
import com.oases.user.presenter.view.NickNameView
import com.oases.user.presenter.view.PasswordInSecurityView
import com.oases.user.service.UserService
import com.oases.user.utils.putUserInfo
import javax.inject.Inject

class PasswordInSecurityPresenter @Inject constructor() : BasePresenter<PasswordInSecurityView>() {

    @Inject
    lateinit var userService: UserService

    /*
        得到用户备份钱包的状态
     */
    fun  getBackupWalletStatus() {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.getBackupWalletStatus().execute(object : BaseSubscriber<BackupWalletStatusResp>(mView) {
            override fun onNext(t: BackupWalletStatusResp) {
                mView.onGetBackupWalletStatusResult(t)
            }
        }, lifecycleProvider)
    }

}
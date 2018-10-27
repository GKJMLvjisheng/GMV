package com.oases.user.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.user.data.protocol.*

interface EncryptedMnemonicView : BaseView{
    //fun onGetPrivateKeyMnemonicResult(result: PrivateKeyMnemonicResp)
    fun onGetRewardResult(result: Int)
}
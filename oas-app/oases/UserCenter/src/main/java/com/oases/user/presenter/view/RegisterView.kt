package com.oases.user.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.user.data.protocol.RegisterResp

/**
 * Created by Bingo Zhu
 * 2018/8/14
 */
interface RegisterView : BaseView {
    fun onRegisterResult(result: RegisterResp){}
}
package com.oases.computingpower.presenter.view

import com.oases.base.presenter.view.BaseView
import com.oases.computingpower.data.protocol.InviteFriendsInfoResp

interface InviteFriendsInfoView : BaseView {
    fun onInviteFriendsInfoResult(result: InviteFriendsInfoResp)
}
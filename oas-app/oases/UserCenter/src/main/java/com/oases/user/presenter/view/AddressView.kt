package com.oases.user.presenter.view

import android.media.MediaRouter
import com.oases.base.presenter.view.BaseView
import com.oases.user.data.protocol.UserInfo

interface AddressView : BaseView{
    fun onUpdateAddressResult(result: UserInfo)
}
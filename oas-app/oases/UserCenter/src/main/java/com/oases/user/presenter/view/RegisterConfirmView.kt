package com.oases.user.presenter.view

import com.oases.base.presenter.view.BaseView

interface RegisterConfirmView: BaseView{
    fun onRegisterConfirmResult(result: Boolean)
}
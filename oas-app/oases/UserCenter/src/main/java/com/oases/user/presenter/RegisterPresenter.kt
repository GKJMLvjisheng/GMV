package com.oases.user.presenter

import android.util.Log
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.user.R
import com.oases.user.data.protocol.RegisterResp
import com.oases.user.presenter.view.RegisterView
import com.oases.user.service.UserService
import javax.inject.Inject

/**
 * Created by Bingo Zhu
 * 2018/8/14
 */
class RegisterPresenter @Inject constructor():BasePresenter<RegisterView>() {

    @Inject
    lateinit var userService: UserService

    fun register(name:String, pwd:String, inviteFrom:String){
        if(!checkNetWork()){
            return
        }
        mView.showLoading()

        userService.register(name, pwd,inviteFrom).execute(object : BaseSubscriber<RegisterResp>(mView){
            override  fun onNext(t: RegisterResp){
                Log.d("zbb", "register successfully")
                mView.onRegisterResult(t)
              //  log.d(RegisterResp)
            }

        }, lifecycleProvider)
    }
}
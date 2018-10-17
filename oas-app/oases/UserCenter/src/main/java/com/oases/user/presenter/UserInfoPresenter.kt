package com.oases.user.presenter

import android.util.Log
import com.oases.base.common.BaseConstant
import com.oases.base.ext.execute
import com.oases.base.presenter.BasePresenter
import com.oases.base.rx.BaseSubscriber
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.data.protocol.UserInfo
import com.oases.user.data.protocol.uploadImgResp
import com.oases.user.presenter.view.*
import com.oases.user.service.UserService
import com.oases.user.utils.putUserInfo
import okhttp3.MultipartBody
import javax.inject.Inject

class UserInfoPresenter @Inject constructor() : BasePresenter<UserInfoView>() {


    @Inject
    lateinit var userService: UserService

    /*
        上传头像
     */
    fun  updateUserIcon(UserIcon: MultipartBody.Part) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.updateUserIcon(UserIcon).execute(object : BaseSubscriber<uploadImgResp>(mView) {
            override fun onNext(t: uploadImgResp) {

                Log.d("UserIcon", "update success${t.toString()}")
                mView.onUpdateUserIconResult(t)
               AppPrefsUtils.putString(BaseConstant.USER_ICON,t.profile)
            }
        }, lifecycleProvider)
    }

    //设置生日
    fun  updateBirthday(birthday: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService. updateBirthday(birthday).execute(object : BaseSubscriber<UserInfo>(mView) {
            override fun onNext(t: UserInfo) {

                Log.d("birthday", "update success${t.toString()}")
                //putUserInfo(t)
                AppPrefsUtils.putString(BaseConstant.USER_BIRTHDAY,t.birthday)
                mView.onUpdateBirthdayResult(t)
            }
        }, lifecycleProvider)
    }

    /*
        修改性别
     */
    fun  updateGender(gender: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.updateGender(gender).execute(object : BaseSubscriber<UserInfo>(mView) {
            override fun onNext(t: UserInfo) {

                Log.d("gender", "update success${t.toString()}")
                AppPrefsUtils.putString(BaseConstant.USER_GENDER,t.gender)
                mView.onUpdateGenderResult(t)
            }
        }, lifecycleProvider)
    }

}
package com.oases.user.data.repository

import android.text.GetChars
import com.oases.base.data.net.RetrofitFactory
import com.oases.base.data.protocol.BaseResp
import com.oases.user.data.api.UserApi
import com.oases.user.data.protocol.*
import io.reactivex.Observable
import okhttp3.MultipartBody

import javax.inject.Inject

/**
 * Created by Bingo Zhu
 * 2018/8/14
 */
class UserRepository @Inject constructor() {
    fun register(req:RegisterReq): Observable<BaseResp<RegisterResp>> {
        return RetrofitFactory.instance.create(UserApi::class.java).register(req)
    }
    fun login(name:String, pwd:String, imei: String): Observable<BaseResp<UserInfo>> {
        return RetrofitFactory.instance.create(UserApi::class.java).login(LoginReq(name, pwd, imei))
    }

    fun registerConfirm(uuid:String): Observable<BaseResp<Int>> {
        return RetrofitFactory.instance.create(UserApi::class.java).registerConfirm(RegisterConfirmReq(uuid))
    }

    fun updateUserIcon(UserIcon: MultipartBody.Part): Observable<BaseResp<uploadImgResp>> {
        return RetrofitFactory.instance.create(UserApi::class.java).updateUserIcon(UserIcon)
    }

    fun updateNickName(NickName:String): Observable<BaseResp<UserInfo>> {
        return RetrofitFactory.instance.create(UserApi::class.java).updateNickName(updateNickNameReq(NickName))
    }

    fun updateGender(gender:String): Observable<BaseResp<UserInfo>> {
        return RetrofitFactory.instance.create(UserApi::class.java).updateGender(updateGenderReq(gender))
    }

    fun updateAddress(address:String): Observable<BaseResp<UserInfo>> {
        return RetrofitFactory.instance.create(UserApi::class.java).updateAddress(updateAddressReq(address))
    }

    fun updateBirthday(birthday:String): Observable<BaseResp<UserInfo>> {
        return RetrofitFactory.instance.create(UserApi::class.java).updateBirthday(updateBirthdayReq(birthday))
    }

    fun checkPhone(mobile:String): Observable<BaseResp<Int>> {
        return RetrofitFactory.instance.create(UserApi::class.java).checkPhone(CheckPhoneReq(mobile))
    }

    fun sendMobile(mobile:String): Observable<BaseResp<SendMobileResp>> {
        return RetrofitFactory.instance.create(UserApi::class.java).sendMobile(SendMobileReq(mobile))
    }

    fun checkPhoneCode(mobileCode:String): Observable<BaseResp<CheckPhoneCodeResp>> {
        return RetrofitFactory.instance.create(UserApi::class.java).checkPhoneCode(CheckPhoneCodeReq(mobileCode))
    }

    fun getReward(sourceCode:String): Observable<BaseResp<Int>> {
        return RetrofitFactory.instance.create(UserApi::class.java).getReward(GetRewardReq(sourceCode))
    }

    fun donePhone(mobile:String): Observable<BaseResp<DonePhoneResp>> {
        return RetrofitFactory.instance.create(UserApi::class.java).donePhone(DonePhoneReq(mobile))
    }

    fun checkMail(email:String): Observable<BaseResp<Int>> {
        return RetrofitFactory.instance.create(UserApi::class.java).checkMail(CheckMailReq(email))
    }

    fun sendMail(email:String): Observable<BaseResp<SendMailResp>> {
        return RetrofitFactory.instance.create(UserApi::class.java).sendMail(SendMailReq(email))
    }

    fun checkMailCode(mailCode:String): Observable<BaseResp<CheckMailCodeResp>> {
        return RetrofitFactory.instance.create(UserApi::class.java).checkMailCode(CheckMailCodeReq(mailCode))
    }

    fun doneMail(email:String): Observable<BaseResp<DoneMailResp>> {
        return RetrofitFactory.instance.create(UserApi::class.java).doneMail(DoneMailReq(email))
    }

    fun inquireUserNameByMobile(mobile:String): Observable<BaseResp<ForgetPwdOneResp>> {
        return RetrofitFactory.instance.create(UserApi::class.java).inquireUserNameByMobile(ForgetPwdOneReq(mobile))
    }

    fun resetPwd(name:String,newPwd:String): Observable<BaseResp<Int>> {
        return RetrofitFactory.instance.create(UserApi::class.java).resetPwd(ResetPwdReq(name,newPwd))
    }

    fun confirmOldPwd(req: confirmOldPwdReq): Observable<BaseResp<confirmOldPwdResp>> {
        return RetrofitFactory.instance.create(UserApi::class.java).confirmOldPwd(req)
    }

    //获得用户备份钱包的状态
    fun getBackupWalletStatus(): Observable<BaseResp<BackupWalletStatusResp>> {
        return RetrofitFactory.instance.create(UserApi::class.java).getBackupWalletStatus()
    }

    //获得备份钱包中用户的私钥
    fun getPrivateKeyMnemonic(): Observable<BaseResp<PrivateKeyMnemonicResp>> {
        return RetrofitFactory.instance.create(UserApi::class.java).getPrivateKeyMnemonic()
    }

    fun verifyPassword(req: confirmOldPwdReq): Observable<BaseResp<confirmOldPwdResp>> {
        return RetrofitFactory.instance.create(UserApi::class.java).verifyPassword(req)
    }



}
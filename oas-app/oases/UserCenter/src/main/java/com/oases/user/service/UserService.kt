package com.oases.user.service

import com.oases.user.data.protocol.*
import io.reactivex.Observable

import okhttp3.MultipartBody
import java.io.File


/**
 * Created by Bingo Zhu
 * 2018/8/14
 */
interface UserService {
    fun register(req:RegisterReq): Observable<RegisterResp>
    fun login(name:String, pwd:String, imei: String):Observable<UserInfo>
    fun registerConfirm(uuid:String):Observable<Boolean>
    fun updateUserIcon(UserIcon: MultipartBody.Part):Observable<uploadImgResp>
    fun updateNickName(NickName:String):Observable<UserInfo>
    fun updateGender(gender:String):Observable<UserInfo>
    fun updateBirthday(birthday:String):Observable<UserInfo>
    fun updateAddress(address:String):Observable<UserInfo>

    fun checkPhone(mobile:String):Observable<Int>
    fun sendMobile(mobile:String):Observable<SendMobileResp>
    fun checkPhoneCode(mobileCode:String):Observable<CheckPhoneCodeResp>
    fun donePhone(mobile:String):Observable<DonePhoneResp>
    fun getReward(sourceCode:String): Observable<Int>

    fun checkMail(email:String):Observable<Int>
    fun sendMail(email:String):Observable<SendMailResp>
    fun checkMailCode(mailCode:String):Observable<CheckMailCodeResp>
    fun doneMail(email:String):Observable<DoneMailResp>

    fun inquireUserNameByMobile(mobile:String):Observable<ForgetPwdOneResp>
   // fun checkIdentifyCode(mobileCode:String):Observable<CheckPhoneCodeResp>
    fun resetPwd(name:String,newPwd:String):Observable<Int>


    /**
     * 用户旧密码比对
     */
    fun confirmOldPwd(req: confirmOldPwdReq):Observable<confirmOldPwdResp>

    fun getBackupWalletStatus():Observable<BackupWalletStatusResp>
    fun getPrivateKeyMnemonic():Observable<PrivateKeyMnemonicResp>

}
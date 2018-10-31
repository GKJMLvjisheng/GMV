package com.oases.user.service.impl

import com.oases.base.ext.convert
import com.oases.base.ext.convertBoolean
import com.oases.user.data.protocol.*
import com.oases.user.data.repository.UserRepository
import com.oases.user.service.UserService
import io.reactivex.Observable

import okhttp3.MultipartBody
import java.io.File

import javax.inject.Inject

/**
 * Created by Bingo Zhu
 * 2018/8/14
 */
class UserServiceImpl @Inject constructor():UserService{

    @Inject
    lateinit var  repository:UserRepository

    override fun register(req:RegisterReq):Observable<RegisterResp>{
        return repository.register(req).convert()
    }

    override fun login(name: String, pwd: String, imei: String): Observable<UserInfo> {
        return repository.login(name, pwd, imei).convert()
    }

    override fun registerConfirm(uuid:String): Observable<Boolean>{
        return repository.registerConfirm(uuid).convertBoolean()
    }

    override fun updateUserIcon(UserIcon: MultipartBody.Part): Observable<uploadImgResp> {
        return repository.updateUserIcon(UserIcon).convert()
    }

    override fun updateNickName(NickName: String): Observable<UserInfo> {
        return repository.updateNickName(NickName).convert()
    }

    override fun updateGender(gender: String): Observable<UserInfo> {
        return repository.updateGender(gender).convert()
    }

    override fun updateAddress(address: String): Observable<UserInfo> {
        return repository.updateAddress(address).convert()
    }

    override fun updateBirthday(birthday: String): Observable<UserInfo> {
        return repository.updateBirthday(birthday).convert()
    }

    override fun checkPhone(mobile: String): Observable<Int> {
        return repository.checkPhone(mobile).convert()
    }

    override fun sendMobile(mobile: String): Observable<SendMobileResp> {
        return repository.sendMobile(mobile).convert()
    }

    override fun checkPhoneCode(mobileCode: String): Observable<CheckPhoneCodeResp> {
        return repository.checkPhoneCode(mobileCode).convert()
    }

    override fun donePhone(mobile: String): Observable<DonePhoneResp> {
        return repository.donePhone(mobile).convert()
    }

    override fun getReward(sourceCode:String): Observable<Int>{
        return repository.getReward(sourceCode).convert()
    }

    override fun checkMail(email: String): Observable<Int> {
        return repository.checkMail(email).convert()
    }

    override fun sendMail(email: String): Observable<SendMailResp> {
        return repository.sendMail(email).convert()
    }

    override fun checkMailCode(mailCode: String): Observable<CheckMailCodeResp> {
        return repository.checkMailCode(mailCode).convert()
    }

    override fun doneMail(email: String): Observable<DoneMailResp> {
        return repository.doneMail(email).convert()
    }

    override fun inquireUserNameByMobile(mobile: String): Observable<ForgetPwdOneResp> {
        return repository.inquireUserNameByMobile(mobile).convert()
    }

    /*override fun checkIdentifyCode(mobileCode: String): Observable<CheckPhoneCodeResp> {
        return repository.checkIdentifyCode(mobileCode).convert()
    }*/


    override fun resetPwd(name:String,newPwd: String): Observable<Int> {
        return repository.resetPwd(name,newPwd).convert()
    }

    //账户与安全中的设置密码
    override fun confirmOldPwd(req: confirmOldPwdReq): Observable<confirmOldPwdResp> {
        return repository.confirmOldPwd(req).convert()
    }

    override fun getBackupWalletStatus(): Observable<BackupWalletStatusResp> {
        return repository.getBackupWalletStatus().convert()
    }

    override fun getPrivateKeyMnemonic(): Observable<PrivateKeyMnemonicResp> {
        return repository.getPrivateKeyMnemonic().convert()
    }
    override fun verifyPassword(req: confirmOldPwdReq): Observable<confirmOldPwdResp> {
        return repository.verifyPassword(req).convert()
    }
}
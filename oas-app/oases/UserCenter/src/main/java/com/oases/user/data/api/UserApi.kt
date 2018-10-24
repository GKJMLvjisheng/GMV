package com.oases.user.data.api

import com.oases.base.data.protocol.BaseResp
import com.oases.user.data.protocol.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Created by Bingo Zhu
 * 2018/8/14
 */
interface UserApi {
    @POST("api/v1/userCenter/register")
    fun register(@Body req: RegisterReq):Observable<BaseResp<RegisterResp>>

    @POST("api/v1/userCenter/login")
    fun login(@Body req: LoginReq):Observable<BaseResp<UserInfo>>

    @POST("api/v1/userCenter/registerConfirm")
    fun registerConfirm(@Body req: RegisterConfirmReq):Observable<BaseResp<Int>>

    @Multipart
    @POST("api/v1/userCenter/upLoadImg")
    fun updateUserIcon(@Part req: MultipartBody.Part):Observable<BaseResp<uploadImgResp>>

    @POST("api/v1/userCenter/updateUserInfo")
    fun updateNickName(@Body req: updateNickNameReq):Observable<BaseResp<UserInfo>>

    @POST("api/v1/userCenter/updateUserInfo")
    fun updateGender(@Body req: updateGenderReq):Observable<BaseResp<UserInfo>>

    @POST("api/v1/userCenter/updateUserInfo")
    fun updateAddress(@Body req: updateAddressReq):Observable<BaseResp<UserInfo>>

    @POST("api/v1/userCenter/updateUserInfo")
    fun updateBirthday(@Body req: updateBirthdayReq):Observable<BaseResp<UserInfo>>

    @POST("/api/v1/userCenter/CheckUserMobile")
    fun checkPhone(@Body req: CheckPhoneReq):Observable<BaseResp<Int>>

    @POST("/api/v1/userCenter/sendMobile ")
    fun sendMobile(@Body req: SendMobileReq):Observable<BaseResp<SendMobileResp>>

    @POST("/api/v1/userCenter/mobileCheckCode")
    fun checkPhoneCode(@Body req: CheckPhoneCodeReq):Observable<BaseResp<CheckPhoneCodeResp>>

    @POST("/api/v1/userCenter/resetMobile")
    fun donePhone(@Body req: DonePhoneReq):Observable<BaseResp<DonePhoneResp>>

    //@POST("/api/v1/activityConfig/getReward ")
    @POST("/api/v1/computingPower/doGetReward")
    fun getReward(@Body req: GetRewardReq): Observable<BaseResp<Int>>

    @POST("/api/v1/userCenter/CheckUserEmail")
    fun checkMail(@Body req: CheckMailReq):Observable<BaseResp<Int>>

    @POST("/api/v1/userCenter/sendMail")
    fun sendMail(@Body req: SendMailReq):Observable<BaseResp<SendMailResp>>

    @POST("/api/v1/userCenter/mailCheckCode")
    fun checkMailCode(@Body req: CheckMailCodeReq):Observable<BaseResp<CheckMailCodeResp>>

    @POST("/api/v1/userCenter/resetMail")
    fun doneMail(@Body req: DoneMailReq):Observable<BaseResp<DoneMailResp>>

    @POST("/api/v1/userCenter/inquireUserInfoByMobile")
    fun inquireUserNameByMobile(@Body req: ForgetPwdOneReq):Observable<BaseResp<ForgetPwdOneResp>>

    @POST("/api/v1/userCenter/resetPassword")
    fun resetPwd(@Body req: ResetPwdReq):Observable<BaseResp<Int>>

    @POST("/api/v1/userCenter/checkPassword")
    fun confirmOldPwd(@Body req: confirmOldPwdReq):Observable<BaseResp<confirmOldPwdResp>>
}
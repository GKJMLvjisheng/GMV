package com.oases.computingpower.data.api

import com.oases.base.data.protocol.BaseResp
import com.oases.computingpower.data.protocol.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ComputingPowerApi {
    @POST("/api/v1/computingPower/inquirePower")
    fun inquirePower(): Observable<BaseResp<ComputingPowerResp>>

    @POST("/api/v1/computingPower/promotePowerByWechatAccount")
    fun verifyWeChat(@Body req: WatchingWeChatCodeVerifyReq): Observable<BaseResp<WatchingWeChatCodeVerifyResp>>

    @POST("/api/v1/activityConfig/getReward ")
    fun getReward(@Body req: GetRewardReq): Observable<BaseResp<Int>>

    @POST("/api/v1/computingPower/inqureInviteStatistical")
    fun getInviteFriendsInfo(): Observable<BaseResp<InviteFriendsInfoResp>>

    @POST("/api/v1/computingPower/inquireUserMobile")
    fun checkPhone(): Observable<BaseResp<PhoneStatusResp>>

    @POST("/api/v1/computingPower/inquireUserEmail")
    fun checkMail(): Observable<BaseResp<MailStatusResp>>

    @POST("/api/v1/computingPower/inquirePowerActivityStatus")
    fun inquirePowerActivityStatus(): Observable<BaseResp<PowerActivityStatusResp>>

    @POST("/api/v1/computingPower/inquirePowerDetail")
    fun inquirePowerDetail(@Body req:ComputingPowerDetailReq): Observable<BaseResp<ComputingPowerDetailResp>>

    //图片上传需要注解@Multipart，且上传的请求为Part
    @Multipart
    @POST("/api/v1/userCenter/upLoadUserIdentityInfo")
    fun upLoadKYCInfo(@Part req: MultipartBody.Part): Observable<BaseResp<KYCInfoResp>>

    @POST("/api/v1/userCenter/inqureUserIdentityInfo")
    fun getKYCVerifyStatus():Observable<BaseResp<KYCVerifyStatusResp>>


    @POST("/api/v1/userCenter/confirmSubmitUserIdentifyInfo")
    fun changeKYCStatus(@Body req:KYCImageUriReq):Observable<BaseResp<Int>>


    @POST("/api/v1/miner/inquireMiner")
    fun getMinerInfo(@Body req: MinerInfoReq):Observable<BaseResp<MinerInfoResp>>

    @POST("/api/v1/miner/buyMiner")
    fun confirmBuyMiner(@Body req:ConfirmBuyMinerReq):Observable<BaseResp<Int>>

    @POST("/api/v1/miner/inquirePurchaseRecord")
    fun getBuyingMinerHistory(@Body req:BuyingMinerHistoryReq):Observable<BaseResp<BuyingMinerHistoryResp>>


}
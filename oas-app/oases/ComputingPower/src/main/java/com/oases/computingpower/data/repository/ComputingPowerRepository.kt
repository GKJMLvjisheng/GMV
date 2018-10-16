package com.oases.computingpower.data.repository

import com.oases.base.data.net.RetrofitFactory
import com.oases.base.data.protocol.BaseResp
import com.oases.computingpower.data.api.ComputingPowerApi
import com.oases.computingpower.data.protocol.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.Body
import javax.inject.Inject

class ComputingPowerRepository @Inject constructor() {

    fun inquirePower(): Observable<BaseResp<ComputingPowerResp>> {
        return RetrofitFactory.instance.create(ComputingPowerApi::class.java).inquirePower()
    }

    fun verifyWeChat(@Body req: WatchingWeChatCodeVerifyReq): Observable<BaseResp<WatchingWeChatCodeVerifyResp>> {
        return RetrofitFactory.instance.create(ComputingPowerApi::class.java).verifyWeChat(req)
    }
    fun getReward(sourceCode:Int): Observable<BaseResp<Int>> {
        return RetrofitFactory.instance.create(ComputingPowerApi::class.java).getReward(GetRewardReq(sourceCode))
    }

    fun getInviteFriendsInfo(): Observable<BaseResp<InviteFriendsInfoResp>> {
        return RetrofitFactory.instance.create(ComputingPowerApi::class.java).getInviteFriendsInfo()
    }

    fun checkPhone(): Observable<BaseResp<PhoneStatusResp>> {
        return RetrofitFactory.instance.create(ComputingPowerApi::class.java).checkPhone()
    }

    fun checkMail(): Observable<BaseResp<MailStatusResp>> {
        return RetrofitFactory.instance.create(ComputingPowerApi::class.java).checkMail()
    }

    fun inquirePowerActivityStatus(): Observable<BaseResp<PowerActivityStatusResp>> {
        return RetrofitFactory.instance.create(ComputingPowerApi::class.java).inquirePowerActivityStatus()
    }
    fun inquirePowerDetail(req:ComputingPowerDetailReq): Observable<BaseResp<ComputingPowerDetailResp>>{
        return RetrofitFactory.instance.create(ComputingPowerApi::class.java).inquirePowerDetail(req)
    }

    fun upLoadKYCInfo(KYCInfo: MultipartBody.Part): Observable<BaseResp<KYCInfoResp>>{
        return RetrofitFactory.instance.create(ComputingPowerApi::class.java).upLoadKYCInfo(KYCInfo)
    }
    fun getKYCVerifyStatus():Observable<BaseResp<KYCVerifyStatusResp>>{
        return RetrofitFactory.instance.create(ComputingPowerApi::class.java).getKYCVerifyStatus()
    }

    fun changeKYCStatus(req:KYCImageUriReq):Observable<BaseResp<Int>>{
        return RetrofitFactory.instance.create(ComputingPowerApi::class.java).changeKYCStatus(req)
    }

    fun getMinerInfo(req: MinerInfoReq):Observable<BaseResp<MinerInfoResp>>{
        return RetrofitFactory.instance.create(ComputingPowerApi::class.java).getMinerInfo(req)
    }

    fun confirmBuyMiner(req:ConfirmBuyMinerReq):Observable<BaseResp<Int>>{
        return RetrofitFactory.instance.create(ComputingPowerApi::class.java).confirmBuyMiner(req)
    }



}
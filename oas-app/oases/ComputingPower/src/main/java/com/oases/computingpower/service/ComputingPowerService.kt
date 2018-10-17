package com.oases.computingpower.service

import com.oases.computingpower.data.protocol.*
import io.reactivex.Observable
import okhttp3.MultipartBody

interface ComputingPowerService {
    fun inquirePower(): Observable<ComputingPowerResp>
    fun verifyWeChat(req: WatchingWeChatCodeVerifyReq): Observable<WatchingWeChatCodeVerifyResp>
    fun getReward(sourceCode:Int): Observable<Int>
    fun getInviteFriendsInfo(): Observable<InviteFriendsInfoResp>
    fun checkPhone(): Observable<PhoneStatusResp>
    fun checkMail(): Observable<MailStatusResp>
    fun inquirePowerActivityStatus(): Observable<PowerActivityStatusResp>
    fun inquirePowerDetail(req:ComputingPowerDetailReq):Observable<ComputingPowerDetailResp>
    fun upLoadKYCInfo(KYCInfo: MultipartBody.Part):Observable<KYCInfoResp>
    fun getKYCVerifyStatus():Observable<KYCVerifyStatusResp>
    fun changeKYCStatus(req: KYCImageUriReq):Observable<Int>
    fun getMinerInfo(req: MinerInfoReq):Observable<MinerInfoResp>
    fun confirmBuyMiner(req: ConfirmBuyMinerReq):Observable<Int>


}
package com.oases.computingpower.service.impl

import com.oases.base.ext.convert
import com.oases.computingpower.data.protocol.*
import com.oases.computingpower.data.repository.ComputingPowerRepository
import com.oases.computingpower.service.ComputingPowerService
import io.reactivex.Observable
import okhttp3.MultipartBody
import javax.inject.Inject

class ComputingPowerServiceImpl @Inject constructor(): ComputingPowerService {

    @Inject
    lateinit var repository: ComputingPowerRepository

    override fun inquirePower(): Observable<ComputingPowerResp> {
        return repository.inquirePower().convert()
    }

    override fun verifyWeChat(req: WatchingWeChatCodeVerifyReq): Observable<WatchingWeChatCodeVerifyResp>{
        return repository.verifyWeChat(req).convert()
    }

    override fun getReward(sourceCode:Int): Observable<Int>{
        return repository.getReward(sourceCode).convert()
    }

    override fun getInviteFriendsInfo(): Observable<InviteFriendsInfoResp> {
        return repository.getInviteFriendsInfo().convert()
    }

    override fun checkPhone(): Observable<PhoneStatusResp> {
        return repository.checkPhone().convert()
    }

    override fun checkMail(): Observable<MailStatusResp> {
        return repository.checkMail().convert()
    }

    override fun inquirePowerActivityStatus(): Observable<PowerActivityStatusResp> {
        return repository.inquirePowerActivityStatus().convert()
    }
    override fun inquirePowerDetail(req:ComputingPowerDetailReq):Observable<ComputingPowerDetailResp>{
        return repository.inquirePowerDetail(req).convert()
    }

    override fun upLoadKYCInfo(KYCInfo: MultipartBody.Part):Observable<KYCInfoResp>{
        return repository.upLoadKYCInfo(KYCInfo).convert()
    }
    override fun getKYCVerifyStatus():Observable<KYCVerifyStatusResp>{
        return repository.getKYCVerifyStatus().convert()
    }

    override fun changeKYCStatus(req:KYCImageUriReq):Observable<Int>{
        return repository.changeKYCStatus(req).convert()
    }

    override fun getMinerInfo(req: MinerInfoReq):Observable<MinerInfoResp>{
        return repository.getMinerInfo(req).convert()
    }

    override fun confirmBuyMiner(req:ConfirmBuyMinerReq):Observable<Int>{
        return repository.confirmBuyMiner(req).convert()
    }

    override fun getBuyingMinerHistory(req: BuyingMinerHistoryReq):Observable<BuyingMinerHistoryResp>{
        return repository.getBuyingMinerHistory(req).convert()
    }


}
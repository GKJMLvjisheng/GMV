package com.oases.service.impl

import com.oases.base.data.protocol.BaseResp
import com.oases.base.ext.convert
import com.oases.computingpower.data.protocol.KYCVerifyStatusResp
import com.oases.data.protocol.*
import com.oases.data.repository.WalletRepository
import com.oases.service.WalletService
import io.reactivex.Observable
import retrofit2.http.Body
import java.math.BigDecimal
import javax.inject.Inject


class WalletServiceImpl @Inject constructor(): WalletService {
    @Inject
    lateinit var repository: WalletRepository

    override fun inquirePoints(): Observable<PointsInfo> {
        return repository.inquirePoints().convert()
    }

    /*override fun inquirePointsDetail(pageNumber:Int, size:Int): Observable<InquirePointsDetailResp>{
        return repository.inquirePointsDetail(pageNumber, size).convert()
    }*/

    override fun inquireCurrentPeriodPoints(): Observable<CurrentPeriodPointsResp>{
        return repository.inquireCurrentPeriodPoints().convert()
    }

    override fun inquirePointsFactor(req:InquirePointFactorReq): Observable<InquirePointFactorResp>{
        return repository.inquirePointsFactor(req).convert()
    }
    override fun inquireBalance(): Observable<BalanceDetailResp>{
        return repository.inquireBalance().convert()
    }

    override fun listCoin(): Observable<ListCoinResp>{
        return repository.listCoin().convert()
    }

    override fun summary(): Observable<TransitWalletSummary>{
        return repository.summary().convert()
    }

    override fun redeemPoint(@Body req: RedeemPointReq):Observable<RedeemPointResp>{
        return repository.redeemPoint(req).convert()
    }

    override fun transactionDetail(req:InquireTransactionDetailReq):Observable<InquirePointsDetailResp>{
        return repository.transactionDetail(req).convert()
    }
    override fun exchangeDetail(req: InquireTransactionDetailReq): Observable<InquirePointsDetailResp> {
        return repository.exchangeDetail(req).convert()
    }

    override fun energyDetail(req: InquireTransactionDetailReq): Observable<InquireEnergyDetailResp> {
        return repository.energyDetail(req).convert()
    }
    override fun transactionInOrOutDetail(req:InquirePointsDetailReq):Observable<InquirePointsDetailResp>{
        return repository.transactionInOrOutDetail(req).convert();
    }

    override fun exchangeInOrOutDetail(req: InquirePointsDetailReq): Observable<InquirePointsDetailResp> {
        return repository.exchangeInOrOutDetail(req).convert();
    }

    override fun energyInOrOutDetail(req: InquirePointsDetailReq): Observable<InquireEnergyDetailResp> {
        return repository.energyInOrOutDetail(req).convert();
    }

    override fun inquireAddress():Observable<InquireAddressResp>{
        return repository.inquireAddress().convert()
    }

    override fun walletOutEvent(req:TransferReq):Observable<Int>{
        return repository.walletOutEvent(req).convert()
    }
    override fun ExchangeOutEvent(req:TransferOasReq):Observable<TransferOasResp>{
        return repository.ExchangeOutEvent(req).convert()
    }
    override  fun getAppUpdate():Observable<AppUpdateResp>{
        return repository.getAppUpdate().convert()
    }

    override fun withdraw(req: TransferReq): Observable<Int> {
        return repository.widthdraw(req).convert()
    }

    override fun reverseWithdraw(req: TransferOasReq): Observable<Int> {
        return repository.reverseWithdraw(req).convert()
    }

    override fun getOasExtra(): Observable<String> {
        return repository.getOasExtra().convert()
    }

    override fun getExchangeResult(req: PointItem): Observable<Int> {
        return repository.getExchangeResult(req).convert()
    }
    override  fun getKYCVerifyStatus():Observable<KYCVerifyStatusResp>{
        return repository.getKYCVerifyStatus().convert()
    }

    override  fun getDayMoneyTotal():Observable<BigDecimal>{
        return repository.getDayMoneyTotal().convert()
    }


}

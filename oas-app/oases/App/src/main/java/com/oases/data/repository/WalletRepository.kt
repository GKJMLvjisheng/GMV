package com.oases.data.repository

import com.oases.base.data.net.RetrofitFactory
import com.oases.base.data.protocol.BaseResp
import com.oases.computingpower.data.protocol.KYCVerifyStatusResp
import com.oases.data.api.WalletApi
import com.oases.data.protocol.*
import javax.inject.Inject
import io.reactivex.Observable
import retrofit2.http.Body
import java.math.BigDecimal

class WalletRepository @Inject constructor() {
    //My point
    fun inquirePoints(): Observable<BaseResp<PointsInfo>> {
        return RetrofitFactory.instance.create(WalletApi::class.java).inquirePoints()
    }

/*    fun inquirePointsDetail(pageNumber: Int, size: Int): Observable<BaseResp<InquirePointsDetailResp>> {
        return RetrofitFactory.instance.create(WalletApi::class.java).inquirePointsDetail(InquirePointsDetailReq(pageNumber, size))
    }*/

    fun inquireCurrentPeriodPoints(): Observable<BaseResp<CurrentPeriodPointsResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).inquireCurrentPeriodPoints()
    }

    fun inquirePointsFactor(req: InquirePointFactorReq): Observable<BaseResp<InquirePointFactorResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).inquirePointFactor(req)
    }
    fun redeemPoint(@Body req: RedeemPointReq):Observable<BaseResp<RedeemPointResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).redeemPoint(req)
    }

    fun inquireBalance(): Observable<BaseResp<BalanceDetailResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).balanceDetail()
    }

    //Online wallet
    fun inquireAddress():Observable<BaseResp<InquireAddressResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).inquireAddress()
    }

    //transition wallet
    fun listCoin(): Observable<BaseResp<ListCoinResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).listCoin()
    }

    fun summary(): Observable<BaseResp<TransitWalletSummary>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).summary()
    }

    fun transactionDetail(req:InquireTransactionDetailReq):Observable<BaseResp<InquirePointsDetailResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).transactionDetail(req)
    }
    fun transactionInOrOutDetail(req:InquirePointsDetailReq):Observable<BaseResp<InquirePointsDetailResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).transactionInOrOutDetail(req)
    }
    fun exchangeDetail(req:InquireTransactionDetailReq):Observable<BaseResp<InquirePointsDetailResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).exchangeDetail(req)
    }
    fun exchangeInOrOutDetail(req:InquirePointsDetailReq):Observable<BaseResp<InquirePointsDetailResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).exchangeInOrOutDetail(req)
    }
    fun energyDetail(req:InquireTransactionDetailReq):Observable<BaseResp<InquireEnergyDetailResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).inquirePointsDetail(req)
    }
    fun energyInOrOutDetail(req:InquirePointsDetailReq):Observable<BaseResp<InquireEnergyDetailResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).inquirePointsInOrOutDetail(req)
    }

    fun walletOutEvent(req:TransferReq):Observable<BaseResp<Int>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).walletOutEvent(req);
    }
    fun ExchangeOutEvent(req:TransferOasReq):Observable<BaseResp<TransferOasResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).transferOas(req)
    }
    fun getAppUpdate():Observable<BaseResp<AppUpdateResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).getAppUpdate()
    }
    fun widthdraw(req: TransferReq):Observable<BaseResp<Int>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).withdraw(req)
    }
    fun reverseWithdraw(req: TransferOasReq):Observable<BaseResp<Int>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).reverseWithdraw(req)
    }
    fun getOasExtra():Observable<BaseResp<String>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).getOasExtra()
    }
    fun getExchangeResult(req:PointItem):Observable<BaseResp<Int>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).getExchangeResult(req)
    }

    fun getKYCVerifyStatus():Observable<BaseResp<KYCVerifyStatusResp>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).getKYCVerifyStatus()
    }

    fun getDayMoneyTotal():Observable<BaseResp<BigDecimal>>{
        return RetrofitFactory.instance.create(WalletApi::class.java).getDayMoneyTotal()
    }


}
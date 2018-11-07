package com.oases.data.api

import com.oases.base.data.protocol.BaseResp
import com.oases.computingpower.data.protocol.KYCVerifyStatusResp
import com.oases.data.protocol.*
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.math.BigDecimal

interface WalletApi {

    //My points
    @POST("/api/v1/energyPoint/inquireEnergyPoint")
    fun inquirePoints(): Observable<BaseResp<PointsInfo>>

    @POST("/api/v1/energyPoint/inquireEnergyPointDetail")
    fun inquirePointsDetail(@Body req: InquireTransactionDetailReq): Observable<BaseResp<InquireEnergyDetailResp>>
    @POST("/api/v1/energyPoint/inquireEnergyPointDetail")
    fun inquirePointsInOrOutDetail(@Body req: InquirePointsDetailReq): Observable<BaseResp<InquireEnergyDetailResp>>

    @POST("/api/v1/energyPoint/inquireCurrentPeriodEnergyPoint")
    fun inquireCurrentPeriodPoints(): Observable<BaseResp<CurrentPeriodPointsResp>>

    @POST("/api/v1/energyPoint/redeemPoint")
    fun redeemPoint(@Body req: RedeemPointReq):Observable<BaseResp<RedeemPointResp>>

    @POST("/api/v1/energyPoint/inquirePointFactor")
    fun inquirePointFactor(@Body req: InquirePointFactorReq): Observable<BaseResp<InquirePointFactorResp>>

    //Online wallet
    @POST ("/api/v1/userWallet/balanceDetail")
    fun balanceDetail(): Observable<BaseResp<BalanceDetailResp>>

    @POST ("/api/v1/userWallet/transactionDetail")
    fun transactionDetail(@Body req: InquireTransactionDetailReq):Observable<BaseResp<InquirePointsDetailResp>>

    @POST ("/api/v1/userWallet/transactionDetail")
    fun transactionInOrOutDetail(@Body req: InquirePointsDetailReq):Observable<BaseResp<InquirePointsDetailResp>>

    @POST ("/api/v1/userWallet/inquireAddress")
    fun inquireAddress():Observable<BaseResp<InquireAddressResp>>

    @POST ("/api/v1/userWallet/transfer")
    fun walletOutEvent(@Body req: TransferReq):Observable<BaseResp<Int>>

    @POST("/api/v1/userWallet/withdraw")
    fun withdraw(@Body req: TransferReq):Observable<BaseResp<Int>>

    @GET("/api/v1/userWallet/getOasExtra")
    fun getOasExtra():Observable<BaseResp<OasResp>>


    //Transition Wallet
    @POST("/api/v1/ethWallet/transfer")
    fun transferOas(@Body req: TransferOasReq): Observable<BaseResp<TransferOasResp>>

    @POST ("/api/v1/ethWallet/listCoin")
    fun listCoin():Observable<BaseResp<ListCoinResp>>

    @POST ("/api/v1/ethWallet/summary")
    fun summary():Observable<BaseResp<TransitWalletSummary>>

    @POST ("/api/v1/ethWallet/transactionDetail")
    fun exchangeDetail(@Body req: InquireTransactionDetailReq):Observable<BaseResp<InquirePointsDetailResp>>

    @POST ("/api/v1/ethWallet/transactionDetail")
    fun exchangeInOrOutDetail(@Body req: InquirePointsDetailReq):Observable<BaseResp<InquirePointsDetailResp>>

    @POST ("/api/v1/ethWallet/reverseWithdraw")
    fun reverseWithdraw(@Body req: TransferOasReq):Observable<BaseResp<Int>>

    @POST("/api/v1/ethWallet/getExchangeResult")
    fun getExchangeResult(@Body req:PointItem):Observable<BaseResp<Int>>


    @POST(" /api/v1/userCenter/downloadApp")
    fun getAppUpdate():Observable<BaseResp<AppUpdateResp>>

    @POST("/api/v1/userCenter/inqureUserIdentityInfo")
    fun getKYCVerifyStatus():Observable<BaseResp<KYCVerifyStatusResp>>

    @POST("/api/v1/userWallet/inqureDailyTotalAmount")
    fun getDayMoneyTotal():Observable<BaseResp<BigDecimal>>


}
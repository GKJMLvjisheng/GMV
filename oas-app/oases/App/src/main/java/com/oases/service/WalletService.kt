package com.oases.service

import com.oases.base.data.protocol.BaseResp
import com.oases.computingpower.data.protocol.KYCVerifyStatusResp
import com.oases.data.protocol.*
import io.reactivex.Observable
import retrofit2.http.Body
import java.math.BigDecimal

interface WalletService {
 fun inquirePoints(): Observable<PointsInfo>
 // fun inquirePointsDetail(pageNumber: Int, size: Int): Observable<InquirePointsDetailResp>
 fun inquireCurrentPeriodPoints(): Observable<CurrentPeriodPointsResp>

 fun inquirePointsFactor(req: InquirePointFactorReq): Observable<InquirePointFactorResp>
 fun inquireBalance(): Observable<BalanceDetailResp>
 fun listCoin(): Observable<ListCoinResp>
 fun summary(): Observable<TransitWalletSummary>
 fun redeemPoint(@Body req: RedeemPointReq): Observable<RedeemPointResp>
 fun inquireAddress(): Observable<InquireAddressResp>
 /**
  * 交易记录:在线钱包/交易钱包/能量积分
  */
 fun transactionDetail(req: InquireTransactionDetailReq): Observable<InquirePointsDetailResp>

 fun exchangeDetail(req: InquireTransactionDetailReq): Observable<InquirePointsDetailResp>
 fun energyDetail(req: InquireTransactionDetailReq): Observable<InquireEnergyDetailResp>
 /**
  * 交易记录(带参数)
  */
 fun transactionInOrOutDetail(req: InquirePointsDetailReq): Observable<InquirePointsDetailResp>

 fun exchangeInOrOutDetail(req: InquirePointsDetailReq): Observable<InquirePointsDetailResp>
 fun energyInOrOutDetail(req: InquirePointsDetailReq): Observable<InquireEnergyDetailResp>
 /**
  * 在线钱包-转出金额
  */
 fun walletOutEvent(req: TransferReq): Observable<Int>

 /**
  * 交易钱包-转出
  */
 fun ExchangeOutEvent(req: TransferOasReq): Observable<TransferOasResp>

 /**
  * 获取app更新版本
  */
 fun getAppUpdate(): Observable<AppUpdateResp>

 /**
  * 提币
  */
 fun withdraw(req: TransferReq): Observable<Int>

 /**
  * 充币
  */
 fun reverseWithdraw(req: TransferOasReq): Observable<Int>

 /**
  * 获取oas手续费
  */
 fun getOasExtra(): Observable<String>

 /**
  * 获取交易钱包交易记录获取转账结果
  */
 fun getExchangeResult(req: PointItem): Observable<Int>
   /**
    * 获取KYC状态
    */
   fun getKYCVerifyStatus():Observable<KYCVerifyStatusResp>

 /**
  * 获取用户的日总额
  */
 fun getDayMoneyTotal():Observable<BigDecimal>



}
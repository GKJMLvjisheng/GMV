package com.oases.computingpower.data.protocol

import java.math.BigDecimal

data class BuyingMinerHistoryReq (val pageNum: Int, val pageSize: Int)
data class MinerHistoryItem(val minerName:String,val minerStatus:Int,val minerNum:Int,val created:String,val minerEndTime:String,val minerPrice:BigDecimal,val minerPower:BigDecimal,val minerPeriod:Int)
data class BuyingMinerHistoryResp (val pageNum: Int, val pageSize: Int,val rows:MutableList<MinerHistoryItem>)
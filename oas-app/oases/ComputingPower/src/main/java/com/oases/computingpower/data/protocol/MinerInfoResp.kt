package com.oases.computingpower.data.protocol

import java.math.BigDecimal

data class MinerInfoReq (val pageNum: Int, val pageSize: Int)
data class MinerInfoItem(val minerName:String,val minerPrice:BigDecimal,val minerPower:BigDecimal,val minerPeriod:Int,val minerMaxOutput:BigDecimal,val minerGrade:Int)
data class MinerInfoResp (val pageNum: Int, val pageSize: Int,val rows:MutableList<MinerInfoItem>)
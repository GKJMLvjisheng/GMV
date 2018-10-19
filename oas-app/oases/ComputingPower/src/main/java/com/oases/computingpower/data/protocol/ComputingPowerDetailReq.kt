package com.oases.computingpower.data.protocol

import java.math.BigDecimal

data class ComputingPowerDetailReq (val pageNum: Int, val pageSize: Int)

data class ComputingPowerItem(val activity:String?, val created:String,val value:Int,val powerChange:BigDecimal,val category:String?,val inOrOut:Int)
data class ComputingPowerDetailResp (val pageNum:Int, val pageSize:Int, val rows:ArrayList<ComputingPowerItem>)
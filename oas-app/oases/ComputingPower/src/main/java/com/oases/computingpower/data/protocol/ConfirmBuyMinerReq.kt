package com.oases.computingpower.data.protocol

import java.math.BigDecimal

data class ConfirmBuyMinerReq (val minerName:String,val minerNum:Int,val priceSum:BigDecimal)
package com.oases.data.protocol

import java.math.BigDecimal

data class RedeemPointReq (val period: String,
                           val type:Int,
                           val factor:BigDecimal,
                           val amount:BigDecimal)
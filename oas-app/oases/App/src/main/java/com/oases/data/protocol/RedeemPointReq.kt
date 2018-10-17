package com.oases.data.protocol

data class RedeemPointReq (val period: String,
                           val type:Int,
                           val factor:Double,
                           val amount:Double)
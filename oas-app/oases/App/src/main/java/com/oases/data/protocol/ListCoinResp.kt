package com.oases.data.protocol

import java.math.BigInteger

data class CoinItem(val name:String, val balance:Double, val value:Double,val address:String,val contract:String,val ethBalance:Double,val unconfirmedBalance:Double)//,
//data class ListCoinResp (val data: Array<CoinItem>)
typealias ListCoinResp =  Array<CoinItem>
package com.oases.data.protocol

import java.math.BigDecimal

data class TransferOasReq(val toUserAddress: Address,val amount:BigDecimal,val contract:String,val gasPrice:BigDecimal,val gasLimit:BigDecimal,val remark:String)//,val changeAddress:String
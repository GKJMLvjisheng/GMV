package com.oases.computingpower.data.protocol

data  class PowerActivityStatusResp (val activityResultList:List<Contents>)
data class Contents (val sourceCode: Int,val sourceName:String,val status:Int)
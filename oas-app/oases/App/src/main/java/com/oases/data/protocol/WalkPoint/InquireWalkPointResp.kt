package com.oases.data.protocol.WalkPoint

data class WalkType(val uuid:String, val type:String, val value:Double, val startDate: String)
typealias InquireWalkPointResp = List<WalkType>
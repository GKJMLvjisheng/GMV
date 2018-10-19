package com.oases.data.protocol.WalkPoint

data class WalkType(val startDate: String)
data class InquireWalkPointResp(val data: List<WalkType>)
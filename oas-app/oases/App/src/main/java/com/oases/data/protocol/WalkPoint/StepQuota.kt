package com.oases.data.protocol.WalkPoint

data class StepItem(val date: String, val stepNum: Int)
typealias StepQuota =  List<StepItem>
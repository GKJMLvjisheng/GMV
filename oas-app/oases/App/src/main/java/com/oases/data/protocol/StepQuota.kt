package com.oases.data.protocol

data class StepItem(val date: String, val stepNum: Int)
typealias StepQuota =  Array<StepItem>
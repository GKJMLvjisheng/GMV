package com.oases.data.protocol

data class InquirePointsDetailReq (val pageNum: Int, val pageSize: Int)
data class InquireTransactionDetailReq (val pageNum: Int, val pageSize: Int,val inOrOut:Int)
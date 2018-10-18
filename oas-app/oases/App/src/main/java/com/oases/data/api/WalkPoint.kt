package com.oases.data.api

import com.oases.base.data.protocol.BaseResp
import com.oases.data.protocol.WalkPoint.InquireWalkPointReq
import com.oases.data.protocol.WalkPoint.InquireWalkPointResp
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface WalkPoint {
    @POST("/api/v1/walkPoint/inquireWalkPointBall")
    fun inquireWalkPointBall(@Body req: InquireWalkPointReq): Observable<BaseResp<InquireWalkPointResp>>
}
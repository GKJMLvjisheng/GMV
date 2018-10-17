package com.oases.data.api

import com.oases.base.data.protocol.BaseResp
import com.oases.data.protocol.WalkPoint.InquireWalkPointResp
import io.reactivex.Observable
import retrofit2.http.POST

interface WalkPoint {
    @POST("/api/v1/walkPoint/inquireWalkPointBall")
    fun inquireWalkPointBall(): Observable<BaseResp<InquireWalkPointResp>>
}
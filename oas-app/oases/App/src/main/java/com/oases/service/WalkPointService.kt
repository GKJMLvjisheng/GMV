package com.oases.service

import com.oases.data.protocol.WalkPoint.InquireWalkPointResp
import io.reactivex.Observable

interface WalkPointService {
    fun inquireWalkPoint(): Observable<InquireWalkPointResp>
}
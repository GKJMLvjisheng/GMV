package com.oases.data.repository

import com.oases.base.data.net.RetrofitFactory
import com.oases.base.data.protocol.BaseResp
import com.oases.data.api.WalkPoint
import com.oases.data.protocol.WalkPoint.InquireWalkPointReq
import com.oases.data.protocol.WalkPoint.InquireWalkPointResp
import io.reactivex.Observable
import javax.inject.Inject

class WalkPointRepository @Inject constructor() {
    //My point
    fun inquireWalkPoint(req: InquireWalkPointReq): Observable<BaseResp<InquireWalkPointResp>> {
        return RetrofitFactory.instance.create(WalkPoint::class.java).inquireWalkPointBall(req)
    }
}
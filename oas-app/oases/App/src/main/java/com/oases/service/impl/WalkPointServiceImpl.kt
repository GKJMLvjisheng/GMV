package com.oases.service.impl

import com.oases.base.ext.convert
import com.oases.data.protocol.WalkPoint.InquireWalkPointReq
import com.oases.data.protocol.WalkPoint.InquireWalkPointResp
import com.oases.data.repository.WalkPointRepository
import com.oases.service.WalkPointService
import io.reactivex.Observable
import javax.inject.Inject

class WalkPointServiceImpl @Inject constructor(): WalkPointService {
    @Inject
    lateinit var repository: WalkPointRepository

    override fun inquireWalkPoint(req: InquireWalkPointReq): Observable<InquireWalkPointResp> {
        return repository.inquireWalkPoint(req).convert()
    }
}
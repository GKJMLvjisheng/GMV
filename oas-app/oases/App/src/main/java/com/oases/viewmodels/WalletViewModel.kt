package com.oases.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.oases.base.ext.execute
import com.oases.base.rx.BaseSubscriber
import com.oases.base.rx.BaseViewModelSubscriber
import com.oases.data.protocol.PointsInfo
import com.oases.service.WalletService
import rx.Observer
import javax.inject.Inject

class WalletViewModel: ViewModel(){
    val pointsInfo = MutableLiveData<PointsInfo>()
    @Inject
    lateinit var walletService: WalletService

    fun getPoints():LiveData<PointsInfo> {
        return pointsInfo
    }

    private fun loadPoints(){
        walletService.inquirePoints().execute(object : BaseViewModelSubscriber<PointsInfo> (){
            override fun onNext(t: PointsInfo) {
                pointsInfo.setValue(t)
            }
        })
    }
}


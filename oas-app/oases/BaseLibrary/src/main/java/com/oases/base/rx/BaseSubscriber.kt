package com.oases.base.rx

import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter.printStackTrace
import com.oases.base.presenter.view.BaseView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/*
    Rx订阅者默认实现
 */
open class BaseSubscriber<T>(val baseView: BaseView) : Observer<T> {

    override fun onSubscribe(p0: Disposable) {
    }

    override fun onComplete() {
        baseView.hideLoading()
    }

    override fun onNext(t: T) {
    }

    override fun onError(e: Throwable) {
        baseView.hideLoading()
        if (e is BaseException) {
            if (e.code == 10001){
                //finish(), todo
            }
            baseView.onError(e.msg)
        } else if (e is DataNullException){
            baseView.onDataIsNull()
        }
        else {
            baseView.onError(e.toString())
            Log.d("zbb", "receive message ${e.toString()}")
            e.printStackTrace()
        }
    }
}

open class BaseViewModelSubscriber<T>() : Observer<T> {
    override fun onSubscribe(p0: Disposable) {
    }

    override fun onComplete() {
    }

    override fun onNext(t: T) {
    }

    override fun onError(e: Throwable) {
    }
}
package com.oases.base.rx

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.arouter.launcher.ARouter.printStackTrace
import com.alibaba.android.arouter.routes.`ARouter$$Providers$$arouterapi`
import com.oases.base.common.AppManager
import com.oases.base.common.BaseConstant
import com.oases.base.presenter.view.BaseView
import com.oases.base.utils.AppPrefsUtils
import com.today.step.lib.TodayStepService
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.*

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
                Log.d("zbb","error 10001")
                AppPrefsUtils.putString(BaseConstant.USER_TOKEN,"")
                /*var appManager:AppManager = AppManager.instance
                *//*var nowActivity =  appManager.currentActivity()
                appManager.finishActivity(nowActivity)*//*
                var stack: Stack<Activity> = appManager.getAllActivity()
                for(s in stack){
                    Log.i("zbb","now activity".plus(s))
                    if(s !=  TodayStepService::class.java){
                        appManager.finishActivity(s)
                    }
                }
                ARouter.getInstance().build("/userCenter/login").navigation()*/

                var appManager: AppManager = AppManager.instance
                var stack: Stack<Activity> = appManager.getAllActivity()
                if(stack.size!=0){
                    var list:MutableList<Activity> = arrayListOf()
                    for(activity in stack){
                        list.add(activity)
                    }
                    for(activity in list){
                        Log.i("zbb","now activity".plus(activity.localClassName))

                        if(activity.localClassName == "ui.activity.MainActivity"){
                            Log.i("zbb","acti")
                        }else{
                            appManager.finishActivity(activity)
                        }

                    }
                }
                ARouter.getInstance().build("/userCenter/login").navigation()
                return
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
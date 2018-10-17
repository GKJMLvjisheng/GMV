package com.oases.base.rx

import android.util.Log
import com.oases.base.common.ResultCode
import com.oases.base.data.protocol.BaseResp
import io.reactivex.Observable
import io.reactivex.functions.Function

/*
    Boolean类型转换封装
 */
class BaseFuncBoolean<T>: Function<BaseResp<T>, Observable<Boolean>> {
    override fun apply(t: BaseResp<T>): Observable<Boolean> {
        if (t.code != ResultCode.SUCCESS){
            return Observable.error(BaseException(t.code, t.message))
        }
        Log.d("zbb", "convert to boolean")
        return Observable.just(true)
    }
}

package com.oases.base.ext

import android.graphics.drawable.AnimationDrawable
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.kennyc.view.MultiStateView
import com.oases.base.R
import com.oases.base.data.protocol.BaseResp
import com.oases.base.rx.BaseFunc
import com.oases.base.rx.BaseFuncBoolean
import com.oases.base.rx.BaseSubscriber
import com.oases.base.utils.GlideUtils
import com.oases.base.widgets.DefaultTextWatcher
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.find

//Kotlin通用扩展


/*
    扩展Observable执行
 */
fun <T> Observable<T>.execute(subscriber: BaseSubscriber<T>, lifecycleProvider: LifecycleProvider<*>) {
    this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(lifecycleProvider.bindToLifecycle())
            .subscribe(subscriber)

}

fun <T> Observable<T>.execute(subscriber: Observer<T>) {
    this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber)
}
/*
    扩展数据转换
 */
fun <T> Observable<BaseResp<T>>.convert():Observable<T>{
    return this.flatMap(BaseFunc())
}

/*
    扩展Boolean类型数据转换
 */
fun <T> Observable<BaseResp<T>>.convertBoolean():Observable<Boolean>{
    return this.flatMap(BaseFuncBoolean())
}

/*
    扩展点击事件
 */
fun View.onClick(listener:View.OnClickListener):View{
    setOnClickListener(listener)
    return this
}

fun SeekBar.onProgressChanged(method: () -> Unit){
    setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean){
                method()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar){

            }

            override fun onStopTrackingTouch(seekBar: SeekBar){

            }
        })
    }
/*
    扩展点击事件，参数为方法
 */
fun View.onClick(method:() -> Unit):View{
    setOnClickListener { method() }
    return this
}

/*
    扩展Button可用性
 */
fun Button.enable(et:EditText,method: () -> Boolean){
    val btn = this
    et.addTextChangedListener(object : DefaultTextWatcher(){
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            btn.isEnabled = method()
        }
    })
}

/*
    ImageView加载网络图片
 */
fun ImageView.loadUrl(url: String) {
    GlideUtils.loadUrlImage(context, url, this)
}

/*
    多状态视图开始加载
 */
fun MultiStateView.startLoading(){
    viewState = MultiStateView.VIEW_STATE_LOADING
    val loadingView = getView(MultiStateView.VIEW_STATE_LOADING)
    val animBackground = loadingView!!.find<View>(R.id.loading_anim_view).background
    (animBackground as AnimationDrawable).start()
}

/*
    扩展视图可见性
 */
fun View.setVisible(visible:Boolean){
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun TextView.onRightDrawableClickListener(method: ()->Unit){
    val tv = this
    tv.setOnTouchListener { _, event ->
        val DRAWABLE_RIGHT = 2
        if (event.getAction() == MotionEvent.ACTION_UP){
            if (event.rawX >= (tv.right - tv.compoundDrawables[DRAWABLE_RIGHT].bounds.width()))
                method()
            true
        }else
            false
    }
}

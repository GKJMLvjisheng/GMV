package com.oases.base.ui.activity

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.oases.base.common.BaseApplication
import com.oases.base.injection.component.ActivityComponent
import com.oases.base.injection.component.DaggerActivityComponent
import com.oases.base.injection.module.ActivityModule
import com.oases.base.injection.module.LifecycleProviderModule
import com.oases.base.widgets.ProgressLoading
import com.oases.base.presenter.BasePresenter
import com.oases.base.presenter.view.BaseView
import org.jetbrains.anko.toast
import javax.inject.Inject

/**
 * Created by Bingo Zhu
 * 2018/8/14
 */
abstract class BaseMvpActivity<T : BasePresenter<*>> : BaseActivity(), BaseView {

    //Presenter泛型，Dagger注入
    @Inject
    lateinit var mPresenter: T

    lateinit var mActivityComponent: ActivityComponent

    private lateinit var mLoadingDialog: ProgressLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivityInjection()
        injectComponent()

        //初始加载框
        mLoadingDialog = ProgressLoading.create(this)
        //ARouter注册
    }

    /*
        Dagger注册
     */
    protected abstract fun injectComponent()

    /*
        初始Activity Component
     */
    private fun initActivityInjection() {
        mActivityComponent = DaggerActivityComponent.builder().appComponent((application as BaseApplication).appComponent)
                .activityModule(ActivityModule(this))
                .lifecycleProviderModule(LifecycleProviderModule(this))
                .build()

    }

    /*
        显示加载框，默认实现
     */
    override fun showLoading() {
        mLoadingDialog.showLoading()
    }

    /*
        隐藏加载框，默认实现
     */
    override fun hideLoading() {
        mLoadingDialog.hideLoading()
    }

    /*
        错误信息提示，默认实现
     */
    override fun onError(text:String) {
        toast(text)
    }
}

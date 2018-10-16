package com.oases.computingpower.ui.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.oases.base.common.BaseConstant
import com.oases.base.ext.setVisible
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.computingpower.R
import com.oases.computingpower.adapter.RecycleDetailViewAdapter
import com.oases.computingpower.data.protocol.ComputingPowerDetailResp
import com.oases.computingpower.injection.component.DaggerComputingPowerComponent
import com.oases.computingpower.injection.module.ComputingPowerModule
import com.oases.computingpower.presenter.ComputingPowerHistoryPresenter
import com.oases.computingpower.presenter.view.ComputingPowerHistoryView
import kotlinx.android.synthetic.main.activity_computing_power_history.*
import java.util.zip.Inflater

class ComputingPowerHistoryActivity : BaseMvpActivity<ComputingPowerHistoryPresenter>(), ComputingPowerHistoryView {
    private var mRecyclerView :RecyclerView ? = null
    private var adapter : RecycleDetailViewAdapter ? =null
    private var mPageCount:Int = 1
    private var mPageSum:Int = 0
    private var loadFlag:Boolean =true
    private var tip:String = "正在加载..."
    private var lastVisibleItemPosition:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_computing_power_history)
        mPresenter.inquirePowerDetail(1,BaseConstant.PAGE_SIZE)
    }

    override fun injectComponent() {
        DaggerComputingPowerComponent
                .builder()
                .activityComponent(mActivityComponent)
                .computingPowerModule(ComputingPowerModule())
                .build()
                .inject(this)
        mPresenter.mView = this
    }

    override fun OnGetInquirePowerDetail(result: ComputingPowerDetailResp) {
        val list = result.rows
        if(mPageCount ==1){
            if(list.isNotEmpty()){
                mRecyclerView = computerPowerRecyclerView
                mRecyclerView!!.layoutManager = LinearLayoutManager(this)
                adapter =  RecycleDetailViewAdapter(list)
                mRecyclerView!!.adapter =adapter

                mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        val lm = recyclerView.layoutManager as LinearLayoutManager
                        //item总数
                        val totalItemCount = recyclerView.adapter.itemCount
                        //最后一个可见的位置
                        lastVisibleItemPosition = lm.findLastVisibleItemPosition()
                        //第一个可见item的位置
                        val visibleItemCount = recyclerView.childCount
                        if (newState==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition==totalItemCount - 1  && visibleItemCount > 0) {
                            //加载更多
                            mBottomShow.text = tip
                            mBottomShow.setVisible(true)
                            mPageCount++
                            mPageSum+=totalItemCount
                            if(loadFlag){ //小于总条数加载加载更多
                                mPresenter.inquirePowerDetail(mPageCount,BaseConstant.PAGE_SIZE)
                            }
                        }
                    }
                    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                        if(dy<0){
                            mBottomShow.setVisible(false)
                        }
                    }
                })
            }
        }else{
            if(list.isNotEmpty()){
                adapter!!.setData(list)
                mBottomShow.setVisible(false)
            }else{
                tip = "无更多的数据..."
                mBottomShow.text = tip
                mBottomShow.setVisible(true)
                loadFlag = false
            }
        }
    }

}

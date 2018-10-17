package com.oases.computingpower.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.oases.base.common.BaseConstant
import com.oases.base.ext.setVisible
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.computingpower.R
import com.oases.computingpower.adapter.RecycleMinerHistoryViewAdapter
import com.oases.computingpower.data.protocol.BuyingMinerHistoryResp
import com.oases.computingpower.injection.component.DaggerComputingPowerComponent
import com.oases.computingpower.injection.module.ComputingPowerModule
import com.oases.computingpower.presenter.BuyingMinerHistoryPresenter
import com.oases.computingpower.presenter.view.BuyingMinerHistoryView
import kotlinx.android.synthetic.main.activity_buying_miner_history.*

class BuyingMinerHistoryActivity : BaseMvpActivity<BuyingMinerHistoryPresenter>(), BuyingMinerHistoryView {

    private var mRecyclerView : RecyclerView? = null
    private var adapter : RecycleMinerHistoryViewAdapter ? =null
    private var mPageCount:Int = 1
    private var mPageSum:Int = 0
    private var loadFlag:Boolean =true
    private var tip:String = "正在加载..."
    private var lastVisibleItemPosition:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buying_miner_history)
        mPresenter.getBuyingMinerHistory(1, BaseConstant.PAGE_SIZE)
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


    override fun onGetBuyingMinerHistoryResult(result: BuyingMinerHistoryResp) {
        val list = result.rows
        if(mPageCount ==1){
            if(list.isNotEmpty()){
                mRecyclerView = minerBuyHistoryRecyclerView
                mRecyclerView!!.layoutManager = LinearLayoutManager(this)
                adapter =  RecycleMinerHistoryViewAdapter(list)
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
                                mPresenter.getBuyingMinerHistory(mPageCount,BaseConstant.PAGE_SIZE)
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

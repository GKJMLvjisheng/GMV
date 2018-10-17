package com.oases.computingpower.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ext.setVisible
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.computingpower.R
import com.oases.computingpower.adapter.RecycleMinerViewAdapter
import com.oases.computingpower.data.protocol.MinerInfoItem
import com.oases.computingpower.data.protocol.MinerInfoResp
import com.oases.computingpower.injection.component.DaggerComputingPowerComponent
import com.oases.computingpower.injection.module.ComputingPowerModule
import com.oases.computingpower.presenter.BuyingMinerPresenter
import com.oases.computingpower.presenter.view.BuyingMinerView
import com.oases.computingpower.utils.SpaceItemDecoration
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.activity_buying_miner.*
import kotlinx.android.synthetic.main.miner_item.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class BuyingMinerActivity : BaseMvpActivity<BuyingMinerPresenter>(), BuyingMinerView {

    private var mRecyclerView :RecyclerView ? = null
    private var adapter : RecycleMinerViewAdapter = RecycleMinerViewAdapter(mutableListOf(),null,this@BuyingMinerActivity)
    private var mPageCount:Int = 1
    private var mPageSum:Int = 0
    private var loadFlag:Boolean =true
    private var tip:String = "正在加载..."
    private var lastVisibleItemPosition:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buying_miner)
        initView()
    }

    fun initView(){
        mPresenter.getMinerInfo(1,BaseConstant.PAGE_SIZE)

        /*mBuyButton.onClick {
            alertView()
        }*/

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


    //弹框，用户购买确认信息
    /*private fun alertView(){
        var dialogBuilder = AlertDialog.Builder(this@BuyingMinerActivity)
        var  selectList:Array<String> = arrayOf("名称：","数量：","总计：")

        dialogBuilder.setItems(selectList,DialogInterface.OnClickListener{
            dialog, id ->
        })

        dialogBuilder?.setMessage("请您核对购买信息：")?.setPositiveButton("购买", DialogInterface.OnClickListener{
            dialog, id ->
            //此处填写实现购买的方法，购买成功然后跳进购买记录里面
          //  mPresenter.confirmBuyMiner(minerName,minerNum,minerTotalAccount)
        })?.setNegativeButton("取消", null)

        val dialog = dialogBuilder?.create() as AlertDialog
        dialog.show()
        dialog.run {
            getButton(AlertDialog.BUTTON_POSITIVE).background = resources.getDrawable(R.drawable.authentication)
            getButton(AlertDialog.BUTTON_NEGATIVE).background = resources.getDrawable(R.drawable.authentication)
            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.common_bg))
            getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.common_bg))
        }
    }*/

    override fun onGetMinerInfoResult(result: MinerInfoResp) {
        val list = result.rows

        if(mPageCount ==1){
            if(list.isNotEmpty()){
                mRecyclerView = mMinerRecyclerView
                mRecyclerView!!.layoutManager = LinearLayoutManager(this)
                adapter =  RecycleMinerViewAdapter(list,BuyingMinerHistoryActivity(),BuyingMinerActivity())
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
                                mPresenter.getMinerInfo(mPageCount,BaseConstant.PAGE_SIZE)
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

    override fun onConfirmBuyMinerResult(result: Int) {
        if (result==0){
          //  startActivity<BuyingMinerHistoryActivity>()
        }else{
            toast("购买失败")
        }
    }


}

package com.oases.computingpower.ui.activity

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.graphics.drawable.toDrawable
import com.oases.base.common.BaseApplication.Companion.context
import com.oases.base.common.BaseConstant
import com.oases.base.ext.setVisible
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.computingpower.R
import com.oases.computingpower.adapter.RecycleMinerViewAdapter
import com.oases.computingpower.data.protocol.MinerInfoResp
import com.oases.computingpower.injection.component.DaggerComputingPowerComponent
import com.oases.computingpower.injection.module.ComputingPowerModule
import com.oases.computingpower.presenter.BuyingMinerPresenter
import com.oases.computingpower.presenter.view.BuyingMinerView
import kotlinx.android.synthetic.main.activity_buying_miner.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import com.oases.computingpower.adapter.RecycleMinerViewAdapter.SubClickListener
import org.w3c.dom.Text
import java.math.BigDecimal


class BuyingMinerActivity : BaseMvpActivity<BuyingMinerPresenter>(), BuyingMinerView{

    private var mRecyclerView :RecyclerView ? = null
    private var adapter : RecycleMinerViewAdapter ?= null
    private var mPageCount:Int = 1
    private var mPageSum:Int = 0
    private var loadFlag:Boolean =true
    private var tip:String = "正在加载..."
    private var lastVisibleItemPosition:Int = 0
    private var mMinerName:String =""
    private var mMinerNum:Int =0
    private lateinit var mMinerSum:BigDecimal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buying_miner)
        initView()
    }

    fun initView(){
        mPresenter.getMinerInfo(1,BaseConstant.PAGE_SIZE)

        mHeadBar.onClickRightTv {
            startActivity<BuyingMinerHistoryActivity>()
        }
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
    private  fun alertView(){
        var dialogBuilder = AlertDialog.Builder(this@BuyingMinerActivity)
        var view = View.inflate(context,R.layout.buy_miner_dialog,null)
        val dialogBtnConfirm = view.findViewById(R.id.mMinerConfirm) as Button
        val dialogBtnCancel = view.findViewById(R.id.mMinerCancel) as Button
        var name = view.findViewById(R.id.mSelectMinerName) as TextView
        name.text=mMinerName
        var num = view.findViewById(R.id.mSelectMinerNum) as TextView
        num.text=mMinerNum.toString()
        var sum = view.findViewById(R.id.mSelectMinerSum) as TextView
        sum.text= mMinerSum.toString()
        dialogBuilder.setView(view)
        dialogBuilder.setCancelable(true)
        val dialog = dialogBuilder?.create() as AlertDialog
        dialogBtnConfirm.setOnClickListener {
            dialog.dismiss()
            mPresenter.confirmBuyMiner(name.text.toString(),num.text.toString().toInt(),sum.text.toString().toBigDecimal())
        }
        dialogBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
        /*dialogBuilder?.setMessage("请您核对购买信息：".plus("\n").plus("\n").plus("名称：").plus(mMinerName).plus("\n").plus("数量：").plus(mMinerNum).plus("\n").plus("总计：").plus(mMinerSum).plus("（OAS）").plus("\n"))?.setPositiveButton("购买", DialogInterface.OnClickListener{
            _, _ ->
            //此处填写实现购买的方法，购买成功然后跳进购买记录里面
            mPresenter.confirmBuyMiner(mMinerName,mMinerNum,mMinerSum)
        })?.setNegativeButton("取消", null)*/
        dialog.show()
        /*dialog.run {
            getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.authentication)
            getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.authentication)
            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(-0x1) //白色
            getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(-0x1)
        }*/
    }



    override fun onGetMinerInfoResult(result: MinerInfoResp) {
        val list = result.rows

        if(mPageCount ==1){
            if(list.isNotEmpty()){
                mRecyclerView = mMinerRecyclerView
                mRecyclerView!!.layoutManager = LinearLayoutManager(this)
                adapter =  RecycleMinerViewAdapter(list)

                //此处为弹框，用户进行购买
                adapter!!.setsubClickListener(object : SubClickListener {
                    override fun onTopicClickListener(minerName: String, minerNum: Int, minerSum: BigDecimal) {
                        mMinerName = minerName
                        mMinerNum = minerNum
                        mMinerSum = minerSum
                        Log.d("sssss","mMinerNum：$mMinerNum, mMinerSum: $mMinerSum")
                        if (mMinerNum==0){
                            toast("您选择的矿机数量不能为0，请重新选择")
                        }else {
                            alertView()
                        }

                    }
                })


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
            startActivity<BuyingMinerHistoryActivity>()
        }else{
            toast("购买失败")
        }
    }

    override fun onResume() {
        super.onResume()
        initView()
    }


}

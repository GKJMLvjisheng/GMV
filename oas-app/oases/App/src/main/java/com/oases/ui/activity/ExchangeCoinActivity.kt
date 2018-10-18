package com.oases.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.oases.R
import com.oases.base.common.BaseApplication.Companion.context
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ext.setVisible
import com.oases.base.ui.activity.BaseActivity
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.data.protocol.*
import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.ExchangeDetailPresenter
import com.oases.presenter.view.ExchangeDetailView
import com.oases.ui.adapter.ExchangeItemRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_coin.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity

@Route(path = "/app/ExchangeCoinActivity")
class ExchangeCoinActivity : BaseMvpActivity<ExchangeDetailPresenter>(),ExchangeDetailView{
    private var lists:ArrayList<PointItem> = ArrayList<PointItem>()
    private var list2:ArrayList<EnergyItem> = ArrayList<EnergyItem>()
    private val adapter: ExchangeItemRecyclerViewAdapter = ExchangeItemRecyclerViewAdapter(lists,list2,"EXCHANGE",null,null,null,this@ExchangeCoinActivity)
    private lateinit var mExchangeRecord: View
    private lateinit var recycleView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var mPageCount:Int = 1
    private var mPageSum:Int = 0
    private var loadFlag:Boolean =true
    private var tip:String = "正在加载..."
    private var lastVisibleItemPosition:Int = 0
    private var totalItems:ArrayList<PointItem>? =null
    private var outItems:ArrayList<PointItem>? =null
    private var inItems:ArrayList<PointItem>? =null
    private var balance:String? = "0"
    private var unconfirmedBalance:String? = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin)

       /* var balance:String = intent.extras.getString("balance")
       nowCoin.setRightTopText(balance)
       sumCoin.setRightTopText(intent.extras.getString("value"))
       waitRorExchange.setRightTopText(AppPrefsUtils.getString(BaseConstant.ON_GOING_TRANSACTION))*/

        mBuyOas.onClick {
           // var pair:Pair<String,String> = Pair<String,String>({"balance":balance,"unconfirmed":unconfirmedBalance})
            startActivity<OasExchangeToOnlineActivity>("balance" to balance,"unconfirmed" to unconfirmedBalance) //Toast.makeText(this, "即将上线", Toast.LENGTH_SHORT).show()
        }
        mExchangeBtn.onClick {
            //var pair:Pair<String,String> = Pair<String,String>("balance",balance?:"")
            startActivity(intentFor<ExchangeOutActivity>("balance" to balance,"unconfirmed" to unconfirmedBalance).singleTop().clearTop())
        }
        mCoinMoreDetail.onClick {
            val req = InquirePointsDetailReq(1, BaseConstant.PAGE_SIZE)
            val outReq = InquireTransactionDetailReq(1,BaseConstant.PAGE_SIZE,0)
            val inReq = InquireTransactionDetailReq(1,BaseConstant.PAGE_SIZE,1)
            mPresenter.exchangeOasMoreDetail(req, outReq,inReq)
        }

        viewManager = LinearLayoutManager(context)
        recycleView =  mExchaangeRecyclerView
        recycleView.layoutManager = viewManager

        recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        getData(mPageCount)
                    }
                }
            }
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if(dy<0){
                    mBottomShow.setVisible(false)
                }
            }

        })
        recycleView.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        Log.d("zht","coin is onresume")
        adapter.clearData()
        getData(1)
        mPresenter.listCoin()
    }

    private fun getData(pageNum:Int){
        val req = InquirePointsDetailReq(pageNum, BaseConstant.PAGE_SIZE)
        val outReq = InquireTransactionDetailReq(pageNum,BaseConstant.PAGE_SIZE,0)
        mPresenter.onExchangeTransactionAll(req,outReq)
    }
    override fun injectComponent() {
        DaggerMainComponent
                .builder()
                .activityComponent(mActivityComponent)
                .walletModule(WalletModule())
                .build()
                .inject(this)
        mPresenter.mView = this
    }

    override fun onGetTransactionMoreDeails(list: InquirePointsDetailResp) {
        //if(list!=null){
            val items = list.rows
            if(!items.isEmpty()){
                adapter.update(items)
                mBottomShow.setVisible(false)
            }else{
                tip = "无更多的数据..."
                mBottomShow.text = tip
                mBottomShow.setVisible(true)
                loadFlag = false;
            }
       // }
    }
    private fun checkAndStartDetialActivity()
    {
        if (totalItems != null &&  outItems != null && inItems != null) {
            startActivity<ExchangeDetailActivity>(
                    BaseConstant.TOTAL_ITEM to totalItems!!,
                    BaseConstant.OUT_ITEM to outItems!!,
                    BaseConstant.IN_ITEM to inItems!!,
                    BaseConstant.MORE_TYPE to "EXCHANGE")
            totalItems = null
            outItems = null
            inItems = null
        }
    }
    override fun onGetTransactionOutDeails(list: InquirePointsDetailResp) {
        outItems = list.rows
        checkAndStartDetialActivity()
    }

    override fun onGetTransactionInDeails(list: InquirePointsDetailResp) {
        inItems = list.rows
        checkAndStartDetialActivity()
    }

    override fun onGetEnergyMoreDeails(list: InquireEnergyDetailResp) {

    }
    override fun onGetOasAllDeails(list: InquirePointsDetailResp) {
        totalItems = list.rows
        checkAndStartDetialActivity()
    }

    override fun setCoin(coins: ListCoinResp) {
        balance = coins.userCoin[0].balance.toString()
        unconfirmedBalance = coins.userCoin[0].unconfirmedBalance.toString()
        nowCoin.setRightTopText(coins.userCoin[0].balance.toString())
        sumCoin.setRightTopText("≈ ¥".plus(coins.userCoin[0].value.toString()))
        waitRorExchange.setRightTopText(coins.userCoin[0].unconfirmedBalance.toString())//AppPrefsUtils.getString(BaseConstant.ON_GOING_TRANSACTION)
    }
    override fun getExchangeResult(t: Int) {
        if(t == 1){
            getData(1)
        }
    }


}


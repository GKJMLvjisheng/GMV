package com.oases.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oases.R
import com.oases.base.common.BaseConstant
import com.oases.base.ui.fragment.BaseMvpFragment
import com.oases.base.utils.AppPrefsUtils
import com.oases.data.protocol.*
import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.ExchangeDetailPresenter
import com.oases.presenter.view.ExchangeDetailView
import com.oases.ui.adapter.ExchangeItemRecyclerViewAdapter


private const val ARG_ITEMS = "items"
private const val AEG_ITEMS2 = "item2"
/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ExchangeItemFragment.OnListFragmentInteractionListener] interface.
 */
class ExchangeItemFragment: BaseMvpFragment<ExchangeDetailPresenter>(), ExchangeDetailView {
    private var listener: OnListFragmentInteractionListener? = null
    private var mItems: ArrayList<PointItem> = ArrayList()
    private var eItems: ArrayList<EnergyItem> = ArrayList()
    private var cItems: ArrayList<PointItem> = ArrayList()
    private var lastVisibleItemPosition:Int = 0
    private var mPageCount:Int = 1
    private var mPageSum:Int = 0
    private var loadFlag:Boolean =true
    private var tip:String = "正在加载..."
    private var index:Int = 0 //第几个framgent
    private lateinit var mRecycleView:RecyclerView
    private var flag:String = ""
    private lateinit var type:String //= AppPrefsUtils.getString(BaseConstant.MORE_TYPE)
    //private lateinit var mBottomShow:TextView

    override fun injectComponent() {
        Log.d("zbb", "init exchange fragment")
        DaggerMainComponent
                .builder()
                .activityComponent(mActivityComponent)
                .walletModule(WalletModule())
                .build()
                .inject(this)
        mPresenter.mView = this

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
           /* eItems = it.getParcelableArrayList(ARG_ITEMS)
            mItems = it.getParcelableArrayList(AEG_ITEMS2)*/
           when(type){
                "ENERGY"->  eItems = it.getParcelableArrayList(ARG_ITEMS)
                "WALLET"->  mItems = it.getParcelableArrayList(ARG_ITEMS)
                "EXCHANGE"->  cItems = it.getParcelableArrayList(ARG_ITEMS)
                else -> eItems = it.getParcelableArrayList(ARG_ITEMS)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mRecycleView = inflater.inflate(R.layout.fragment_exchangeitem_list, container, false) as RecyclerView
       // mRecycleView =   rootFragment.findViewById(R.id.mRecycleViewId)  as RecyclerView
        mRecycleView.layoutManager = LinearLayoutManager(context)
        when(type){
            "ENERGY"->  mRecycleView.adapter = ExchangeItemRecyclerViewAdapter(mItems,eItems,type, listener,this@ExchangeItemFragment,null,null)
            "WALLET"->  mRecycleView.adapter = ExchangeItemRecyclerViewAdapter(mItems,eItems,type, listener,this@ExchangeItemFragment,null,null)
            "EXCHANGE"->  mRecycleView.adapter = ExchangeItemRecyclerViewAdapter(cItems,eItems,type, listener,this@ExchangeItemFragment,null,null)
            else -> mRecycleView.adapter = ExchangeItemRecyclerViewAdapter(mItems,eItems,type,listener,this@ExchangeItemFragment,null,null)
        }
        mRecycleView.adapter.notifyDataSetChanged()
       /* var viewLayout:LinearLayout = LinearLayout(context)
        viewLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        var inflater2: LayoutInflater = LayoutInflater.from(context)
        val textView:TextView = inflater2.inflate(R.layout.bottom_tip,container,false) as TextView
        viewLayout.addView(textView)
        container!!.addView(viewLayout)*/
       // mBottomShow = LayoutInflater.from(context).inflate(R.layout.bottom_tip,container,false).findViewById(R.id.mBottomShow)
        return mRecycleView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(view as RecyclerView) {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        //mBottomShow.text = tip
                       // mBottomShow.setVisible(true)
                        mPageCount++
                        mPageSum += totalItemCount
                        if(loadFlag){ //小于总条数加载加载更多
                            var req:InquirePointsDetailReq = InquirePointsDetailReq(mPageCount,10)
                            var req2:InquireTransactionDetailReq = InquireTransactionDetailReq(mPageCount,10,0)
                            var req3:InquireTransactionDetailReq = InquireTransactionDetailReq(mPageCount,10,1)

                            flag = type.plus("_").plus(AppPrefsUtils.getString(BaseConstant.FRAGMENT_INDEX))
                            when(flag){
                                "ENERGY_ALL"->  mPresenter.onEnergyTransactionAll(req,req2)//EnumList.ENEYGE_ALL
                                "ENERGY_OUT"->  mPresenter.onEnergyTransactionOut(req,req2)
                                "ENERGY_IN"->   mPresenter.onEnergyTransactionIn(req,req3)
                                "WALLET_ALL"-> mPresenter.onWalletTransactionAll(req,req2)
                                "WALLET_OUT"-> mPresenter.onWalletTransactionOut(req,req2)
                                "WALLET_IN"->  mPresenter.onWalletTransactionIn(req,req3)
                                "EXCHANGE_ALL"-> mPresenter.onExchangeTransactionAll(req,req2)
                                "EXCHANGE_OUT"-> mPresenter.onExchangeTransactionOut(req,req2)
                                "EXCHANGE_IN"->    mPresenter.onExchangeTransactionIn(req,req3)
                                //else -> ""
                            }
                        }
                    }
                }
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    if(dy<0){
                        //mBottomShow.setVisible(false)
                    }
                }
            })
         /*   addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
                    Log.d("zht","124")
                }

                override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
                   return false
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                }
            })*/
        }
    }
/*     fun alertWindow(str:String){
        *//*val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(str).setNegativeButton("关闭弹框", null)
        val dialog = dialogBuilder.create() as AlertDialog
        dialog.show()
        dialog.findViewById<TextView>(android.R.id.message)?.gravity = Gravity.CENTER*//*
        val mMaterialDialog = OptionMaterialDialog(BaseApplication.context)
        mMaterialDialog
                .setMessage(str)
                .setNegativeButton("关闭弹窗") { mMaterialDialog.dismiss() }
                .setCanceledOnTouchOutside(true)
                .show()
    }*/
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: PointItem?,item2:EnergyItem?)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WalletOnLineFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(items: ArrayList<PointItem>,item2:ArrayList<EnergyItem>,typeOut:String) =
                ExchangeItemFragment().apply {
                    arguments = Bundle().apply {
                        type= typeOut//AppPrefsUtils.getString(BaseConstant.MORE_TYPE)
                        when(type){
                            "ENERGY"-> putParcelableArrayList(ARG_ITEMS, item2)
                            "WALLET"-> putParcelableArrayList(ARG_ITEMS, items)
                            "EXCHANGE"-> putParcelableArrayList(ARG_ITEMS, items)
                            else -> putParcelableArrayList(ARG_ITEMS, item2)
                        }
                        //putParcelableArrayList(ARG_ITEMS, items)
                        //putParcelableArrayList(AEG_ITEMS2, item2)
                    }
                }

    }

    fun handleResult(list: InquirePointsDetailResp){
        if(list.rows !=null && !list.rows.isEmpty()){
            when(type){
                "WALLET"->  mItems.addAll(list.rows)
                "EXCHANGE"->  cItems.addAll(list.rows)
            }
            mRecycleView.adapter.notifyDataSetChanged()
        }else{
            tip = "无更多的数据..."
           // mBottomShow.text = tip
            //mBottomShow.setVisible(true)
            loadFlag = false;
        }
    }

    override fun onGetTransactionMoreDeails(list: InquirePointsDetailResp) {
        handleResult(list)
    }

    override fun onGetTransactionOutDeails(list: InquirePointsDetailResp) {
        handleResult(list)
    }

    override fun onGetTransactionInDeails(list: InquirePointsDetailResp) {
        handleResult(list)
    }
    override fun onGetEnergyMoreDeails(list: InquireEnergyDetailResp){
        if(!list.rows.isEmpty()){
            eItems.addAll(list.rows)
            mRecycleView.adapter.notifyDataSetChanged()

        }else{
            tip = "无更多的数据..."
            //mBottomShow.text = tip
           // mBottomShow.setVisible(true)
            loadFlag = false;
        }
    }
    override fun onGetOasAllDeails(list: InquirePointsDetailResp) {
    }
    override fun setCoin(coins: ListCoinResp) {
    }

    override fun getExchangeResult(t: Int) {

    }
}

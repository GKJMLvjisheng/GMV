package com.oases.ui.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.oases.R
import com.oases.R.id.mBottomShow

import com.oases.base.common.BaseConstant.Companion.MY_OAS_ADDRESS
import com.oases.base.common.BaseConstant.Companion.MY_OAS_AMOUNT
import com.oases.base.ext.onClick

import com.oases.base.common.BaseConstant
import com.oases.base.common.BaseConstant.Companion.MORE_TYPE
import com.oases.base.ext.onClick
import com.oases.base.ext.setVisible
import com.oases.base.ui.fragment.BaseMvpFragment
import com.oases.base.utils.AppPrefsUtils
import com.oases.computingpower.data.protocol.KYCVerifyStatusResp
import com.oases.data.protocol.*

import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.MyPointsPresenter
import com.oases.presenter.OnLineWalletPresenter
import com.oases.presenter.view.MyPointsView
import com.oases.presenter.view.OnLineWalletView
import com.oases.ui.activity.ExchangeDetailActivity
import com.oases.ui.activity.RedrawOasActivity
import com.oases.ui.activity.WalletOutActivity
import com.oases.ui.adapter.ExchangeItemRecyclerViewAdapter
//import kotlinx.android.synthetic.main.fragment_my_points.*
import kotlinx.android.synthetic.main.fragment_wallet_on_line.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [WalletOnLineFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [WalletOnLineFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
@Route(path = "/app/WalletOnLineFragment")    //路径参考Provider中RounterPath的写法
class WalletOnLineFragment : BaseMvpFragment<OnLineWalletPresenter>(), OnLineWalletView {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var totalItems:ArrayList<PointItem>? =null
    private var outItems:ArrayList<PointItem>? =null
    private var inItems:ArrayList<PointItem>? =null
    private var mPageCount:Int = 1
    private var mPageSum:Int = 0
    private var loadFlag:Boolean =true
    private var tip:String = "正在加载..."
    private var lastVisibleItemPosition:Int = 0
    private var lists:ArrayList<PointItem> = ArrayList<PointItem>()
    private var list2:ArrayList<EnergyItem> = ArrayList<EnergyItem>()
    private  val adapter:ExchangeItemRecyclerViewAdapter = ExchangeItemRecyclerViewAdapter(lists,list2,"WALLET",null,null,this@WalletOnLineFragment,null)
  //  private lateinit var mFragScroll:ScrollView
    private lateinit var mExchangeRecord:View
    private lateinit var recycleView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private  lateinit var mOasHead: View
    private lateinit var mToolBar: View
    private var KYCVerifyStatus:Int =0
    private var KYCVerifyInfo:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val rootFragment = inflater.inflate(R.layout.fragment_wallet_on_line, container, false)
        viewManager = LinearLayoutManager(context)
        recycleView =  rootFragment.findViewById(R.id.walletOnRecyclerView)
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
                        mPresenter.transactionDetail(mPageCount,BaseConstant.PAGE_SIZE)
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
//        mFragScroll = rootFragment.findViewById(R.id.mFragmentScrollView)
        onFragmentScroll()
        mExchangeRecord = rootFragment.findViewById<AppBarLayout>(R.id.mOasHeadLayout)
        mOasHead = rootFragment.findViewById(R.id.mOasHead)
        mToolBar = rootFragment.findViewById(R.id.mToolbar)

        rootFragment.findViewById<AppBarLayout>(R.id.mOasHeadLayout).addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val offset = Math.abs(verticalOffset)
            Log.d("zbb", "scrollbar offset $offset")

            if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                mOasHead.visibility = View.GONE
                mToolbar.visibility = View.VISIBLE
            } else {
                mOasHead.visibility = View.VISIBLE
                mToolbar.visibility  = View.GONE

            }
        }
        return rootFragment

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRedrawOas.onClick {
            //lh修改   添加KYC认证后的操作  认证后才可提币
            //mPresenter.inquireAddress()
            //mPresenter.getKYCVerifyStatus()
            if (KYCVerifyStatus==0){
                toast("您还未进行KYC认证，请先进行KYC认证")
                //弹窗
                alertView()
            }
            if (KYCVerifyStatus==1){
                toast("您的KYC认证正在审核中,暂时无法提币")
            }
            if (KYCVerifyStatus==2){
                //审核状态已通过，可以进行提币
                mPresenter.inquireAddress()
            }

            if (KYCVerifyStatus==3){
                toast("您的KYC认证审核未通过："+KYCVerifyInfo+"，因此不能提币")
                //弹窗
                alertView()
            }
        }
        mExchangeBtn.onClick{
            //lh修改携带KYC状态进行转账跳转
            //startActivity<WalletOutActivity>()
            //alertView()
            intentKYCVerifyStatus()
        }
        mMoreWalletDetail.onClick {
            mPresenter.transactionOnWalletDetail(1, BaseConstant.PAGE_SIZE)
            //checkAndStartDetialActivity()
        }
    }

    //携带KYC状态跳入转账页面
    private fun intentKYCVerifyStatus (){
        var intent = Intent(context,WalletOutActivity::class.java)
        var bundle = Bundle()
        bundle.putString("KYCVerifyStatus",KYCVerifyStatus.toString())
        bundle.putString("KYCVerifyInfo",KYCVerifyInfo)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    //KYC审核状态显示
    override fun onGetKYCVerifyStatus(result: KYCVerifyStatusResp) {
        KYCVerifyStatus = result.verifyStatus
        KYCVerifyInfo = result.remark
    }

    //弹框，提醒用户去KYC认证
    private fun alertView(){
        var dialogBuilder = AlertDialog.Builder(this.context!!)

        dialogBuilder?.setMessage("进行提现、转账操作时需要通过KYC认证审核")?.setPositiveButton("前往认证", DialogInterface.OnClickListener{
                dialog, id ->
            ARouter.getInstance().build("/computingPower/KYCActivity").navigation()
            })?.setNegativeButton("取消", null)

        val dialog = dialogBuilder?.create() as AlertDialog
        dialog.show()
        //dialog.findViewById<TextView>(android.R.id.message)?.gravity = Gravity.CENTER
       // dialog.findViewById<TextView>(android.R.id.button1)?.gravity = Gravity.CENTER
       // dialog.findViewById<Button>(AlertDialog.BUTTON_NEGATIVE)?.gravity = Gravity.LEFT
       // dialog.findViewById<Button>(AlertDialog.BUTTON_POSITIVE)?.gravity = Gravity.RIGHT
        dialog.run {
            getButton(AlertDialog.BUTTON_POSITIVE).background = resources.getDrawable(R.drawable.button_kyc1)
            getButton(AlertDialog.BUTTON_NEGATIVE).background = resources.getDrawable(R.drawable.button_kyc1)
            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.common_bg))
            getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.common_bg))
        }
    }


    override fun onInquireAddress(address: InquireAddressResp) {
        startActivity<RedrawOasActivity>(
                MY_OAS_AMOUNT to mCurrentPoints.text
        )
    }
    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onWalletOnLineFragmentInteraction(uri)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            Log.d("zbb", "WALLET, visible $isVisibleToUser")
            Log.i("zbb",AppPrefsUtils.getString(BaseConstant.MORE_TYPE))
            updateData()
        }
    }

    private fun updateData(){
        AppPrefsUtils.putString(BaseConstant.MORE_TYPE,"WALLET")
        adapter.clearData()
        mPresenter.inquireBalance()
        mPresenter.transactionDetail(1,BaseConstant.PAGE_SIZE)
        mPresenter.getKYCVerifyStatus()     //一进页面直接去取用户的KYC状态
        initParam()
    }
    override fun onResume() {
        super.onResume()
        Log.d("zbb", "WALLET, on resume")
        updateData()
    }
    fun initParam(){
        mPageCount = 1
        mPageSum = 0
        loadFlag = true
        tip = "正在加载..."
        lastVisibleItemPosition = 0
    }
    override fun injectComponent() {
        DaggerMainComponent
                .builder()
                .activityComponent(mActivityComponent)
                .walletModule(WalletModule())
                .build()
                .inject(this)
        mPresenter.mView = this
        Log.d("zbb", "OnLine Wallet injected")
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun setBalance(balance: BalanceDetailResp) {
        mCurrentPoints.text = balance.availableBalance.toString()
        mOngoingTransaction.text = balance.ongoingBalance.toString()
        //AppPrefsUtils.putString(BaseConstant.ON_GOING_TRANSACTION,balance.ongoingBalance.toString())
        mOasValue.text = "≈ ¥ " + balance.availableBalanceValue.toString()
    }

    private fun checkAndStartDetialActivity()
    {
        if (totalItems != null &&  outItems != null && inItems != null){
            Log.i("zbb", "start detail activity")
            startActivity<ExchangeDetailActivity>(
                    BaseConstant.TOTAL_ITEM to totalItems!!,
                    BaseConstant.OUT_ITEM to outItems!!,
                    BaseConstant.IN_ITEM to inItems!!,
                    BaseConstant.MORE_TYPE to "WALLET")
          //  AppPrefsUtils.putString(BaseConstant.MORE_TYPE,"WALLET")
            totalItems = null
            outItems = null
            inItems = null
        }
      /*  startActivity<ExchangeDetailActivity>(
                BaseConstant.MORE_TYPE to 1
        )*/
    }

    override fun onGetTransactionDeails(list: InquirePointsDetailResp){
       // Log.d("zbb", "onGetTransactionDeails")
        if(!list.rows.isEmpty()){
            adapter.update(list.rows)
           // adapter.notifyItemRangeChanged(lastVisibleItemPosition,list.rows.size-lastVisibleItemPosition)

            mBottomShow.setVisible(false)
        }else{
            tip = "无更多的数据..."
            mBottomShow.text = tip
            mBottomShow.setVisible(true)
            loadFlag = false;
        }

    }

    override fun onGetTransactionMoreDeails(list: InquirePointsDetailResp){
        Log.d("zbb", "onGetTransactionMoreDeails")
        totalItems = list.rows
        checkAndStartDetialActivity()
    }

    override fun onGetTransactionOutDeails(list: InquirePointsDetailResp) {
        Log.d("zbb", "onGetTransactionOutDeails")
        outItems = list.rows
        checkAndStartDetialActivity()
    }

    override fun onGetTransactionInDeails(list: InquirePointsDetailResp) {
        Log.d("zbb", "onGetTransactionInDeails")
        inItems = list.rows
        checkAndStartDetialActivity()
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onWalletOnLineFragmentInteraction(uri: Uri)
    }

    fun onFragmentScroll(){

      /*
        mFragScroll.viewTreeObserver.addOnScrollChangedListener {
            Log.d("zbb", "frag scroll ${mFragmentScrollView.scrollY}")
            Log.d("zbb", "recycle view scroll ${mFragmentScrollView.scrollY}")
            if (mFragmentScrollView.scrollY > 668){
                //mFragScroll.smoothScrollTo(0, 688)
              //  recycleView.smoothScrollToPosition(mFragmentScrollView.scrollY)
            }
        }*/
        //mFragmentScrollView.setOnScrollChangeListener { fragmentView, x, y, oldX, oldY ->  }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                WalletOnLineFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}

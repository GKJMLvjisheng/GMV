package com.oases.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ParseException
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ExpandableListView

import com.oases.R
import com.oases.base.common.BaseConstant
import com.oases.base.common.BaseConstant.Companion.IN_ITEM
import com.oases.base.common.BaseConstant.Companion.MORE_TYPE
import com.oases.base.common.BaseConstant.Companion.OUT_ITEM
import com.oases.base.common.BaseConstant.Companion.TOTAL_ITEM
import com.oases.base.ext.onClick
import com.oases.base.ui.fragment.BaseMvpFragment
import com.oases.base.utils.AppPrefsUtils
import com.oases.data.protocol.*
import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.MyPointsPresenter
import com.oases.presenter.view.MyPointsView
import com.oases.ui.activity.ExchangePointsActivity
import com.oases.ui.activity.ExchangeDetailActivity
import com.oases.ui.adapter.MyPointsGroupAdapter
import kotlinx.android.synthetic.main.fragment_my_points.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.startActivity
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MyPointsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MyPointsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MyPointsFragment : BaseMvpFragment<MyPointsPresenter>(), MyPointsView {
    private var listener: OnFragmentInteractionListener? = null
    private var isInjected = false
    private var totalItems:ArrayList<EnergyItem>? =null
    private var outItems:ArrayList<EnergyItem>? =null
    private var inItems:ArrayList<EnergyItem>? =null
    /*private val i:Intent = Intent()
    private val bundle = Bundle()*/
    lateinit var exAdapter:MyPointsGroupAdapter
    var groupData:MutableList<String>?=null
    var itemData :MutableList<MutableList<EnergyItem>>?=null
    lateinit var swipeLayout:SwipeRefreshLayout
    lateinit var expandableListView:ExpandableListView

    override fun injectComponent() {
        DaggerMainComponent
                .builder()
                .activityComponent(mActivityComponent)
                .walletModule(WalletModule())
                .build()
                .inject(this)
        Log.d("zbb", "MyPoints injected")
        mPresenter.mView = this
        isInjected = true
        //i.setClass(context,ExchangeDetailActivity::class.java)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d("zbb", "mypoints, visible $isVisibleToUser")
        if (isVisibleToUser && isInjected){
            AppPrefsUtils.putString(BaseConstant.MORE_TYPE,"ENERGY")
            updatePoints()
            getDetail()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        var rootFragment = inflater.inflate(R.layout.fragment_my_points, container, false)
        Log.d("zbb", "pointsfragment gpu ${rootFragment.layerType}")
        exAdapter = MyPointsGroupAdapter(this.context!!)

        expandableListView = rootFragment.findViewById(R.id.mPointGroupLayout) as ExpandableListView
        expandableListView.setAdapter(exAdapter)
        //expandableListView.expandGroup(0);//设置第一组张开
        //expandableListView.collapseGroup(0); 将第group组收起
      //  expandableListView.setGroupIndicator(null)//除去自带的箭头，自带的箭头在父列表的最左边，不展开向下，展开向上
       // expandableListView.setDivider(null)//这个是设定每个Group之间的分割线。,默认有分割线，设置null没有分割线
        expandableListView.setOnChildClickListener(object : ExpandableListView.OnChildClickListener {
            override fun onChildClick(parent: ExpandableListView, view: View, groupPosition: Int,
                             childPosition: Int, id: Long): Boolean {
                // TODO Auto-generated method stub
                exAdapter.setChildSelection(groupPosition, childPosition)
                return true
            }
        })
        swipeLayout = rootFragment.findViewById(R.id.mSwipeLayout) as SwipeRefreshLayout
        swipeLayout.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                var mHandler:Handler  = Handler()
                mHandler.postDelayed(object :Runnable{
                    override fun run() {
                        getDetail()
                    }
                },1000)
            }
        })
        return rootFragment
    }

    override fun onGetPoints(points: Float) {
        mCurrentPoints.text = points.toString()
        Log.d("zbb", "points ${points}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("zbb", "my points view created")

        mExchangePointsBtn.setOnClickListener(
                {startActivity<ExchangePointsActivity>()}
        )
        mMoreDetail.onClick {
            mPresenter.energyDetail(1,BaseConstant.PAGE_SIZE)
        }
    }

    fun checkAndStartDetialActivity()
    {
        if (totalItems != null &&  outItems != null && inItems != null){
            Log.d("zbb", "start exchange activity in my point")
            startActivity<ExchangeDetailActivity>(
                    TOTAL_ITEM to totalItems!!,
                    OUT_ITEM  to outItems!!,
                    IN_ITEM to inItems!! ,
                    MORE_TYPE to "ENERGY")
           // startActivity<ExchangeDetailActivity>(MORE_TYPE to 1)
           // AppPrefsUtils.putString(BaseConstant.MORE_TYPE,"ENERGY")
            totalItems = null
            outItems = null
            inItems = null
        }
    }

    override fun onResume() {
        super.onResume()
        //AppPrefsUtils.putString(BaseConstant.MORE_TYPE,"ENERGY")
        Log.d("zbb", "my points on resume")
        updatePoints()
        getDetail()
    }
    override fun onGetCurrentPeriodPoints(currentPeriodPointsResp: CurrentPeriodPointsResp){
        mCurrentProduce.text = currentPeriodPointsResp.producedEnergyPoint
        mCurrentSpend.text = currentPeriodPointsResp.consumedEnergyPoint
    }


    fun updatePoints(){
        if (isInjected) {
            mPresenter.inquirePoints()
            //  mPresenter.inquirePointsDetail(1, BaseConstant.PAGE_SIZE)
            mPresenter.inquireCurrentPeriodPoints()
        }
    }
    fun getDetail(){
        if (isInjected) {
            mPresenter.inquirePointsDetail(1,1000)
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        return listener!!.onMyPointsFragmentInteraction(uri)
    }

    override fun onGetPointsDeails(list:InquireEnergyDetailResp) {
       // if(list!=null){
            swipeLayout.setRefreshing(false)
            if(list.rows.size>0){
                val items = list.rows
                var map:Map<String,ArrayList<EnergyItem>> = mapOf<String,ArrayList<EnergyItem>>()
                map = map.toMutableMap()
                for(item in items){
                    if(!item.created.isNullOrEmpty()){
                        var time = item.created!!.substring(0,7).plus(" 第").plus(getWeekInfo(item.created)).plus("周")
                        if(!map.containsKey(time)){
                            var energys = ArrayList<EnergyItem>()
                            energys.add(item)
                            map[time] = energys
                        }else{
                            var existsItems:ArrayList<EnergyItem> = map.get(time)!!
                            existsItems.add(item)
                            map[time] = existsItems
                        }


                    }
                }
                var group: MutableList<String>? =  null
                var item: MutableList<MutableList<EnergyItem>> ?=null
                for(key in map.keys){
                    group?.add(key)
                    item?.add(map[key]!!)
                }
                exAdapter.pushData(map)

                var groupNumber = exAdapter.getGroupCount()
                for( i in 1..groupNumber){
                    expandableListView.collapseGroup(i-1)
                }

              //  exAdapter.notifyDataSetChanged()
            }
       // }
    }
    override fun onGetTransactionDeails(list: InquireEnergyDetailResp){
        totalItems = list.rows
        checkAndStartDetialActivity()
    }

    override fun onGetTransactionOutDeails(list: InquireEnergyDetailResp) {
        outItems = list.rows
       checkAndStartDetialActivity()
    }

    override fun onGetTransactionInDeails(list: InquireEnergyDetailResp) {
        inItems = list.rows
        checkAndStartDetialActivity()
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

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onMyPointsFragmentInteraction(uri: Uri)
    }

    fun getWeekInfo(date:String):Int{
        var result:Int = 1
        val cal:Calendar = Calendar.getInstance()
        val format:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try{
            cal.time= format.parse(date)
            result = cal.get(Calendar.WEEK_OF_YEAR)
        }catch (e: ParseException){
            e.printStackTrace()
        }finally {
            return result
        }
    }

}

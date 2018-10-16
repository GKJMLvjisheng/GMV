package com.oases.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import com.oases.R
import com.oases.base.common.BaseConstant
import com.oases.base.common.BaseConstant.Companion.IN_ITEM
import com.oases.base.common.BaseConstant.Companion.OUT_ITEM
import com.oases.base.common.BaseConstant.Companion.TOTAL_ITEM
import com.oases.base.ui.activity.BaseActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.data.protocol.EnergyItem
import com.oases.data.protocol.PointItem
import com.oases.ui.adapter.WalletAdapter
import com.oases.ui.fragment.ExchangeItemFragment
import kotlinx.android.synthetic.main.activity_exchange_detail.*
import kotlinx.android.synthetic.main.activity_exchange_detail.view.*
import kotlin.collections.ArrayList


class ExchangeDetailActivity : BaseActivity(), ExchangeItemFragment.OnListFragmentInteractionListener{
    private  var  totalItems: ArrayList<PointItem> = ArrayList<PointItem>()
    private  var  outItems: ArrayList<PointItem> = ArrayList<PointItem>()
    private  var  inItems: ArrayList<PointItem> = ArrayList<PointItem>()

    private  var  totalItem2: ArrayList<EnergyItem> =ArrayList<EnergyItem>()
    private  var  outItem2: ArrayList<EnergyItem> =ArrayList<EnergyItem>()
    private  var  inItem2: ArrayList<EnergyItem> =ArrayList<EnergyItem>()
    //private var  type :String= AppPrefsUtils.getString(BaseConstant.MORE_TYPE)
    private lateinit var type:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = intent.getStringExtra(BaseConstant.MORE_TYPE)
        Log.i("zbb11",type)
        if(type.equals("ENERGY")){
            totalItem2 = intent.getParcelableArrayListExtra<EnergyItem>(TOTAL_ITEM)
            outItem2 = intent.getParcelableArrayListExtra<EnergyItem>(OUT_ITEM)
            inItem2 = intent.getParcelableArrayListExtra<EnergyItem>(IN_ITEM)
        }else if(type.equals("WALLET") || type.equals("EXCHANGE")){
            totalItems = intent.getParcelableArrayListExtra<PointItem>(TOTAL_ITEM)
            outItems = intent.getParcelableArrayListExtra<PointItem>(OUT_ITEM)
            inItems = intent.getParcelableArrayListExtra<PointItem>(IN_ITEM)
        }else{
            totalItem2 = intent.getParcelableArrayListExtra<EnergyItem>(TOTAL_ITEM)
            outItem2 = intent.getParcelableArrayListExtra<EnergyItem>(OUT_ITEM)
            inItem2 = intent.getParcelableArrayListExtra<EnergyItem>(IN_ITEM)
        }
        setContentView(R.layout.activity_exchange_detail)
        setupViewPager()


       mViewPager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener {
           override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
               AppPrefsUtils.putString(BaseConstant.FRAGMENT_INDEX,getFragmentIndex(mViewPager.currentItem))
           }
           override fun onPageSelected(position: Int) {
               AppPrefsUtils.putString(BaseConstant.FRAGMENT_INDEX,getFragmentIndex(mViewPager.currentItem))
            }
           override fun onPageScrollStateChanged(state: Int) {
               AppPrefsUtils.putString(BaseConstant.FRAGMENT_INDEX,getFragmentIndex(mViewPager.currentItem))
           }

        })
    }
    //根據下標獲取第幾個fragment
    fun getFragmentIndex(index:Int):String{
      return when(index){
            0-> "ALL"
            1->"OUT"
            2->"IN"
          else ->"ALL"
        }
    }
   //@RequiresApi(Build.VERSION_CODES.M)
    private fun setupViewPager() {
        val titles = arrayOf("全部", "转出", "转入")
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[0]))
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[1]))
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[2]))

        val fragments = arrayOf(
                ExchangeItemFragment.newInstance(totalItems,totalItem2,type),
                ExchangeItemFragment.newInstance(outItems,outItem2,type),
                ExchangeItemFragment.newInstance(inItems,inItem2,type))
        val adapter = WalletAdapter(supportFragmentManager, fragments, titles)
        mViewPager.setAdapter(adapter)
        mTabLayout.setupWithViewPager(mViewPager)
    }

    override fun onListFragmentInteraction(item: PointItem?,item2:EnergyItem?) {
        //Todo to communication
    }
}


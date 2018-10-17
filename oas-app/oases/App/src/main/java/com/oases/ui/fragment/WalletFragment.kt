package com.oases.ui.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.app.ListFragment
import android.arch.lifecycle.ViewModelProvider
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.oases.R
import com.oases.base.ui.fragment.BaseFragment
import com.oases.ui.adapter.WalletAdapter

class WalletFragment : BaseFragment(){
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager
    private val mFragments  by lazy {arrayOf(MyPointsFragment(), WalletOnLineFragment(), TransitWalletFragment())}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d("zbb", "WalletFragment, create view")

        var rootFragment = inflater.inflate(R.layout.fragment_wallet, null)
        mTabLayout = rootFragment.findViewById(R.id.mTabLayout)
        mViewPager = rootFragment.findViewById(R.id.mViewPager)
        setupViewPager()
        //mPresenter.transactionDetail(1,BaseConstant.PAGE_SIZE)
        return rootFragment
    }

    private  fun setupViewPager(){
        val titles = arrayOf("能量积分", "在线钱包", "交易钱包")
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[0]))
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[1]))
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[2]))

        mViewPager.adapter = WalletAdapter(
                getChildFragmentManager(),
                mFragments,
                titles)

        mTabLayout.setupWithViewPager(mViewPager)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d("zbb", "my wallet visible hint $isVisibleToUser")
        if (isVisibleToUser){
            //todo change to view model to sync data between fragments
            (mFragments[0] as MyPointsFragment).updatePoints()
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onWalletFragmentInteraction(uri)
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
        fun onWalletFragmentInteraction(uri: Uri)
    }

}

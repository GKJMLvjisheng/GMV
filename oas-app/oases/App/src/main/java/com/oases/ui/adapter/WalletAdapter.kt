package com.oases.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AlertDialog
import android.util.Log

class WalletAdapter(fm: FragmentManager, val mFragments:Array<out Fragment>, val mTitles:Array<String>) : FragmentStatePagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence? {
        Log.d("zbb", "WalletAdapter get tile $position")
        return mTitles[position]
    }

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getCount(): Int {
        return mTitles.size
    }
}

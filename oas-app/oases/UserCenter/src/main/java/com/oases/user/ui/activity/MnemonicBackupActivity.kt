package com.oases.user.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import com.oases.ui.adapter.MnemonicBackupAdapter
import com.oases.user.R
import kotlinx.android.synthetic.main.activity_mnemonic_backup.*

class MnemonicBackupActivity : AppCompatActivity() {

    /*private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager
    private val mFragments  by lazy {arrayOf(EncryptedMnemonicFragment(), PrivateKeyMnemonicFragment())}*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mnemonic_backup)
        initView()
    }

    private fun initView(){
        mTabLayout.tabMode = TabLayout.MODE_FIXED
        mViewPager.adapter = MnemonicBackupAdapter(supportFragmentManager)
        //mTabLayout关联mViewPager
        mTabLayout.setupWithViewPager(mViewPager)
    }

}

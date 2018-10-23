
package com.oases.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.oases.user.ui.fragment.EncryptedMnemonicFragment
import com.oases.user.ui.fragment.PrivateKeyMnemonicFragment

class MnemonicBackupAdapter(fm:FragmentManager): FragmentPagerAdapter(fm) {

    private val titles = arrayOf("私钥助记词","加密助记词")

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 0){
            EncryptedMnemonicFragment()
        }else{
            PrivateKeyMnemonicFragment()
        }
    }

    override fun getCount(): Int {
        return titles.size
    }

}

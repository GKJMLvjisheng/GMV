package com.oases.user.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.darsh.multipleimageselect.helpers.Constants
import com.oases.provider.router.RouterPath
import com.oases.ui.adapter.MnemonicBackupAdapter
import com.oases.user.R
import com.oases.user.ui.fragment.EncryptedMnemonicFragment
import com.oases.user.ui.fragment.PrivateKeyMnemonicFragment
import kotlinx.android.synthetic.main.activity_mnemonic_backup.*

@Route(path = RouterPath.UserCenter.PATH_BACKUP_WALLET)
class MnemonicBackupActivity : FragmentActivity(), PrivateKeyMnemonicFragment.OnFragmentInteractionListener , EncryptedMnemonicFragment.OnFragmentInteractionListener {

    private var listener: PrivateKeyMnemonicFragment.OnFragmentInteractionListener? = null
    private var mListener: EncryptedMnemonicFragment.OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mnemonic_backup)
        //initView()
        requestPermission()
    }

    private fun initView(){
        mTabLayout.tabMode = TabLayout.MODE_FIXED
        mViewPager.adapter = MnemonicBackupAdapter(supportFragmentManager)
        //mTabLayout关联mViewPager
        mTabLayout.setupWithViewPager(mViewPager)
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
        mListener?.onFragmentInteraction(uri)
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    //获取读写权限
    private fun requestPermission() {
        val checkWritePermission = ActivityCompat.checkSelfPermission(this@MnemonicBackupActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkWritePermission== PackageManager.PERMISSION_GRANTED) {
            initView()
        } else {
            //requset permission
            ActivityCompat.requestPermissions(this@MnemonicBackupActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                initView()
            } else{
                //permission denied
                Toast.makeText(this@MnemonicBackupActivity,"permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

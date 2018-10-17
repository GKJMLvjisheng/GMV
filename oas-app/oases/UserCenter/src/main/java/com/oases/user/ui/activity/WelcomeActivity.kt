package com.oases.user.ui.activity


import android.content.Intent
import android.os.Bundle
import android.view.View

import com.oases.base.ext.onClick

import com.oases.base.ui.activity.BaseActivity
import com.oases.user.R
import com.oases.user.ui.activity.LoginActivity
import com.oases.user.ui.activity.RegisterActivity
import kotlinx.android.synthetic.main.activity_welcome.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class WelcomeActivity : BaseActivity(), View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        initBtn()
    }

    private fun initBtn() {
        mLoginBtn.onClick(this)
        mRegisterBtn.onClick(this)
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.mRegisterBtn  -> startActivity<RegisterActivity>()
            R.id.mLoginBtn -> startActivityForResult<LoginActivity>(0)
            else -> toast(v.id.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        finish()
    }
}

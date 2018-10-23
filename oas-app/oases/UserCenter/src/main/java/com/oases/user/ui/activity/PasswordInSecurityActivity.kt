package com.oases.user.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.R
import kotlinx.android.synthetic.main.activity_password_in_security.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class PasswordInSecurityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_in_security)
        initView()
    }

    private fun initView(){
        mUserName.text = AppPrefsUtils.getString(BaseConstant.USER_NAME)
        mSetPassword.onClick {
            startActivity<SetPasswordActivity>()
        }
        mBackupWallet.onClick {
            toast("功能待开发，敬请期待！")
            //startActivity<MnemonicBackupActivity>()
        }
    }
}

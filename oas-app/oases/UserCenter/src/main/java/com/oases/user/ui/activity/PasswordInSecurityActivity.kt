package com.oases.user.ui.activity

import android.os.Bundle
import com.oases.base.common.BaseConstant
import com.oases.base.common.BaseConstant.Companion.WALLET_BACKUP
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.R
import com.oases.user.data.protocol.BackupWalletStatusResp
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.PasswordInSecurityPresenter
import com.oases.user.presenter.view.PasswordInSecurityView
import kotlinx.android.synthetic.main.activity_password_in_security.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class PasswordInSecurityActivity : BaseMvpActivity<PasswordInSecurityPresenter>(), PasswordInSecurityView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_in_security)
        initView()
    }

    fun setBackupState(){
        if (AppPrefsUtils.getBoolean(WALLET_BACKUP)){
            mBackupWallet.setValue("已设置")
        }else{
            mBackupWallet.setValue("未设置")
        }
    }
    private fun initView(){
        setBackupState()
        mUserName.text = AppPrefsUtils.getString(BaseConstant.USER_NAME)
        mPresenter.getBackupWalletStatus()
        mSetPassword.onClick {
            startActivity<SetPasswordActivity>()
        }
        mBackupWallet.onClick {
            toast("功能待开发，敬请期待！")
           // startActivity<MnemonicBackupActivity>()
        }
    }

    override fun injectComponent() {
        DaggerUserComponent
                .builder()
                .activityComponent(mActivityComponent)
                .userModule(UserModule())
                .build()
                .inject(this)
        mPresenter.mView = this
    }
    override fun onGetBackupWalletStatusResult(result: BackupWalletStatusResp) {
        /*if (result.backup==0){
            mBackupWallet.setValue("未设置")
        }else{
            mBackupWallet.setValue("已设置")
        }*/
    }

    override fun onResume() {
        super.onResume()
        setBackupState()
    }

}

package com.oases.user.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.oases.base.common.BaseConstant
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.R
import com.oases.user.data.protocol.UserInfo
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.LoginPresenter
import com.oases.user.presenter.NickNamePresenter
import com.oases.user.presenter.view.LoginView
import com.oases.user.presenter.view.NickNameView
import kotlinx.android.synthetic.main.activity_nick_name.*
import org.jetbrains.anko.*

class NickNameActivity : BaseMvpActivity<NickNamePresenter>(), NickNameView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nick_name)
        var bundle = this.intent.extras
        mNickName.setText(bundle.get("NickName").toString())
        initView()
    }

    private fun initView() {
        mHeadBar.onClickRightTv{
            mPresenter.updateNickName(mNickName.text.toString())
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

    override fun onUpdateNickNameResult(result: UserInfo) {
        AppPrefsUtils.putString(BaseConstant.USER_NICK_NAME,mNickName.text.toString())
        toast("保存成功")
        //startActivity<UserInfoActivity>()
        startActivity(intentFor<UserInfoActivity>().singleTop().clearTop())
    }
}

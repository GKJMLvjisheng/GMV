package com.oases.user.ui.activity

import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast
import com.oases.base.common.BaseConstant
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.R
import com.oases.user.data.protocol.UserInfo
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.AddressPresenter
import com.oases.user.presenter.view.AddressView
import kotlinx.android.synthetic.main.activity_address.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast


class AddressActivity : BaseMvpActivity<AddressPresenter>(),AddressView {

    var Address:String =""
    var Address1:String =""
    var Address2:String =""
    lateinit var myWebUser: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        myWebUser = findViewById(R.id.mCountry)
        myWebUser.settings.javaScriptEnabled = true
        myWebUser.webViewClient = WebViewClient()
        val url = "file:///android_asset/selectCountry.html"
        myWebUser.loadUrl(url)

        /*myWebUser.evaluateJavascript("javascript:getContent()", object : ValueCallback<String> {
            override fun onReceiveValue(value:String?){
                //    Toast.makeText(this@AddressActivity, "我是调用js返回的数据：$value", Toast.LENGTH_LONG).show()
                Address=value.toString()
                //去掉地址前后引号
                Address = Address.substring(1,Address.length-1)
                mSelectCountry.setText(value.toString())
            }
        })*/
        initView()

    }

     fun initView() {
        mHeadBar.onClickRightTv {
            myWebUser.evaluateJavascript("javascript:getContent()", object : ValueCallback<String> {
               override fun onReceiveValue(value:String?){
                //    Toast.makeText(this@AddressActivity, "我是调用js返回的数据：$value", Toast.LENGTH_LONG).show()
                   Address=value.toString()
                   //去掉地址前后引号
                   Address = Address.substring(1,Address.length-1)
                   //当为直辖市时，值前后一样，取一个值
                   if (Address.length==5) {
                       Address1 = Address.substring(0, 2)
                       Address2 = Address.substring(3, 5)
                       Log.d("address1", Address1)
                       Log.d("address2", Address2)
                       if (Address1 == Address2) {
                           Address = Address1
                       }
                       mPresenter.updateAddress(Address)
                   }else {
                       Log.d("address", Address)
                       mPresenter.updateAddress(Address)
                   }
                }
            })
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

    override fun onUpdateAddressResult(result: UserInfo) {
        AppPrefsUtils.putString(BaseConstant.USER_ADDRESS,Address)
        toast("保存成功")
        //startActivity<UserInfoActivity>()
        startActivity(intentFor<UserInfoActivity>().singleTop().clearTop())
    }


}

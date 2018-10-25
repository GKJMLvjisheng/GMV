package com.oases.ui.fragment

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat.finishAffinity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder
import com.allenliu.versionchecklib.v2.builder.UIData
import com.oases.R
import com.oases.base.common.BaseConstant
import com.oases.base.ui.fragment.BaseMvpFragment
import com.oases.base.utils.AppPrefsUtils
import com.oases.data.protocol.AppUpdateResp
import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.MainPresenter
import com.oases.presenter.view.MainView
import com.oases.ui.activity.MainActivity
import com.oases.user.ui.activity.PasswordInSecurityActivity
import com.oases.user.ui.activity.UserInfoActivity
import com.oases.user.ui.activity.UserInfoQrCodeActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_me.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast


class MeFragment : BaseMvpFragment<MainPresenter>(), MainView, View.OnClickListener{

    private var serverAddress:String = ""
    private var versionInfo:PackageInfo? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
      //  mIdTv.text = "Hello"
        return inflater.inflate(R.layout.fragment_me, null)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView() {
        mUserNameTv.text = AppPrefsUtils.getString(BaseConstant.USER_NICK_NAME)
        mIdTv.text = AppPrefsUtils.getString(BaseConstant.USER_NAME)
        mInviteNumberTv.text = AppPrefsUtils.getString(BaseConstant.USER_INVITE_CODE)
        serverAddress =BaseConstant.SERVER_ADDRESS+"null"
        if (AppPrefsUtils.getString(BaseConstant.USER_ICON)==serverAddress||AppPrefsUtils.getString(BaseConstant.USER_ICON)==""){
            Log.d("lhlhlhlh",serverAddress)
            mUserIconIv.setImageResource(R.drawable.icon_default_user)
        }else{
            Picasso.get().load(AppPrefsUtils.getString(BaseConstant.USER_ICON))
                    .into(mUserIconIv)
          Log.d("ssss",AppPrefsUtils.getString(BaseConstant.USER_ICON))
        }
        //显示版本
        versionInfo = this.activity?.packageManager?.getPackageInfo(this.activity?.packageName,0)
        mNowVersion.setText("当前版本为V"+"${versionInfo?.versionName}")

        mUserIconIv.setOnClickListener(this)
        mQrCode.setOnClickListener(this)
        mUpdateAppTv.setOnClickListener(this)
        mAddressTv.setOnClickListener(this)
        mShareTv.setOnClickListener(this)
        mSwitchLanguageTv.setOnClickListener(this)
        mHelpTv.setOnClickListener(this)
        mAboutUsTv.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0){
            mUserIconIv -> startActivity<UserInfoActivity>()
            mQrCode -> startActivity<UserInfoQrCodeActivity>()
            mUpdateAppTv -> mPresenter.onGetAppUpdate()
            mAddressTv -> startActivity<PasswordInSecurityActivity>()
            mShareTv -> readyTip()
            mSwitchLanguageTv -> readyTip()
            mHelpTv -> readyTip()
            mAboutUsTv -> readyTip()
        }
    }

    fun readyTip(){
        toast("即将上线")
    }
    override fun onResume() {
        super.onResume()
        initView()
    }

    override fun injectComponent() {
        DaggerMainComponent
                .builder()
                .activityComponent(mActivityComponent)
                .walletModule(WalletModule())
                .build()
                .inject(this)
        mPresenter.mView = this
    }

    override fun onGetAppUpdate(value: AppUpdateResp) {
        try {
            // if(value!=null){
            val versionFromServer:Int = value.versionCode.toInt()
            val addressFromServer:String = value.appUrl
            Log.d("zht44","${versionInfo?.versionCode}")
            //val versionApp = AppPrefsUtils.getString(BaseConstant.APP_VERSION_NAME)//AppPrefsUtils.getString(BaseConstant.APP_VERSION)
            //val packageInfo = BaseApplication.context.packageManager.getPackageInfo(packageName, 0)
            if("${versionInfo?.versionCode}".toInt() < versionFromServer){
                var str = "发现新版本，是否升级？"
                if(value.upGradeStatus == 1){
                    str = "发现新版本，请先升级后使用！"
                }
                val uiData:UIData = UIData.create().setDownloadUrl(addressFromServer).setTitle("新版本提醒").setContent(str)
                AppPrefsUtils.putString(BaseConstant.PACKAGE_URL, addressFromServer)
                val builder: DownloadBuilder = AllenVersionChecker
                        .getInstance()
                        .downloadOnly(uiData)as DownloadBuilder
                //builder.setDownloadAPKPath("/storage/emulated/0/oases_download")
                builder .excuteMission(context)
                builder.setForceRedownload(true) //本地有安装包缓存仍重新下载apk
                if(value.upGradeStatus == 1){
                    builder.setForceUpdateListener {
                        System.exit(0)

                    }
                }
            }else{
                Toast.makeText(context, "当前处于最新版本", Toast.LENGTH_SHORT).show()
            }
        }catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }
    }


}

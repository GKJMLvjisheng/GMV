package com.oases.ui.activity

import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.net.toUri
import com.alibaba.android.arouter.facade.annotation.Route
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.eightbitlab.rxbus.Bus
import com.oases.R
import com.oases.ui.fragment.TransitWalletFragment
import com.oases.base.common.AppManager
import com.oases.base.common.BaseApplication.Companion.context
import com.oases.base.common.BaseConstant.Companion.PACKAGE_URL
import com.oases.base.common.BaseConstant.Companion.USER_TOKEN
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.data.protocol.AppUpdateResp
import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.MainPresenter
import com.oases.presenter.view.MainView
import com.oases.provider.router.RouterPath
import com.oases.ui.fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.util.*
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder
import com.allenliu.versionchecklib.v2.builder.UIData
import com.oases.base.common.BaseConstant
import com.today.step.lib.ISportStepInterface
import com.today.step.lib.TodayStepService
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.startService



@Route(path = RouterPath.App.PATH_MAIN)
class MainActivity : BaseMvpActivity<MainPresenter>(), MainView,
        WalletFragment.OnFragmentInteractionListener,
        TransitWalletFragment.OnFragmentInteractionListener,
        WalletOnLineFragment.OnFragmentInteractionListener,
        MyPointsFragment.OnFragmentInteractionListener{
    private var pressTime:Long = 0
    private val mStack = Stack<Fragment>()
    private val mHomeFragment by lazy {HomeFragment()}
    // private val mHomeFragment by lazy {WebViewFragment()}
    private  val mWalletFragment by lazy { WalletFragment() }
    private val mMeFragment by lazy {MeFragment()}
    private var mPosition:Int = 0

    //循环取当前时刻的步数中间的间隔时间
    private val TIME_INTERVAL_REFRESH: Long = 500
    private val REFRESH_STEP_WHAT = 0
    private lateinit var iSportStepInterface: ISportStepInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragment()
        initBottomNav()
        //todo getLatestVesionInfo
        onGetLatestVersion()//todo add to presenter callback
        changeFragment(0)
    }

    private fun changeFragment(position: Int) {
        val manager = supportFragmentManager.beginTransaction()
        for (fragment in mStack){
            fragment.userVisibleHint = false
            manager.hide(fragment)
        }

        mStack[position].userVisibleHint = true
        mPosition = position
        manager.show(mStack[position])
        manager.commit()
    }

    private fun initBottomNav() {
        mBottomNavBar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener{
            override fun onTabReselected(position: Int) {
            }

            override fun onTabUnselected(position: Int) {
            }

            override fun onTabSelected(position: Int) {
                changeFragment(position)
            }
        })

    }

    private fun initFragment() {
        val manager = supportFragmentManager.beginTransaction()
        manager.add(R.id.mContainer, mHomeFragment)
        manager.add(R.id.mContainer, mWalletFragment)
        manager.add(R.id.mContainer, mMeFragment)
        manager.commit()

        mStack.add(mHomeFragment)
        mStack.add(mWalletFragment)
        mStack.add(mMeFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        Bus.unregister(this)
        Log.d("zbb", "destroy main")
        AppPrefsUtils.putString(USER_TOKEN, "")
    }

    override fun onBackPressed() {
        //home frag should process view back
        if (mPosition ==0 ){
            mHomeFragment.onBackPressed{
                precessBackPressed()
            }
        }
        else {
            precessBackPressed()
        }
    }

    fun precessBackPressed(){
        val time = System.currentTimeMillis()
        if (time - pressTime > 2000) {
            toast("再按一次退出程序")
            pressTime = time
        } else {
            Log.d("zbb", "exit app")
            AppPrefsUtils.putString(USER_TOKEN, "")
            AppManager.instance.exitApp(this)
        }
    }

    private fun startDownload(){
        val intent = Intent()
        intent.setAction(Intent.ACTION_VIEW)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        //replace it with link to app
       // intent.setData("http://auto.163.com/photoview/4GDR0008/200346.html#p=DSCNNE514GDR0008NOS".toUri())
        startActivity(intent)
    }

    private fun onGetLatestVersion(){
       // AppPrefsUtils.putString(PACKAGE_URL, "http://auto.163.com/photoview/4GDR0008/200346.html#p=DSCNNE514GDR0008NOS")
        checkVersion()
    }

    private fun createUpgradeDialog()
    {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
                .setMessage("发现新版本，是否升级？")
                .setTitle("新版本提醒")
                .setPositiveButton("升级", DialogInterface.OnClickListener{
                    _, _ -> toast("to be done")//startDownload()
                })
                .setNegativeButton("取消", null)

        val dialog = dialogBuilder.create() as AlertDialog
        dialog.show()
        dialog.findViewById<TextView>(android.R.id.message)?.gravity = Gravity.CENTER
    }

    private fun checkVersion(){
        mPresenter.onGetAppUpdate()
        /*try {
            val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
            Log.d("zbb", "version is: ${packageInfo.versionCode}")
            //todo add version check
            createUpgradeDialog()
        }catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }*/

    }

    override fun onWalletFragmentInteraction(uri: Uri){
        Log.i("zbb", "receive communication from wallet fragment")
    }

    override fun onWalletOnLineFragmentInteraction(uri: Uri){

    }

    override fun onMyPointsFragmentInteraction(uri:Uri){

    }

    override fun onTransitFragmentInteraction(uri: Uri) {
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
                Log.d("zbb",versionFromServer.toString())
                Log.d("zbb",addressFromServer)
                val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
                Log.d("zbb", "${packageInfo.versionCode}")
                AppPrefsUtils.putString(BaseConstant.PACKAGE_URL,addressFromServer)
                //AppPrefsUtils.putString(BaseConstant.APP_VERSION,"${packageInfo.versionCode}")
                //AppPrefsUtils.putString(BaseConstant.APP_VERSION_NAME,"${packageInfo.versionName}")

                //todo add version check
               // val url:String = "http://18.219.19.160:8080/image/news/App-release.apk"
                if(packageInfo.versionCode.toInt() < versionFromServer){

                    val uiData:UIData = UIData.create().setDownloadUrl(addressFromServer).setTitle("新版本提醒").setContent("发现新版本，是否升级？")
                    val builder:DownloadBuilder = AllenVersionChecker
                            .getInstance()
                            .downloadOnly(uiData)as DownloadBuilder
                    //builder.setDownloadAPKPath("/storage/emulated/0/oases_download")
                    builder .excuteMission(this)
                    builder.setForceRedownload(true) //本地有安装包缓存仍重新下载apk
                    if(value.upGradeStatus == 1){
                        builder.setForceUpdateListener { }
                    }
                }
           // }

        }catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }
    }
}

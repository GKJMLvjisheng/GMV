package com.oases.ui.fragment

import android.annotation.TargetApi
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.*
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.oases.R
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ext.setVisible
import com.oases.base.ui.fragment.BaseFragment
import com.oases.base.utils.AppPrefsUtils
import com.oases.computingpower.ui.activity.ComputingPowerMainActivity
import com.oases.ui.activity.StartActivity
import com.today.step.lib.ISportStepInterface
import com.today.step.lib.TodayStepService
import org.jetbrains.anko.support.v4.startActivity
import android.os.IBinder
import com.oases.base.ui.fragment.BaseMvpFragment
import com.oases.data.protocol.WalkPoint.InquireWalkPointResp
import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.HomePresenter
import com.oases.presenter.view.HomeView
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.startService
import org.jetbrains.anko.support.v4.toast
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BaseMvpFragment<HomePresenter>(), HomeView {
    lateinit var myWebHome: WebView
    lateinit var mHeadBar: Toolbar
    val mHost: String = BaseConstant.SERVER_ADDRESS//"18.219.19.160:8080"
    val homeUrl: String = "$mHost/api/v1/greenMap"//"http://$mHost/api/v1/greenMap"//
    //val homeUrl:String = "http://10.0.0.25:8080/api/v1/greenMap"
    val mHandler = Handler()

    var mTodaySteps:Int = 0
    //循环取当前时刻的步数中间的间隔时间
    private val TIME_INTERVAL_REFRESH: Long = 500
    private val HISTORY_DAYS = 7
    private val REFRESH_STEP_WHAT = 0
    private var iSportStepInterface: ISportStepInterface? = null

    private val mDelayHandler = Handler(TodayStepCounterCall())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val homeFragment = inflater.inflate(R.layout.fragment_home, null)
        myWebHome = homeFragment.findViewById(R.id.mWebHome)
        //myWebHome.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        Log.d("zbb1", "view gpu")
        //myWebHome.loadUrl("http://www.163.com")
        myWebHome.settings?.javaScriptEnabled = true
        myWebHome.settings?.databaseEnabled = true
        myWebHome.settings?.domStorageEnabled = true
        myWebHome.settings?.allowUniversalAccessFromFileURLs = true
        myWebHome.settings?.allowContentAccess = true
        myWebHome.settings?.blockNetworkImage = true
        myWebHome.settings?.cacheMode = WebSettings.LOAD_DEFAULT

        myWebHome.addJavascriptInterface(JsInterface(this, mHandler), "Android")
        initView()
        initHeadBar(homeFragment)
        createTodayStepService()
       // getHistorySteps()

        return homeFragment
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

    fun getHistorySteps(){

        iSportStepInterface?.getTodaySportStepArrayByStartDateAndDays(getDate(), HISTORY_DAYS)

    }

    fun getDate():String{
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -HISTORY_DAYS)
        return SimpleDateFormat("yyyy-MM-dd").format(cal.time)
    }

    fun createTodayStepService(){
        var intent = Intent()
        intent.setClass(activity, TodayStepService::class.java)
        activity?.startService(intent)
        activity?.bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                //Activity和Service通过aidl进行通信
                iSportStepInterface = ISportStepInterface.Stub.asInterface(service)
                try {
                    mTodaySteps = iSportStepInterface!!.getCurrentTimeSportStep()
                    Log.d("zbb", iSportStepInterface?.getTodaySportStepArrayByStartDateAndDays(getDate(), HISTORY_DAYS).toString())
                    mSteps.text = mTodaySteps.toString()
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }

                mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH)
            }

            override fun onServiceDisconnected(name: ComponentName) {
            }
        }, Context.BIND_AUTO_CREATE)
    }

    override fun onInquireWalkPoint(value: InquireWalkPointResp)
    {}

    internal inner class TodayStepCounterCall : Handler.Callback {

        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                REFRESH_STEP_WHAT -> {
                    //每隔500毫秒获取一次计步数据刷新UI
                    if (null != iSportStepInterface) {
                        var step = 0
                        try {
                            step = iSportStepInterface!!.currentTimeSportStep
                        } catch (e: RemoteException) {
                            e.printStackTrace()
                        }
                        mTodaySteps = step
                        mSteps.text = mTodaySteps.toString()
                    }
                    mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH)
                }
            }
            return false
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("zbb1","homeFragment on resume")
        //initView()
        myWebHome.loadUrl("javascript:skipRefresh()")
    }

    fun shouldToolBarShowBack(){
        if (myWebHome.canGoBack()) {
            mHeadBar.setVisible(true)
        }
        else{
            mHeadBar.setVisible(false)
        }
    }
    private fun initView(){
        val token = AppPrefsUtils.getString(BaseConstant.USER_TOKEN)
        val urlHead = mapOf<String, String>("token" to token)

        myWebHome.loadUrl(homeUrl, urlHead)
        //myWebHome.loadUrl("file:///android_asset/form4.html")
        // myWebHome.loadUrl("file:///android_asset/index.html")
        // myWebHome.loadUrl("file:///android_asset/test.html")
        myWebHome.webViewClient = MyWebViewClient(homeUrl)
        myWebHome.webChromeClient = WebChromeClient()
    }
    fun initHeadBar(homeFragment: View){
        mHeadBar = homeFragment.findViewById(R.id.mHeadBar) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(mHeadBar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        mHeadBar.setNavigationOnClickListener {
            if (myWebHome.canGoBack()) {
                myWebHome.goBack()
            }

        }
    }
    fun onBackPressed(superBackPressed:()->Unit) {
        if (myWebHome.canGoBack()) {
            myWebHome.goBack()
        } else {
            superBackPressed()
        }
    }

    inner class JsInterface(val context: Fragment, val mHandler: Handler){

        @JavascriptInterface
        fun getToken():String {
            Log.d("zbb", "get token called")
            mHandler.post{
                shouldToolBarShowBack()
            }
            return AppPrefsUtils.getString(BaseConstant.USER_TOKEN)
        }

        @JavascriptInterface
        fun startLiftComputingPower(){
            mHandler.post{
                try {
                    context.startActivity<ComputingPowerMainActivity>()
                } catch (e: Exception) {
                  //  Toast.makeText(this, "对不起，跳转页面出现异常", Toast.LENGTH_SHORT).show()
                }
            }
        }

        @JavascriptInterface
        fun getTodaySteps():Int{
            return mTodaySteps
        }
    }

    inner class MyWebViewClient(val mHomeUrl: String): WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            view.settings.blockNetworkImage = false
            view.settings.loadsImagesAutomatically = true
        }

        @Suppress("deprecation")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            val uri = Uri.parse(url)
            return handleUri(uri)
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val uri = request?.url
            uri?:return false
            return handleUri(uri)
        }

        private fun handleUri(uri: Uri): Boolean{
            val host = uri.host
            Log.i("zbb", "Uri $uri host $host")


            if(mHost.contains(host)){
                mHeadBar.setVisible(false)
            }
            else {
                mHeadBar.setVisible(true)
            }
            return false
        }
    }


}

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
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.oases.base.ext.fromJson
import com.oases.base.ui.fragment.BaseMvpFragment
import com.oases.base.utils.DateUtils.DATE_FORMAT
import com.oases.base.utils.DateUtils.getDaysBetweenDates
import com.oases.base.utils.DateUtils.getNow
import com.oases.data.protocol.WalkPoint.InquireWalkPointReq
import com.oases.data.protocol.WalkPoint.InquireWalkPointResp
import com.oases.data.protocol.WalkPoint.StepItem
import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.HomePresenter
import com.oases.presenter.view.HomeView
import com.today.step.lib.TodayStepData
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

    fun getDateByOffset(offset: Int):String{
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, offset)
        return SimpleDateFormat("yyyy-MM-dd").format(cal.time)
    }

    private fun transformToReq(steps: String): InquireWalkPointReq {
        val jsonParser = JsonParser()
        val stepsArray = jsonParser.parse(steps).asJsonArray
        val gson = Gson()

        val stepList: MutableList<StepItem> = ArrayList()

        for (stepJson in stepsArray){
            val item = gson.fromJson<StepItem>(stepJson)
            stepList.add(item)
        }
        return InquireWalkPointReq(stepList)
    }

    private fun uploadHistorySteps() {
        val lastUpdateDate = getStepUploadDate()
        val daysGap = getDaysBetweenDates(lastUpdateDate, getNow(DATE_FORMAT))

        if (daysGap >= 1) {
            val steps = iSportStepInterface!!.getTodaySportStepArrayByStartDateAndDays(lastUpdateDate, daysGap)
            if (steps == "[]") {
                Log.d("zbb", "uploadHistorySteps: no steps $steps")
            }
            else {
                Log.d("zbb", "update history steps $steps")
                mPresenter.inquireWalkPoint(transformToReq(steps))
            }
            updateStepUploadDate()
        }
    }

    private fun createTodayStepService(){
        var intent = Intent()
        intent.setClass(activity, TodayStepService::class.java)
        activity?.startService(intent)
        activity?.bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                //Activity和Service通过aidl进行通信
                iSportStepInterface = ISportStepInterface.Stub.asInterface(service)
                try {
                    mTodaySteps = iSportStepInterface!!.getCurrentTimeSportStep()

                        uploadHistorySteps()
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }

                mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH)
            }

            override fun onServiceDisconnected(name: ComponentName) {
            }
        }, Context.BIND_AUTO_CREATE)
    }

    private fun updateStepUploadDate(){
        return AppPrefsUtils.putString(BaseConstant.LAST_STEP_UPLOAD_DATE, getNow(DATE_FORMAT))
    }

    fun getStepUploadDate(): String {
        return if (AppPrefsUtils.getString(BaseConstant.LAST_STEP_UPLOAD_DATE) == "")
            getNow(DATE_FORMAT)
        else
            AppPrefsUtils.getString(BaseConstant.LAST_STEP_UPLOAD_DATE)
    }


    override fun onInquireWalkPoint(value: InquireWalkPointResp)
    {}

    internal inner class TodayStepCounterCall : Handler.Callback {

        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                REFRESH_STEP_WHAT -> {
                    //每隔500毫秒获取一次计步数据刷新UI
                    if (null != iSportStepInterface) {
                        try {
                            mTodaySteps = iSportStepInterface!!.currentTimeSportStep
                        } catch (e: RemoteException) {
                            e.printStackTrace()
                        }
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

          //  view.loadUrl("javascript:window.Android.checkCode" +
          //          "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
      //  }
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

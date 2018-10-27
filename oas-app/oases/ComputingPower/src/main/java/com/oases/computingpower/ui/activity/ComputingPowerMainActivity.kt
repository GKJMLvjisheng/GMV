package com.oases.computingpower.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.view.get
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.oases.base.ui.activity.BaseActivity
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.computingpower.R
import com.oases.computingpower.adapter.FunctionAdapter
import com.oases.computingpower.data.protocol.FunctionItem
import com.oases.computingpower.data.protocol.MailStatusResp
import com.oases.computingpower.data.protocol.PhoneStatusResp
import com.oases.computingpower.data.protocol.PowerActivityStatusResp
import com.oases.computingpower.injection.component.DaggerComputingPowerComponent
import com.oases.computingpower.injection.module.ComputingPowerModule
import com.oases.computingpower.presenter.ComputingPowerMainPresenter
import com.oases.computingpower.presenter.view.ComputingPowerMainView
import com.oases.computingpower.utils.SpaceItemDecoration
import com.oases.provider.router.RouterPath
import com.zhy.view.flowlayout.TagFlowLayout.dip2px
import kotlinx.android.synthetic.main.activity_computing_power_main.*
import kotlinx.android.synthetic.main.grid_item.*
import kotlinx.android.synthetic.main.grid_item.view.*
import org.jetbrains.anko.*
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView

@Route(path = "/computingPower/ComputingPowerMainActivity")
class ComputingPowerMainActivity :
        BaseMvpActivity<ComputingPowerMainPresenter>(),
        ComputingPowerMainView,
        View.OnClickListener{

    private val phoneJumpFlag: String = "fromComputingPower"
    private val mailJumpFlag: String = "fromComputingPower"
    private val KYCJumpFlag: String = "fromComputingPower"
    private var originalPhoneNumber: String = ""
    private var originalMail: String = ""
    private var allData = mutableListOf(
        FunctionItem(mBackupWallet,false,"wallet_fill"),
        FunctionItem(mWatchWeChat,false,"wechat"),
        FunctionItem(mInviteFriends,false,"shape"),
        FunctionItem(mEnvironmentalmProtectionAnswer,false,"read"),
        FunctionItem(mConnectToPhone,false,"mobile"),
        FunctionItem(mConnectToMail,false,"mail"),
        FunctionItem(mBuyMiner,false,"sketch_fill"),
        FunctionItem(mKYC,false,"protect_fill"),
        FunctionItem(mModuleConstruction,false,"home")
    )
    private lateinit var functionAdapter:FunctionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_computing_power_main)
        initView()
        mHead.onClickRightTv {
            startActivity(intentFor<ComputingPowerHistoryActivity>().singleTop().clearTop())
        }

    }

    override fun injectComponent() {
        DaggerComputingPowerComponent
                .builder()
                .activityComponent(mActivityComponent)
                .computingPowerModule(ComputingPowerModule())
                .build()
                .inject(this)
        Log.d("zbb", "MyPoints injected")
        mPresenter.mView = this
    }

    fun getActivityWidth(context: Context): Int {
        try {
            val mDm = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay
                    .getMetrics(mDm)
            return mDm.widthPixels
        } catch (e: Exception) {
            return 0
        }

    }

    override fun onGetPower(power: Int) {
        mCurrentComputingPower.text = power.toString()
    }

    private fun initView() {
        //当前算力值
        mPresenter.inquireCurrentPeriodPoints()

        //活动状态
        mPresenter.inquirePowerActivityStatus()
        val gridManager = GridLayoutManager(this, 3)
       /* gridManager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        })*/

        functionAdapter = FunctionAdapter(this, allData, this)
        mRecycleView.setLayoutManager(gridManager)
        mRecycleView.setAdapter(functionAdapter)

        val spaceDecoration = SpaceItemDecoration(3, dip2px(this, 10f))
        mRecycleView.addItemDecoration(spaceDecoration)
       // functionAdapter.notifyDataSetChanged()
        // allData.set(1, FunctionItem("关注公众号√",false,"wallet_fill"))
        //functionAdapter.update(allData)
       // QBadgeView(this).bindTarget(mRecycleView).setBadgeText("√").setBadgeGravity(Gravity.START or Gravity.CENTER)
    }

    override fun onClick(v: View?) {
        var name = v?.mName?.text?:""
        when (name){
            mBackupWallet ->ARouter.getInstance().build(RouterPath.UserCenter.PATH_BACKUP_WALLET).navigation()
            //mBackupWallet->toast("即将上线")
            mWatchWeChat -> startActivity<WatchingWeChatPublicAccountActivity>()
            mInviteFriends ->startActivity<InviteFriendsActivity>()
            mEnvironmentalmProtectionAnswer->toast("即将上线")
            /*mConnectToPhone -> {
                originalPhoneNumber = "未设置"
                ARouter.getInstance().build("/userCenter/PhoneStepOneActivity")
                        .withString("originalPhoneNumber", originalPhoneNumber)
                        .withString("phoneJumpFlag", phoneJumpFlag).navigation()
            }*/
            mConnectToPhone -> mPresenter.checkPhone()
            mConnectToMail -> mPresenter.checkMail()
            mBuyMiner -> startActivity<BuyingMinerActivity>()
           /* mConnectToMail ->{
                originalMail = "未设置"
                ARouter.getInstance().build("/userCenter/MailStepOneActivity")
                        .withString("originalMail", originalMail)
                        .withString("mailJumpFlag", mailJumpFlag).navigation()
            }*/
            //mKYC ->startActivity<KYCActivity>()
            mKYC ->intentKYCActivity()
            mModuleConstruction ->toast("即将上线")
        else -> toast("即将上线")
        }
    }

    //携带KYC页面跳转标志进行跳转
    private fun intentKYCActivity (){
        var intent = Intent(this@ComputingPowerMainActivity,KYCActivity::class.java)
        var bundle = Bundle()
        bundle.putString("KYCJumpFlag",KYCJumpFlag)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    companion object {
        val  mBackupWallet: String = "备份钱包"
        val  mWatchWeChat: String = "关注公众号"
        val  mInviteFriends: String = "好友分享"
        val  mEnvironmentalmProtectionAnswer: String = "环保答题"
        val  mConnectToPhone: String = "关联手机号"
        val  mConnectToMail: String = "关联邮箱"
        val  mBuyMiner: String = "购买矿机"
        val  mKYC: String = "KYC认证"
        val  mModuleConstruction: String = "模块建设中"

    }

    override fun onCheckPhoneResult(result: PhoneStatusResp) {
        if (result.mobile=="empty"){
            //toast("手机号已存在，不能进行关联")
            originalPhoneNumber = "未设置"
            ARouter.getInstance().build("/userCenter/PhoneStepOneActivity")
                    .withString("originalPhoneNumber", originalPhoneNumber)
                    .withString("phoneJumpFlag", phoneJumpFlag).navigation()
        }else{
            originalPhoneNumber = result.mobile
            ARouter.getInstance().build("/userCenter/PhoneStepOneActivity")
                    .withString("originalPhoneNumber", originalPhoneNumber)
                    .withString("phoneJumpFlag", phoneJumpFlag).navigation()
        }
    }

    override fun onCheckMailResult(result: MailStatusResp) {
        if (result.email=="empty"){
            //toast("邮箱已存在，不能进行关联")
            originalMail = "未设置"
            ARouter.getInstance().build("/userCenter/MailStepOneActivity")
                    .withString("originalMail", originalMail)
                    .withString("mailJumpFlag", mailJumpFlag).navigation()
        }else{
            originalMail = result.email
            ARouter.getInstance().build("/userCenter/MailStepOneActivity")
                    .withString("originalMail", originalMail)
                   .withString("mailJumpFlag", mailJumpFlag).navigation()
        }
    }

    /*override fun onInquirePowerActivityStatusResult(result: PowerActivityStatusResp) {
     var list=result.activityResultList
        for(item in list) {
            if (item.sourceCode >1 && item.sourceCode < 8) {
                allData.set(item.sourceCode - 2, FunctionItem(martchResult(item.sourceCode), false, imageResult(item.sourceCode)))
            }
        }
        functionAdapter.update(allData)
    }

    private fun martchResult(type:Int):String{
        return when(type){
            2->"备份钱包√"
            3->"关注公众号√"
            4->"好友分享√"
            5->"环保答题√"
            6->"关联手机号√"
            7->"关联邮箱√"
            else ->""
        }
    }

    private fun imageResult(type:Int):String{
        return when(type){
            2->"wallet"
            3->"wechat_fill"
            4->"shape_fill"
            5->"read_fill"
            6->"mobile_fill"
            7->"mail_fill"
            else ->""
        }
    }*/

    /*override fun onInquirePowerActivityStatusResult(result: PowerActivityStatusResp) {
     var list=result.activityResultList
        for(item in list) {
            if (item.sourceCode >1 && item.sourceCode < 8) {
                ViewHolder(mRecycleView)
                //    allData.set(item.sourceCode - 2, FunctionItem(martchResult(item.sourceCode), false, imageResult(item.sourceCode)))
            }
        }
    }*/

  /*  internal inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var badge: Badge
        init {
            badge = QBadgeView(this@ComputingPowerMainActivity).bindTarget(itemView.findViewById(R.id.layout))
            badge.setBadgeText("√").badgeGravity = Gravity.END or Gravity.TOP
        }
    }*/

    override fun onInquirePowerActivityStatusResult(result: PowerActivityStatusResp) {
        var list=result.activityResultList
        var selectList:MutableList<Int> = mutableListOf()
        for(item in list) {
            if (item.sourceCode >2 && item.sourceCode < 12) {
                if (!selectList.contains(item.sourceCode-3)) {
                    selectList.add(item.sourceCode - 3)
                }
            }
        }
        functionAdapter.setIndex(selectList)
        functionAdapter.notifyDataSetChanged()
    }

    //跨模块跳转后，删除Activity栈，并形成新栈，即刷新
    // （manifests中配置singleTask不能形成新的栈,跳回到Activity后执行onNewIntent函数）
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        finish()
        startActivity<ComputingPowerMainActivity>()
    }


    override fun onResume() {
        super.onResume()
        //finish()
        //startActivity<ComputingPowerMainActivity>()
        mPresenter.inquireCurrentPeriodPoints()
        //mPresenter.inquirePowerActivityStatus()
    }
}

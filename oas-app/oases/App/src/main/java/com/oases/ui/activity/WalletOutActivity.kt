package com.oases.ui.activity

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_wallet_out.*
import com.oases.R
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.utils.AppPrefsUtils
import com.yzq.zxinglibrary.android.CaptureActivity
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import com.yzq.zxinglibrary.bean.ZxingConfig
import com.yzq.zxinglibrary.common.Constant
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.WalletOutPresenter
import com.oases.presenter.view.WalletOutView
import com.oases.ui.type.ToolUtil
import com.tbruyelle.rxpermissions.RxPermissions
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast
import java.lang.Integer.parseInt
import java.math.BigDecimal

@Route(path = "/app/WalletOutActivity")
class WalletOutActivity : BaseMvpActivity<WalletOutPresenter>(), WalletOutView {

    val REQUEST_CODE_SCAN:Int = 1
    var permission:RxPermissions ?= null
    var toReceive: String = ""
    var money: String = ""
    var rank: String = ""
    var KYCVerifyStatus: String = ""
    var KYCVerifyInfo: String = ""
    var totalBalance:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_out)
        var bundle = this.intent.extras
        KYCVerifyStatus=bundle.get("KYCVerifyStatus").toString()
        KYCVerifyInfo=bundle.get("KYCVerifyInfo").toString()
        Log.d("KYC",KYCVerifyStatus)
        permission = RxPermissions(this)
        mWalletOutSend.setRightTopText(AppPrefsUtils.getString(BaseConstant.USER_NAME))
        totalBalance=bundle.get(BaseConstant.MY_OAS_AMOUNT).toString()

        qrImg.onClick {
            useCamera()
        }
       /* mReceiveAccount.onClick {
            useCamera()
        }*/
        mMoney.addTextChangedListener(object: TextWatcher {
            var beforeText:String = ""
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                beforeText = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val inputStr = p0.toString()
                if(!inputStr.isNullOrBlank()){
                    if(!ToolUtil.regularExpressionValidate(inputStr,BaseConstant.NUMBER_POINT_FOUR)){
                        val diff = inputStr.length - beforeText.length
                        mMoney.setText(beforeText)
                        mMoney.setSelection(inputStr.length-diff) //删除字符重写光标位置
                    }
                }

            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
        mRank.addTextChangedListener(object :TextWatcher{
            var beforeRankText:String = ""
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                beforeRankText = p0.toString()
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val inputString = p0.toString()
                if(!inputString.isNullOrBlank()){
                    if(inputString.length > 100){
                        val diff = inputString.length - beforeRankText.length
                        mRank.setText(beforeRankText)
                        mRank.setSelection(inputString.length-diff) //删除字符重写光标位置
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        mWalletOutSureBtn.onClick {
            toReceive = mReceiveAccount.text.toString()
            money = mMoney.text.toString()
            rank = mRank.text.toString()
            if (TextUtils.isEmpty(toReceive) || toReceive.equals("请输入接收方账号")) {
                Toast.makeText(this, "接收方不能为空", Toast.LENGTH_SHORT).show()
                return@onClick
            }
            if (TextUtils.isEmpty(money)) {
                Toast.makeText(this, "金额不能为空", Toast.LENGTH_SHORT).show()
                return@onClick
            }
            if(money.toBigDecimal().compareTo(totalBalance.toBigDecimal())>0){
                Toast.makeText(this, "转出金额超过拥有的代币", Toast.LENGTH_SHORT).show()
                return@onClick
            }
            if (rank.length > 100) {
                Toast.makeText(this, "备注最多不超过100字符", Toast.LENGTH_SHORT).show()
                return@onClick
            }
            //lh修改KYC认证信息
             //mPresenter.walletOutEvent(toReceive,money,rank)  //进行转账
            //根据用户的KYC状态进行进行不同的操作，未审核有限制    0未认证，1是审核中，2是已通过，3是未通过
            if (KYCVerifyStatus == "2") {
                mPresenter.walletOutEvent(toReceive, money, rank)  //进行转账
            } else if (KYCVerifyStatus=="0") {
                var everyTimeLimitMoney = BigDecimal(100)
                if (money.toBigDecimal() <= everyTimeLimitMoney) {      //此处设置次限额  前短固定死
                    //获取用户的日总额
                    mPresenter.getDayMoneyTotal()
                }else {
                    toast("您已超过每次转账的最大额度"+everyTimeLimitMoney.toInt())
                    //弹窗让用户进行KYC认证
                    alertView()
                }
            }else if (KYCVerifyStatus=="1") {
                var everyTimeLimitMoney = BigDecimal(100)
                if (money.toBigDecimal() <= everyTimeLimitMoney) {      //此处设置次限额  前短固定死
                    //获取用户的日总额
                    mPresenter.getDayMoneyTotal()
                }else {
                    toast("您已超过每次转账的最大额度"+everyTimeLimitMoney.toInt()+",且您的KYC认证正在审核中,审核通过后无限制")
                }
            }else if (KYCVerifyStatus=="3") {
                var everyTimeLimitMoney = BigDecimal(100)
                if (money.toBigDecimal() <= everyTimeLimitMoney) {      //此处设置次限额  前短固定死
                    //获取用户的日总额
                    mPresenter.getDayMoneyTotal()
                }else {
                    toast("您已超过每次转账的最大额度"+everyTimeLimitMoney.toInt()+",您的KYC认证审核未通过："+KYCVerifyInfo)
                    //弹窗让用户进行KYC认证
                    alertView()
                }
            }

            /*if (KYCVerifyStatus == "2") {
                mPresenter.walletOutEvent(toReceive, money, rank)  //进行转账
            } else {
                var everyTimeLimitMoney = "100"
                if (money <= everyTimeLimitMoney) {      //此处设置次限额  前短固定死
                    //获取用户的日总额
                     mPresenter.getDayMoneyTotal()
                } else {
                    if (KYCVerifyStatus=="0"){
                        toast("您已超过每次转账的最大额度"+everyTimeLimitMoney)
                        //弹窗让用户进行KYC认证
                        alertView()
                    }
                    if (KYCVerifyStatus=="1"){
                        toast("您已超过每次转账的最大额度"+everyTimeLimitMoney+",您的KYC认证正在审核中，暂不能转账")
                    }
                    if (KYCVerifyStatus=="3"){
                        toast("您已超过每次转账的最大额度"+everyTimeLimitMoney+",您的KYC认证审核未通过："+KYCVerifyInfo)
                        //弹窗让用户进行KYC认证
                        alertView()
                    }
                }
            }*/
        }

        walletOutHead!!.onClickRightTv({
            //startActivity<WalletInActivity>()
            startActivity(intentFor<WalletInActivity>().singleTop().clearTop())
        })

    }
     fun useCamera(){
        permission!!.request(Manifest.permission.CAMERA).subscribe({ granted ->
            if(granted){
                val intent = Intent(this@WalletOutActivity, CaptureActivity::class.java)
                val config: ZxingConfig = ZxingConfig()
                config.setShowbottomLayout(true)//底部布局（包括闪光灯和相册）
                config.setPlayBeep(true)//是否播放提示音
                config.setShake(true)//是否震动
                config.setShowAlbum(true)//是否显示相册
                config.setShowFlashLight(true)//是否显示闪光灯
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config)
                startActivityForResult(intent, REQUEST_CODE_SCAN)
            }else{
                Toast.makeText(this, "你拒绝了权限申请，无法打开相机扫码哟！", Toast.LENGTH_SHORT).show()
            }
        })
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

    //弹框，提醒用户去KYC认证
    private fun alertView(){
        var dialogBuilder = AlertDialog.Builder(this@WalletOutActivity)

        dialogBuilder?.setMessage("进行提现、转账操作时需要通过KYC认证审核")?.setPositiveButton("前往认证", DialogInterface.OnClickListener{
            dialog, id ->
            ARouter.getInstance().build("/computingPower/KYCActivity").navigation()
        })?.setNegativeButton("取消", null)

        val dialog = dialogBuilder?.create() as AlertDialog
        dialog.show()
        //dialog.findViewById<TextView>(android.R.id.message)?.gravity = Gravity.CENTER
        // dialog.findViewById<TextView>(android.R.id.button1)?.gravity = Gravity.CENTER
        // dialog.findViewById<Button>(AlertDialog.BUTTON_NEGATIVE)?.gravity = Gravity.LEFT
        // dialog.findViewById<Button>(AlertDialog.BUTTON_POSITIVE)?.gravity = Gravity.RIGHT
        dialog.run {
        //    getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.authentication)
         //   getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.authentication)
         //   getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.common_bg))
        //    getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.common_bg))
        }
    }

    //转账结果
   override fun onGetWalletOutEvent(value:Int){
       var text = "转出失败！"
       if(value==1){
           text ="转出成功！"
           Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
           startActivity(intentFor<MainActivity>().singleTop().clearTop())
           finish()
       }else{
           Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
       }

   }

    //获取日总额结果
    override fun onGetDayMoneyTotal(result: BigDecimal) {
        var totalMoney = BigDecimal(money)+BigDecimal(result.toString())
        var dayLimitMoney = BigDecimal(200)  //此处设置日限额
        if (totalMoney<=dayLimitMoney){
            mPresenter.walletOutEvent(toReceive, money, rank)  //进行转账
             }
        else{
            if (KYCVerifyStatus=="0"){
            toast("您已超过每日转账的最大额度"+dayLimitMoney.toInt())
            //弹窗让用户进行KYC认证
            alertView()
            }
            if (KYCVerifyStatus=="1"){
                toast("您已超过每日转账的最大额度"+dayLimitMoney.toInt()+",您的KYC认证正在审核中,审核通过后无限制")

            }
            if (KYCVerifyStatus=="3"){
                toast("您已超过每日转账的最大额度"+dayLimitMoney.toInt()+",您的KYC认证审核未通过："+KYCVerifyInfo)
                //弹窗让用户进行KYC认证
                alertView()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                val content = data.getStringExtra(Constant.CODED_CONTENT)
                mReceiveAccount.setText("$content")
            }
        }
    }

}

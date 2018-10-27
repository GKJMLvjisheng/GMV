package com.oases.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.oases.R
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.data.protocol.TransferOasResp
import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.ExchangeOutPresenter
import com.oases.presenter.view.ExchangeOutView
import com.oases.ui.type.ToolUtil
import com.tbruyelle.rxpermissions.RxPermissions
import com.yzq.zxinglibrary.android.BeepManager
import com.yzq.zxinglibrary.android.CaptureActivity
import com.yzq.zxinglibrary.bean.ZxingConfig
import com.yzq.zxinglibrary.common.Constant
import kotlinx.android.synthetic.main.activity_wallet_exchange_out.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import java.math.BigDecimal
import java.util.regex.Matcher
import java.util.regex.Pattern


@Route(path = "/app/ExchangeOutActivity")
class ExchangeOutActivity : BaseMvpActivity<ExchangeOutPresenter>(), ExchangeOutView {
    val REQUEST_CODE_SCAN:Int = 2
    var permission: RxPermissions?= null
    var high:Int = 0
    var low:Int = 0
    var middle:Int = 0
    var width:Int = 0
    var step:Float = 0.toFloat()
    lateinit var text:TextView
    var address:String = AppPrefsUtils.getString(BaseConstant.MY_OAS_ADDRESS)
    //var tMoney:BigDecimal = 0.toBigDecimal()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_exchange_out)
        //OAS积分
        var balance:String = getIntent().getStringExtra("balance")
        var unconfirmed:String = getIntent().getStringExtra("unconfirmed")
        tipLeft.setHint((BaseConstant.GAS_PRICE_LOW.toString()).plus(" GWEI"))
        tipRight.setHint(BaseConstant.GAS_PRICE_HIGH.toString().plus(" GWEI"))
        //mFactorTv.text = balance
        mAvailableAmount.text = (balance.toBigDecimal().subtract(unconfirmed.toBigDecimal())).setScale(4,BigDecimal.ROUND_HALF_UP).toString()
        canUseMoney.text = getIntent().getStringExtra("eth")

        permission = RxPermissions(this)
        //接收方地址
        var addressAfterChange:String = ""
        if(address.length == 42){
            var address_1:String = address.substring(0,16)
            var address_2:String = address.substring(21,37)
            addressAfterChange = ToolUtil.modifyAddressInfo(address_1).plus(address.substring(16,21)).plus("\n")
                    .plus(ToolUtil.modifyAddressInfo(address_2)).plus(address.substring(37,42))
        }
        mMyAddress.setRightTopText(addressAfterChange)

        high= tipRight.hint.toString().replace("GWEI","").trim().toInt()
        low= tipLeft.hint.toString().replace("GWEI","").trim().toInt()
        middle = (high-low+1)/2
        mSeakBar.max = BaseConstant.GAS_PRICE_HIGH -1
        //mSeakBar.min = BaseConstant.GAS_PRICE_LOW
        width= 210
        Log.i("zbb",width.toString())
        step = width.toFloat()/(high-low+1).toFloat()
        text = tipTop
        text.setTextSize("10".toFloat())
        text.setPadding(0,10,changeToBg(width-(middle-1)*step).toInt(),0)
        text.text = middle.toString()
        mSeakBar.setProgress(middle-1)

        qrImg.onClick {
            useCamera()
        }
       /* mReceiveAccount.onClick {
            useCamera()
        }*/
        //金额监听事件
        mMoney.addTextChangedListener(object:TextWatcher{
            var beforeText:String = ""
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                beforeText = p0.toString().trim()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               val inputStr = p0.toString().trim()
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
        mRemark.addTextChangedListener(object :TextWatcher{
            var beforeRankText:String = ""
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                beforeRankText = p0.toString()
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val inputString = p0.toString()
                if(!inputString.isNullOrBlank()){
                    if(inputString.length > 100){
                        val diff = inputString.length - beforeRankText.length
                        mRemark.setText(beforeRankText)
                        mRemark.setSelection(inputString.length-diff) //删除字符重写光标位置
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        mchangeOutHead.onClickRightTv {
            startActivity(intentFor<ExchangeInActivity>().singleTop().clearTop())
        }
        mExchangeOutBtn.onClick {
            val receiveAccount:String = mReceiveAccount.text.toString().replace("\n","").replace(" ","")
            val receiveMoney:String = mMoney.text.toString().replace(" ","")
            val receiveComment:String = mRemark.text.toString()
            Log.d("zzz",receiveAccount)
            Log.d("zzz",receiveAccount.startsWith("Ox").toString())
            if(receiveAccount.length!=42 || !ToolUtil.regularExpressionValidate(receiveAccount,BaseConstant.ENGLISH_NUMBER)){
                toast("接收地址格式有误！")
                return@onClick
            }
           if(address == receiveAccount){
                toast("发送地址和接收地址一致！")
                return@onClick
            }
            if(receiveMoney.isEmpty() || !ToolUtil.regularExpressionValidate(receiveMoney,BaseConstant.NUMBER_POINT_FOUR)){
                toast("转账金额格式有误！")
                return@onClick
            }
            if(canUseMoney.text.toString().toBigDecimal().compareTo(mFactorTv.text.toString().toBigDecimal()) == -1){
                toast("ETH余额不足！")
                return@onClick
            }
            //tMoney = balance.toBigDecimal()-receiveMoney.toBigDecimal()
            mPresenter.ExchangeOutEvent(receiveAccount, BigDecimal(receiveMoney), AppPrefsUtils.getString(BaseConstant.MY_OAS_PROTOCOL), BigDecimal(text.text.toString()), BaseConstant.GAS_LIMIT.toBigDecimal(),receiveComment)//AppPrefsUtils.getString(BaseConstant.MY_OAS_ADDRESS),
        }

        //手续费计算
        mFactorTv.text = ToolUtil.caculateETH(text.text.toString().toInt(),BaseConstant.GAS_LIMIT)
        //用户账号可用的ETH余额,不知道咋获取
        //canUseMoney.text = getEth()

        mSeakBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            //SeekBar滚动过程中的回调函数
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                text.setPadding(0,10,changeToBg(width-i*step).toInt(),0)
                text.text =check(i)
               // Log.i("zbb2",i.toString())
               // Log.i("zbb2",changeToBg(width-i*step).toString())
               // text.layout(i*step.toInt(),20,210,80)
                mFactorTv.text = ToolUtil.caculateETH(text.text.toString().toInt(),BaseConstant.GAS_LIMIT)
                //seekBar.setProgress(i)
            }
            //SeekBar开始滚动的回调函数
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }
            //SeekBar停止滚动的回调函数
            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

    }

    fun toast(msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    //清空输入
    fun clearInput(){
        mReceiveAccount.text.clear()
        mMoney.text.clear()
        mRemark.text.clear()
        text.setPadding(0,10,changeToBg(width-((high-low+1)/2-1)*step).toInt(),0)
        text.text = ((high-low+1)/2).toString()

        mFactorTv.text = ToolUtil.caculateETH(text.text.toString().toInt(),BaseConstant.GAS_LIMIT)
        canUseMoney.text =(canUseMoney.text.toString().toBigDecimal()-mFactorTv.text.toString().toBigDecimal()).setScale(10,BigDecimal.ROUND_DOWN).toString()
    }

    //dp->px
    fun changeToBg(value:Float):Int{
       return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics()).toInt()
    }

    fun check(progress: Int): String {
        var curValue = (high-low+1) * progress / Math.abs(high)
        if (low < 0 && high < 0) {
            curValue = low + curValue
        } else if (low < 0 && high > -1) {
            curValue = curValue + low
        }
        return (curValue+1).toString()
    }

    fun useCamera(){
        permission!!.request(Manifest.permission.CAMERA).subscribe({ granted ->
            if(granted){
                val intent = Intent(this@ExchangeOutActivity, CaptureActivity::class.java)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                var content = data.getStringExtra(Constant.CODED_CONTENT)
                if(content.length<42){
                    Toast.makeText(this, "发送地址格式有误！", Toast.LENGTH_SHORT).show()
                    return
                }
                var address_1:String = content.substring(0,16)
                var address_2:String = content.substring(21,37)

                if(content!=null && content.length == 42){
                    content = ToolUtil.modifyAddressInfo(address_1).plus(content.substring(16,21)).plus("\n")
                            .plus(ToolUtil.modifyAddressInfo(address_2)).plus(content.substring(37,42))
                }
                mReceiveAccount.setText(content)//"$content"
            }
        }
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


    override fun onGetExchangeOutEvent(value: TransferOasResp) {
        var text = "操作失败"
        if(value.txHash != null){
            text ="操作成功！"
            clearInput()
            toast(text)
           // var pair:Pair<String,String> = Pair<String,String>("balance",tMoney.toString())
            startActivity(intentFor<ExchangeCoinActivity>().singleTop().clearTop())
            finish()
        }else{
            toast(text)
        }
    }

    fun getEth():String{
        var ethOwn = AppPrefsUtils.getString(BaseConstant.USER_OWN_ETH)
        return ethOwn
    }

}


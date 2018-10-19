package com.oases.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.oases.R
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.data.protocol.TransferOasResp
import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.ExchangeOutPresenter
import com.oases.presenter.RedrawOasPresenter
import com.oases.presenter.view.ExchangeOutView
import com.oases.presenter.view.RedrawOasView
import com.oases.ui.type.ToolUtil
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.activity_charge_coin_oas.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import java.math.BigDecimal
import java.util.regex.Pattern


class OasExchangeToOnlineActivity : BaseMvpActivity<RedrawOasPresenter>(), RedrawOasView {
    var permission: RxPermissions?= null
    var high:Int = 0
    var low:Int = 0
    var middle:Int = 0
    var width:Int = 0
    var step:Float = 0.toFloat()
    lateinit var text:TextView
    var beforeText:String = ""
    var address:String = AppPrefsUtils.getString(BaseConstant.MY_OAS_ADDRESS)
    //var tMoney:BigDecimal = 0.toBigDecimal()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charge_coin_oas)
        //OAS积分
        var balance:String = getIntent().getStringExtra("balance")
        var unconfirmed:String = getIntent().getStringExtra("unconfirmed")
        tipLeft.setHint((BaseConstant.GAS_PRICE_LOW.toString()).plus(" GWEI"))
        tipRight.setHint(BaseConstant.GAS_PRICE_HIGH.toString().plus(" GWEI"))
        //mFactorTv.text = balance
        mAvailableAmount.text = (balance.toBigDecimal().subtract(unconfirmed.toBigDecimal())).setScale(4,BigDecimal.ROUND_HALF_UP).toString()
        canUseMoney.text = getIntent().getStringExtra("eth")

        //接收方地址
        if(address.length == 42){
            var address_1:String = address.substring(0,16)
            var address_2:String = address.substring(21,37)
            address = ToolUtil.modifyAddressInfo(address_1).plus(address.substring(16,21)).plus("\n")
                    .plus(ToolUtil.modifyAddressInfo(address_2)).plus(address.substring(37,42))
        }
        mMyAddress.setRightTopText(address)
        mReceiveAccount.setHint(AppPrefsUtils.getString(BaseConstant.USER_NAME)?:"")

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


        //金额监听事件
        mMoney.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                beforeText = p0.toString().trim()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               val inputStr = p0.toString().trim()
                if(!(inputStr.isNullOrEmpty() || inputStr.isBlank())){
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

        mExchangeOutBtn.onClick {
            //val receiveAccount:String = mReceiveAccount.text.toString().replace("\n","").replace(" ","")
            val receiveMoney:String = mMoney.text.toString().replace(" ","")
            val receiveComment:String = mRemark.text.toString()
           /* if(receiveAccount.length!=42 || !ToolUtil.regularExpressionValidate(receiveAccount,BaseConstant.ENGLISH_NUMBER)){
                toast("接收地址格式有误！")
                return@onClick
            }*/

            if(receiveMoney.isEmpty() || !ToolUtil.regularExpressionValidate(receiveMoney,BaseConstant.NUMBER_POINT_FOUR)){
                toast("充币金额格式有误！")
                return@onClick
            }
            if(receiveMoney.toBigDecimal() > mAvailableAmount.text.toString().toBigDecimal() ){
                toast("余额不足！")
                return@onClick
            }
            if(canUseMoney.text.toString().toBigDecimal().compareTo(mFactorTv.text.toString().toBigDecimal()) == -1){
                toast("ETH余额不足！")
                return@onClick
            }
            mPresenter.reverseWithdraw(receiveMoney.toBigDecimal(),BigDecimal(text.text.toString()), BaseConstant.GAS_LIMIT.toBigDecimal(),receiveComment)
        }

        //手续费计算
        mFactorTv.text = ToolUtil.caculateETH(text.text.toString().toInt(),BaseConstant.GAS_LIMIT)
        //用户账号可用的ETH余额,不知道咋获取
       // canUseMoney.text = getEth()

        mSeakBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            //SeekBar滚动过程中的回调函数
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                text.setPadding(0,10,changeToBg(width-i*step).toInt(),0)
                text.text =check(i)
                mFactorTv.text = ToolUtil.caculateETH(text.text.toString().toInt(),BaseConstant.GAS_LIMIT)
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

    override fun injectComponent() {
        DaggerMainComponent
                .builder()
                .activityComponent(mActivityComponent)
                .walletModule(WalletModule())
                .build()
                .inject(this)
        mPresenter.mView = this
    }

    fun getEth():String{
        var ethOwn = AppPrefsUtils.getString(BaseConstant.USER_OWN_ETH)
        return ethOwn
    }

    override fun withdraw(t: Int) {
    }

    override fun getOasExtra(t: String) {
    }

    override fun reverseWithdraw(t: Int) {
        var text = "充币请求提交失败！"
        if(t == 1){
            text ="充币请求提交成功！"
            clearInput()
            toast(text)
            // var pair:Pair<String,String> = Pair<String,String>("balance",tMoney.toString())
            startActivity(intentFor<ExchangeCoinActivity>().singleTop().clearTop())
            finish()
        }else{
            toast(text)
        }
    }

}


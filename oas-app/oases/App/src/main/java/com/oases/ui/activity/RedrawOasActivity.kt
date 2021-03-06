package com.oases.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.oases.R
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.data.protocol.OasResp
import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.RedrawOasPresenter
import com.oases.presenter.view.RedrawOasView
import com.oases.ui.type.ToolUtil
import kotlinx.android.synthetic.main.activity_redraw_oas.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast
import java.math.BigDecimal

class RedrawOasActivity : BaseMvpActivity<RedrawOasPresenter>(), RedrawOasView {

    private var extra:String? = null
    private var max_extra:String? = null
    private var min_extra:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    private fun initView() {
        setContentView(R.layout.activity_redraw_oas)
        var address = AppPrefsUtils.getString(BaseConstant.MY_OAS_ADDRESS)
        if(address.length == 42){
            var address_1:String = address.substring(0,16)
            var address_2:String = address.substring(21,37)
            address = ToolUtil.modifyAddressInfo(address_1).plus(address.substring(16,21)).plus("\n")
                    .plus(ToolUtil.modifyAddressInfo(address_2)).plus(address.substring(37,42))
        }
        mMyAddress.setRightTopText(address)
        var balance = intent.getStringExtra(BaseConstant.MY_OAS_AMOUNT)
        var unconfirmed = intent.getStringExtra(BaseConstant.MY_OAS_AMOUNT_UNCONFIRMED)
        mAvailableAmount.text =  (balance.toBigDecimal()).setScale(4, BigDecimal.ROUND_HALF_UP).toString()//.subtract(unconfirmed.toBigDecimal())

        mPresenter.getOasExtra()

        //金额监听事件
        mAmount.addTextChangedListener(object: TextWatcher {
            var beforeText:String = ""
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                beforeText = p0.toString().trim()
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val inputStr = p0.toString().trim()
                if(!(inputStr.isNullOrEmpty() || inputStr.isBlank())){
                    if(!ToolUtil.regularExpressionValidate(inputStr,BaseConstant.NUMBER_POINT_FOUR)){
                        val diff = inputStr.length - beforeText.length
                        mAmount.setText(beforeText)
                        mAmount.setSelection(inputStr.length-diff) //删除字符重写光标位置

                    }
                    getOasExtraResult()
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

        mConfirmRedrawBtn.onClick {
           // val receiveAccount:String =  mMyAddress.getRightTopText().toString().replace("\n","").replace(" ","")
            val receiveMoney:String = mAmount.text.toString().replace(" ","")
            val receiveComment:String = mRemark.text.toString()
            /*if(receiveAccount.length!=42 || !ToolUtil.regularExpressionValidate(receiveAccount,BaseConstant.ENGLISH_NUMBER)){
                toast("发送地址格式有误！")
                return@onClick
            }*/
            if(receiveMoney.isEmpty() || !ToolUtil.regularExpressionValidate(receiveMoney,BaseConstant.NUMBER_POINT_FOUR)){
                toast("金额格式有误！")
                return@onClick
            }
            if(receiveMoney.toBigDecimal().compareTo(max_extra?.toBigDecimal()) == 1){
                toast("超过限额，操作失败！")
                return@onClick
            }
            if(receiveMoney.toBigDecimal().add(mFactorTv.text.toString().toBigDecimal()).compareTo( mAvailableAmount.text.toString().toBigDecimal())==1 ){
                toast("余额不足,操作失败！")
                return@onClick
            }

            mPresenter.withdraw(receiveMoney,receiveComment, mFactorTv.text.toString())

        }
    }
    override fun withdraw(t: Int) {
        var text = "在线钱包划转请求提交失败！"
        if(t==1){
            text ="在线钱包划转请求提交成功！"
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            startActivity(intentFor<MainActivity>().singleTop().clearTop())
            finish()
        }else{
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun reverseWithdraw(t: Int) {
    }
    override fun getOasExtra(t: OasResp) {
        extra = t.value
        max_extra = t.valueMax
        min_extra = t.valueMin

        getOasExtraResult()
    }

    private fun getOasExtraResult(){
        var amount = mAmount.text.toString().replace(" ","")
        if(amount.isEmpty()){
            mFactorTv.text = min_extra?.toBigDecimal()?.setScale(4,BigDecimal.ROUND_UP).toString()
        }else{
            var nowExtra = amount.toString().toBigDecimal().multiply(extra?.toBigDecimal()).setScale(4,BigDecimal.ROUND_UP)
            if(nowExtra.compareTo(min_extra?.toBigDecimal()) == -1){
                mFactorTv.text = min_extra?.toBigDecimal()?.setScale(4,BigDecimal.ROUND_UP).toString()
            }else{
                mFactorTv.text = nowExtra.toString()
            }
        }

    }


}

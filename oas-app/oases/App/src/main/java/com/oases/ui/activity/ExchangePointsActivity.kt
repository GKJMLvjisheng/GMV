package com.oases.ui.activity


import android.app.DatePickerDialog
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.oases.R
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.data.protocol.InquirePointFactorResp
import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.ExchangePointsPresenter
import com.oases.presenter.view.ExchangePointsView
import kotlinx.android.synthetic.main.activity_exchange_points.*
import org.jetbrains.anko.Android
import org.jetbrains.anko.toast
import java.util.*

class ExchangePointsActivity : BaseMvpActivity<ExchangePointsPresenter>(), ExchangePointsView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_points)
        mPeriod.setRightTopText(getDayAndMonth())
        mPresenter.inquirePointsFactor(mPeriod.getRightTopText())
        mExchangePointsBtn.onClick{
            mPresenter.redeemPoint(mPeriod.getRightTopText(), mType.getRightTopText(), mFactor.getRightTopText(), mAmount.getRightTopText())
        }

        mPeriod.onClick {

            val date = mPeriod.getRightTopText().toString().splitToSequence('-')
            val datePickerDialog = DatePickerDialog(
                    this,
                    DatePickerDialog.THEME_HOLO_LIGHT,
                    dateSetListener,
                    date.elementAt(0).toInt(), date.elementAt(1).toInt(), 0)
            datePickerDialog.datePicker
                    .findViewById<ViewGroup>(Resources.getSystem().getIdentifier("day", "id", "android"))?.visibility = View.GONE
            datePickerDialog.show()
        }
    }

    var dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, _ ->
        mPeriod.setRightTopText(year.toString() + "-" + "%02d".format(month + 1))
        mPeriod.invalidate()
        mPresenter.inquirePointsFactor(mPeriod.getRightTopText())
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

    override fun onGetFactor(factor: InquirePointFactorResp) {
        Log.d("zbb", "get factor")
        mPeriod.setRightBottomText(factor.amount.toString() + " 积分可用")
        mFactor.setRightTopText(factor.factor.toString())
        mAmount.setRightTopText(factor.amount.toString())
    }

    override fun onRedeemSucceed() {
        toast("兑换成功")
        mPeriod.setRightBottomText(0.toString() + " 积分可用")
        mAmount.setRightTopText(0.toString())
    }

    override fun onRedeemFailed() {
        toast("兑换失败")
    }
    fun getDayAndMonth():String{
        val cal:Calendar = Calendar.getInstance()
        val year:String = cal.get(Calendar.YEAR).toString()
        val month = cal.get(Calendar.MONTH)+1
        var monthStr:String
        if(month<10){
            monthStr = "0".plus(month)
        }else{
            monthStr = month.toString()
        }
        return year.plus("-").plus(monthStr)
    }
}

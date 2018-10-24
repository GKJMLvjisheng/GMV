package com.oases.ui.adapter

import android.content.DialogInterface
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.net.toUri
import com.oases.R
import com.oases.base.common.BaseApplication.Companion.context
import com.oases.base.common.BaseConstant
import com.oases.base.utils.AppPrefsUtils
import com.oases.base.widgets.ExchangeItem
import com.oases.data.protocol.EnergyItem
import com.oases.data.protocol.PointItem
import com.oases.ui.fragment.ExchangeItemFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_exchangeitem.view.*
import com.oases.ui.fragment.ExchangeItemFragment
import com.oases.ui.activity.ExchangeCoinActivity
import com.oases.ui.fragment.WalletOnLineFragment
import java.math.BigDecimal


/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class ExchangeItemRecyclerViewAdapter(
        private var mValues: ArrayList<PointItem>,
        private var eValues: ArrayList<EnergyItem>,
        private val type:String,
        private val mListener: OnListFragmentInteractionListener?,
        private val viewInMore: ExchangeItemFragment?,
        private val viewInOnlineWallet: WalletOnLineFragment?,
        private val viewInExchangeWallet: ExchangeCoinActivity?
        )
    : RecyclerView.Adapter<ExchangeItemRecyclerViewAdapter.ViewHolder>(){

    private val mOnClickListener: View.OnClickListener
    //private val type:String = AppPrefsUtils.getString(BaseConstant.MORE_TYPE)

    init {
        mOnClickListener = View.OnClickListener { v ->
            var item:EnergyItem? = null
            var item2:PointItem? = null
            if(type.equals("ENERGY")){
                item = v.tag as EnergyItem
            }else if(type.equals("WALLET") || type.equals("EXCHANGE")){
                item2 = v.tag as PointItem

                if(type.equals("WALLET")){

                    var extra = if(item2.extra==null || (item2.title?:"").indexOf("充币")!= -1)"" else "\n手续费：".plus( item2.extra!!.toBigDecimal().setScale(2,BigDecimal.ROUND_HALF_UP).toString())
                    if(item2.remark.isNullOrBlank()){
                        alertWindow("无备注".plus(extra),1,null,null)
                    }else{
                        alertWindow("备注：".plus(item2.remark).plus(extra),1,null,null)

                    }
                }else{
                    if(item2.remark.isNullOrBlank()){
                        alertWindow("无备注".plus("\n").plus("hash:").plus(item2.txHash?:""),2,item2.txHash,item2.txNetwork)
                    }else{
                        alertWindow("备注：".plus(item2.remark).plus("\n").plus("hash:").plus(item2.txHash?:""),2,item2.txHash,item2.txNetwork)
                    }
                    /*if(viewInMore!=null){
                        viewInMore?.mPresenter?.getExchangeResult(item2.title?:"",item2.value?: BigDecimal.valueOf(0),item2.address?:"",item2.txHash?:"",item2.txResult,item2.uuid?:"")
                    }
                    if(viewInExchangeWallet!=null){
                        viewInExchangeWallet?.mPresenter?.getExchangeResult(item2.title?:"",item2.value?: BigDecimal.valueOf(0),item2.address?:"",item2.txHash?:"",item2.txResult,item2.uuid?:"")
                    }*/
                }
            }else{
                item = v.tag as EnergyItem
            }
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item2,item)
        }
    }

    private fun alertWindow(str:String,t:Int,txHash:String?,txNetwork:String?){

        var dialogBuilder:AlertDialog.Builder? = null
        if(viewInMore!=null){
            dialogBuilder = AlertDialog.Builder(this.viewInMore.context!!)
        }
        if(viewInOnlineWallet!=null){
            dialogBuilder = AlertDialog.Builder(this.viewInOnlineWallet.context!!)
        }
        if(viewInExchangeWallet!=null){
            dialogBuilder = AlertDialog.Builder(this.viewInExchangeWallet)
        }
        //val dialogBuilder = AlertDialog.Builder(this.fragment2?.context!!)
        when(t){
            1->dialogBuilder?.setMessage(str)?.setNegativeButton("关闭弹框", null)
            2->dialogBuilder?.setMessage(str)?.setPositiveButton("网站跳转", DialogInterface.OnClickListener{
                    _,_ -> jumpToWebSite(viewInMore,viewInOnlineWallet,viewInExchangeWallet,txHash,txNetwork)
                })?.setNegativeButton("关闭弹框", null)
        }

        val dialog = dialogBuilder?.create() as AlertDialog
        dialog.show()
        dialog.findViewById<TextView>(android.R.id.message)?.gravity = Gravity.CENTER
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_exchangeitem, null, false)
        return ViewHolder(view)
    }
    //根据hash值跳转查询页面
    private fun jumpToWebSite(dialogBuilder:ExchangeItemFragment?,viewInOnlineWallet:WalletOnLineFragment?,viewInExchangeWallet:ExchangeCoinActivity?,txHash: String?,txNetwork:String?){
        //获取默认网络,目前默认ropsten
        if(!txHash.isNullOrBlank()){
            //AppPrefsUtils.putString(BaseConstant.TRANSFER_NET,"ropsten")
            var netType = if(txNetwork.isNullOrBlank()) "ropsten" else txNetwork
            val intent = Intent()
            intent.setAction(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData("https://".plus(netType).plus(BaseConstant.TRANSFER_CHECK_ADDRESS).plus(txHash).toUri())
            if(viewInMore!=null){
                viewInMore.startActivity(intent)
            }
            if(viewInOnlineWallet!=null){
                viewInOnlineWallet.startActivity(intent)
            }
            if(viewInExchangeWallet!=null){
                viewInExchangeWallet.startActivity(intent)
            }
        }else{
            Toast.makeText(context, "无hash值，无法跳转", Toast.LENGTH_SHORT).show()
        }

    }
    fun clearData(){
        mValues.clear()
        eValues.clear()
    }

   fun update(items:ArrayList<PointItem>){
        mValues.addAll(items)
        notifyDataSetChanged()
    }
    fun updateEnerey(items:ArrayList<EnergyItem>){
        eValues.addAll(items)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(type.equals("WALLET") || type.equals("EXCHANGE")) {
            var item: PointItem? = null
            if (mValues.size > 0) {
                item = mValues[position]

                val title = item.title?:""
                //holder.mExchangeItemView.setLeftTopText(title.plus(if(item.title.equals("提币") || item.title.equals("充币")) status else "" ))
                holder.mExchangeItemView.setLeftBottomText(item.created?:"")
                holder.mExchangeItemView.setRightTopText((if (item.inOrOut==0) "-" else "+").plus(item.value?.setScale(5).toString()))
                // holder.mExchangeItemView.setRightBottomText(item.subTitle.plus(item.comment).toString())
                holder.mExchangeItemView.setRightTopTextColor()
               // var changeUserName: String?
                if (type.equals("WALLET")) {
                    var status :String
                    when(item.txResult){
                        0-> status = "(待审核)"
                        1-> status = "(进行中)"
                        2-> status ="(失败)"
                        3-> status=""
                        else ->status = "(失败)"
                    }
                    //changeUserName = item.changeUserName
                    holder.mExchangeItemView.setRightBottomText(item.subTitle?:"")
                    holder.mExchangeItemView.setLeftTopText(title.plus(if(item.title.equals("提币") || item.title.equals("充币")) status else "" ))
                } else {
                    var status :String
                    when(item.txResult){
                        0-> status = "(进行中)"
                        1-> status = ""
                        else ->status = "(失败)"
                    }
                    var subT = item.subTitle?:""
                    if(subT.length == 2){
                        subT =""
                    }
                    if(subT.length>42){
                        subT =  subT.substring(0, 5).plus("...").plus(subT.substring(subT.length - 3, subT.length))
                    }
                    holder.mExchangeItemView.setLeftTopText(title.plus(status))
                    holder.mExchangeItemView.setRightBottomText(subT)
                }

            }
            with(holder.mView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }else{ /*if(type.equals("ENERGY"))*/
            var item:EnergyItem? = null
            if(eValues.size>0){
                item = eValues[position]
                holder.mExchangeItemView.setLeftTopText(item.category?:"")
                holder.mExchangeItemView.setLeftBottomText(item.created?:"")
                holder.mExchangeItemView.setRightTopText((if(item.inOrOut==0) "-" else "+").plus(item.decPoint?.setScale(5).toString()))
                holder.mExchangeItemView.setRightBottomText(item.activity?:"")
                holder.mExchangeItemView.setRightTopTextColor()

            }
            with(holder.mView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }

        }
    }
    override fun getItemCount(): Int {
       return when(type){
           "ENERGY" -> eValues.size
           "WALLET" -> mValues.size
           "EXCHANGE" -> mValues.size
           else ->  eValues.size
       }
    }


    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val mExchangeItemView: ExchangeItem = mView.mItem

        override fun toString(): String {
            return super.toString()
        }



    }



}

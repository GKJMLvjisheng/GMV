package com.oases.computingpower.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.load.engine.Resource
import com.oases.computingpower.R
import com.oases.computingpower.data.protocol.MinerInfoItem
import kotlinx.android.synthetic.main.miner_item.view.*
import java.math.BigDecimal
import java.util.*

class RecycleMinerViewAdapter(
        val mList:MutableList<MinerInfoItem>
)
    : RecyclerView.Adapter<RecycleMinerViewAdapter.RecyclerHolder>() {
    private var list :MutableList<MinerInfoItem> = mList
    private var subClickListener: SubClickListener? = null


    //负责承载每个子项的布局
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleMinerViewAdapter.RecyclerHolder {
       val item:View =  LayoutInflater.from(parent.context).inflate(R.layout.miner_item,null,false)
       return RecyclerHolder(item)
    }

    override fun getItemCount(): Int {
        /*if(list == null) {
            return 0
        }*/
        return list.size
    }

    //负责将每个子项holder绑定数据
    override fun onBindViewHolder(holder: RecycleMinerViewAdapter.RecyclerHolder, position: Int) {
        //根据指定元素排序
        /*Collections.sort(list,object: Comparator<MinerInfoItem> {
            override fun compare(o1: MinerInfoItem?, o2: MinerInfoItem?): Int {
                return o1!!.orderNum - o2!!.orderNum
            }
        })*/
       // if (list!=null) {
            val mData = list.get(position)
            if (mData.minerGrade == 1) {
                holder.mExchangeItemView.mMinerImage.setImageResource(R.drawable.onelevel)
            } else if (mData.minerGrade == 2) {
                holder.mExchangeItemView.mMinerImage.setImageResource(R.drawable.twolevel)
            } else if (mData.minerGrade == 3) {
                holder.mExchangeItemView.mMinerImage.setImageResource(R.drawable.treelevel)
            } else if (mData.minerGrade == 4) {
                holder.mExchangeItemView.mMinerImage.setImageResource(R.drawable.fourlevel)
            }
            holder.mExchangeItemView.mMinerName.setText(mData.minerName)

        val minerPrice = mData.minerPrice.toString()
        var decimalPrice  =    minerPrice.substring(minerPrice.indexOf("."),minerPrice.length)
        decimalPrice = decimalPrice.substring(1,decimalPrice.length)
        if (decimalPrice.substring(decimalPrice.length-1,decimalPrice.length)=="0"){
         val viewMinerPrice =minerPrice.substring(0,minerPrice.length-1)
            holder.mExchangeItemView.mMinerPrice.setText(viewMinerPrice.plus("  OAS"))
        }
        if (decimalPrice.substring(decimalPrice.length-2,decimalPrice.length)=="00"){
            val viewMinerPrice =minerPrice.substring(0,minerPrice.length-2)
            holder.mExchangeItemView.mMinerPrice.setText(viewMinerPrice.plus("  OAS"))
        }
        if (decimalPrice.substring(decimalPrice.length-3,decimalPrice.length)=="000"){
            val viewMinerPrice =minerPrice.substring(0,minerPrice.length-3)
            holder.mExchangeItemView.mMinerPrice.setText(viewMinerPrice.plus("  OAS"))
        }
        if (decimalPrice.substring(decimalPrice.length-4,decimalPrice.length)=="0000"){
            val viewMinerPrice =minerPrice.substring(0,minerPrice.length-4)
            holder.mExchangeItemView.mMinerPrice.setText(viewMinerPrice.plus("  OAS"))
        }
        if (decimalPrice.substring(decimalPrice.length-5,decimalPrice.length)=="00000"){
            val viewMinerPrice =minerPrice.substring(0,minerPrice.length-5)
            holder.mExchangeItemView.mMinerPrice.setText(viewMinerPrice.plus("  OAS"))
        }
        if (decimalPrice.substring(0,decimalPrice.length)=="000000"){
            val viewMinerPrice =minerPrice.substring(0,minerPrice.length-7)
            holder.mExchangeItemView.mMinerPrice.setText(viewMinerPrice.plus("  OAS"))
        }else{
            holder.mExchangeItemView.mMinerPrice.setText(minerPrice.plus("  OAS"))
        }
            holder.mExchangeItemView.mMinerPrice.setTextColor(Color.parseColor("#00944b"))  //绿色

        val minerPower = mData.minerPower.toString()
        var decimalPower  = minerPower.substring(minerPower.indexOf("."),minerPower.length)
        decimalPower = decimalPower.substring(1,decimalPower.length)
        if (decimalPower.substring(decimalPower.length-1,decimalPower.length)=="0"){
            val viewMinerPower =minerPower.substring(0,minerPower.length-1)
            holder.mExchangeItemView.mCpomutingPowerValue.setText("算力".plus(viewMinerPower.plus(",")))
        }
        if (decimalPower.substring(decimalPower.length-2,decimalPower.length)=="00"){
            val viewMinerPower =minerPower.substring(0,minerPower.length-2)
            holder.mExchangeItemView.mCpomutingPowerValue.setText("算力".plus(viewMinerPower.plus(",")))
        }
        if (decimalPower.substring(decimalPower.length-3,decimalPower.length)=="000"){
            val viewMinerPower =minerPower.substring(0,minerPower.length-3)
            holder.mExchangeItemView.mCpomutingPowerValue.setText("算力".plus(viewMinerPower.plus(",")))
        }
        if (decimalPower.substring(decimalPower.length-4,decimalPower.length)=="0000"){
            val viewMinerPower =minerPower.substring(0,minerPower.length-4)
            holder.mExchangeItemView.mCpomutingPowerValue.setText("算力".plus(viewMinerPower.plus(",")))
        }
        if (decimalPower.substring(decimalPower.length-5,decimalPower.length)=="00000"){
            val viewMinerPower =minerPower.substring(0,minerPower.length-5)
            holder.mExchangeItemView.mCpomutingPowerValue.setText("算力".plus(viewMinerPower.plus(",")))
        }
        if (decimalPower.substring(0,decimalPower.length)=="000000"){
            val viewMinerPower =minerPower.substring(0,minerPower.length-7)
            holder.mExchangeItemView.mCpomutingPowerValue.setText("算力".plus(viewMinerPower.plus(",")))
        }else{
            holder.mExchangeItemView.mCpomutingPowerValue.setText("算力".plus(minerPower.plus(",")))
        }
            holder.mExchangeItemView.mPrescription.setText("时效".plus(mData.minerPeriod.toString().plus("天")))
            //holder.mExchangeItemView.mMaxOutput.setText("最高每天可产生 ".plus(mData.minerPeriod.toString().plus("  OAS")))
            holder.mExchangeItemView.mMaxOutput.visibility = View.GONE
            // holder.mExchangeItemView.mMaxOutput.setText("最高每天可产生".plus(mData.minerMaxOutput.toString().plus("  OAS")))

            holder.mExchangeItemView.mMinerNumber.setOnClickListener {
                holder.mExchangeItemView.mMinerNumber.isCursorVisible = true
            }
            //实现点击减号数量-1
            holder.mExchangeItemView.mReduce1.setOnClickListener {
                if ((holder.mExchangeItemView.mMinerNumber.text).toString()==""){  // 将数量设为可编辑状态，当用户忘记输入时，重置值1，防止程序报错
                    holder.mExchangeItemView.mMinerNumber.setText("1")
                }
                holder.mExchangeItemView.mMinerNumber.isCursorVisible = false
                var mMinerNumberNow = holder.mExchangeItemView.mMinerNumber.text.toString().toInt()
                if (mMinerNumberNow != 0) {
                    mMinerNumberNow --
                }
                holder.mExchangeItemView.mMinerNumber.setText(mMinerNumberNow.toString())
            }

            //实现点击加号数量+1
            holder.mExchangeItemView.mAdd1.setOnClickListener {
                if ((holder.mExchangeItemView.mMinerNumber.text).toString()==""){
                    holder.mExchangeItemView.mMinerNumber.setText("1")
                }
                holder.mExchangeItemView.mMinerNumber.isCursorVisible = false
                var mMinerNumberNow = holder.mExchangeItemView.mMinerNumber.text.toString().toInt()
                mMinerNumberNow ++
                holder.mExchangeItemView.mMinerNumber.setText(mMinerNumberNow.toString())
            }

            holder.mExchangeItemView.mBuyButton.setOnClickListener {
                Log.d("adapter", "setOnClickListener")
                    if (subClickListener != null && holder.mExchangeItemView.mMinerNumber.text.toString()!="") {
                        subClickListener!!.onTopicClickListener(holder.mExchangeItemView.mMinerName.text.toString(), holder.mExchangeItemView.mMinerNumber.text.toString().toInt(), mData.minerPrice.multiply(holder.mExchangeItemView.mMinerNumber.text.toString().toBigDecimal()))
                        Log.d("adapter", "qqqqqqq")
                    }
            }
       // }
    }

    inner class RecyclerHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mExchangeItemView: RelativeLayout = mView.mMinerItem
    }

    fun setData(data:MutableList<MinerInfoItem>){
        //if(null != data){
            this.list.addAll(data)
            notifyDataSetChanged()
        //}
    }

    fun setsubClickListener(topicClickListener: SubClickListener) {
        Log.d("zbb", "set subclick listener")
        subClickListener = topicClickListener
    }

    //在Adapter中定义接口，传数据给Activity
    interface SubClickListener {
        fun onTopicClickListener(minerName: String, minerNum: Int, minerSum: BigDecimal)
    }

}

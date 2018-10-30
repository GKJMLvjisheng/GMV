package com.oases.computingpower.adapter


import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oases.base.widgets.ExchangeItem
import com.oases.computingpower.R
import kotlinx.android.synthetic.main.miner_buyhistory_item.view.*
import com.oases.computingpower.data.protocol.MinerHistoryItem
import java.math.BigDecimal

class RecycleMinerHistoryViewAdapter(val mList:MutableList<MinerHistoryItem>)
    : RecyclerView.Adapter<RecycleMinerHistoryViewAdapter.RecyclerHolder>() {
    private var list :MutableList<MinerHistoryItem> = mList
    private var minerStatus:String = ""
    private var subClickListener: SubClickListener? = null


    //负责承载每个子项的布局
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleMinerHistoryViewAdapter.RecyclerHolder {
       val item:View =  LayoutInflater.from(parent.context).inflate(R.layout.miner_buyhistory_item,null,false)
       return RecyclerHolder(item)
    }

    override fun getItemCount(): Int {
        if(list == null) {
            return 0
        }
        return list.size
    }

    //负责将每个子项holder绑定数据
    override fun onBindViewHolder(holder: RecycleMinerHistoryViewAdapter.RecyclerHolder, position: Int) {
        if(list!=null){
            val mData = list.get(position)
            var status = mData.minerStatus
            if (status==1){
                 minerStatus = "（工作中）"
            }else{
                 minerStatus = "（寿终正寝）"
            }
            holder.mExchangeItemView.setLeftTopText((mData.minerName).plus(minerStatus))
            holder.mExchangeItemView.setLeftBottomText("购买：".plus(mData.created))
            holder.mExchangeItemView.setRightTopText("x".plus(mData.minerNum.toString()))
            holder.mExchangeItemView.setRightBottomText("到期：".plus(mData.minerEndTime))
            holder.mExchangeItemView.setTimeTextSize()
            holder.mExchangeItemView.setTimeTextMoveDown()
            holder.mExchangeItemView.setOnClickListener {
                if (subClickListener != null) {
                    subClickListener!!.onTopicClickListener(mData.minerName,mData.minerPrice,mData.minerPower,mData.period)
                }
            }
        }

    }

    inner class RecyclerHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mExchangeItemView: ExchangeItem = mView.mMinerHistoryItem
    }

    fun setData(data:MutableList<MinerHistoryItem>){
        if(null != data){
            this.list.addAll(data)
            notifyDataSetChanged()
        }
    }


    fun setsubClickListener(topicClickListener: SubClickListener) {
        Log.d("zbb", "set subclick listener")
        subClickListener = topicClickListener
    }

    //在Adapter中定义接口，传数据给Activity
    interface SubClickListener {
        fun onTopicClickListener(minerName: String, minerPrice: BigDecimal, minerPower: BigDecimal,minerPeriod:Int)
    }
}

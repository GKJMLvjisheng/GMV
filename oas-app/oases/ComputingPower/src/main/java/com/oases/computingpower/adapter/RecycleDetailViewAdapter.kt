package com.oases.computingpower.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oases.base.common.BaseApplication.Companion.context
import com.oases.base.widgets.ExchangeItem
import com.oases.computingpower.R
import com.oases.computingpower.data.protocol.ComputingPowerItem
import kotlinx.android.synthetic.main.detail_item.*
import kotlinx.android.synthetic.main.detail_item.view.*

class RecycleDetailViewAdapter(val mList:MutableList<ComputingPowerItem>)
    : RecyclerView.Adapter<RecycleDetailViewAdapter.RecyclerHolder>() {
    private var list :MutableList<ComputingPowerItem> = mList

    //负责承载每个子项的布局
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleDetailViewAdapter.RecyclerHolder {
       val item:View =  LayoutInflater.from(parent.context).inflate(R.layout.detail_item,null,false)
       return RecyclerHolder(item)
    }

    override fun getItemCount(): Int {
        if(list == null) {
            return 0
        }
        return list.size
    }

    //负责将每个子项holder绑定数据
    override fun onBindViewHolder(holder: RecycleDetailViewAdapter.RecyclerHolder, position: Int) {
        if(list!=null){
            val mData = list.get(position)
            holder.mExchangeItemView.setLeftTopText(mData.activity?:"")
            holder.mExchangeItemView.setLeftBottomText(mData.created?:"")
            if (mData.inOrOut==1) {
                holder.mExchangeItemView.setRightTopText("+".plus(mData.powerChange))
            }else if (mData.inOrOut==0){
                holder.mExchangeItemView.setRightTopText("-".plus(mData.powerChange))
            }
            holder.mExchangeItemView.setRightTopTextColor()
            holder.mExchangeItemView.setRightTopMoveDown()
        }

    }

    inner class RecyclerHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mExchangeItemView: ExchangeItem = mView.mItem
    }

    fun setData(data:MutableList<ComputingPowerItem>){
        if(null != data){
            this.list.addAll(data)
            notifyDataSetChanged()
        }
    }
}

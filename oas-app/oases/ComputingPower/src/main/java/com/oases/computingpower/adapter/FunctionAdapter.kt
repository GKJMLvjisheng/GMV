package com.oases.computingpower.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.oases.computingpower.R
import com.oases.computingpower.data.protocol.FunctionItem
import kotlinx.android.synthetic.main.grid_item.view.*
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView

class FunctionAdapter(
        private val context: Context,
        private val data: List<FunctionItem>,
        private val mListener: View.OnClickListener)
    : RecyclerView.Adapter<FunctionAdapter.ViewHolder>() {

    private val inflater: LayoutInflater
    private lateinit var mData:List<FunctionItem>
    private var indexList:MutableList<Int> = arrayListOf()

    init {
        inflater = LayoutInflater.from(context)
        mData = this.data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.grid_item, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = mData[position]
        setImage(item.imageUrl, viewHolder.image)
        Log.d("zbb", "set name ${item.name}")
        viewHolder.name.text = item.name
        if(indexList.size>0 && indexList.contains(position)){
           viewHolder.badge.setBadgeText("√").badgeGravity = Gravity.END or Gravity.TOP
        //    viewHolder.name.text = item.name.plus("1111")
            viewHolder.badge = null
        }
        /*for(i in indexList){
            Log.i("zbb",i.toString())
            if(position == i){

            }else{

            }
        }*/

        with(viewHolder.itemView) {
            tag = item
            setOnClickListener(mListener)
        }

    }
    fun setIndex(mList:MutableList<Int>){
        indexList.clear()
        indexList.addAll(mList)
    }
    /*fun update(data: List<FunctionItem>){
        mData = data
        notifyDataSetChanged()
    }*/
    fun setImage(url: String, iv: ImageView) {
        try {
            val rid = context.resources.getIdentifier(url, "drawable", context.packageName)
            iv.setImageResource(rid)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    override fun getItemCount(): Int  = mData.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var image = mView.mImage
        var name = mView.mName
        var badge = QBadgeView(context).bindTarget(itemView.findViewById(R.id.layout))
       /* init {
            badge = QBadgeView(context).bindTarget(itemView.findViewById(R.id.layout))
            //badge.setBadgeText("√").badgeGravity = Gravity.START or Gravity.CENTER
        }*/


    }



}

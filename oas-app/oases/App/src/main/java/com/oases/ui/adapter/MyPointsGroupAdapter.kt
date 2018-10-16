package com.oases.ui.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.oases.R
import com.oases.data.protocol.EnergyItem
import com.oases.ui.fragment.ExchangeItemFragment
import java.util.*

class MyPointsGroupAdapter(internal var context: Context)
    : BaseExpandableListAdapter() {
    var selectParentItem = -1
    var selectChildItem = -1
    //父列表显示的文字
    private var groupData:MutableList<String> = arrayListOf()//arrayListOf("2018-08 第32周", "2018-08 第33周", "2018-08 第34周")//?= null //
    //var itemData = arrayListOf(arrayListOf("+ 3500 ", " + 6000", "+ 8000"), arrayListOf("+ 7000 ", " + 5000", "+ 3000"), arrayListOf("+ 3000 ", " + 6000", "+ 9000"))
    private var itemData :MutableList<MutableList<EnergyItem>>? =arrayListOf()//arrayListOf(arrayListOf(EnergyItem("",0.toBigDecimal(),"","",0.toBigDecimal(),"","",0)))


    //获得父列表项的数目
    // TODO Auto-generated method stub
    override  fun getGroupCount(): Int{
        if(groupData==null){
            return 0
        }
        return groupData.size
    }

    fun setChildSelection(groupPosition: Int, childPosition: Int) {
        selectParentItem = groupPosition
        selectChildItem = childPosition
    }
    //获取子列表项对应的Item
    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        // TODO Auto-generated method stub
        if(itemData != null) {
            return itemData!!.get(groupPosition).get(childPosition)
        }
      return EnergyItem
    }

    //获得子列表项的Id
    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        // TODO Auto-generated method stub
        return childPosition.toLong()
    }
    //获得子列表项
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?,
                     parent: ViewGroup): View {
        // TODO Auto-generated method stub
        var view: View? = convertView
        Log.i("zbb", "groupPosition=$groupPosition,childPosition$childPosition")
        if (null == view) {
            //获取LayoutInflater
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            //获取对应的布局
            view = inflater.inflate(R.layout.point_item, null)
        }
        val textView = view!!.findViewById(R.id.mPoints) as TextView
        val mNameText = view.findViewById(R.id.mName) as TextView
        val mDateText = view.findViewById(R.id.mDate) as TextView
        val mEarnDetailText = view.findViewById(R.id.mEarnDetail) as TextView
        //textView.setText(itemData!!.get(groupPosition).get(childPosition))
        if(itemData!=null){
            var item = itemData!!.get(groupPosition).get(childPosition)
            var mDecPoint = item.decPoint?:0
            if(item.inOrOut == 1){
                textView.setText("+".plus(mDecPoint))
                textView.setTextColor(ContextCompat.getColor(context, com.oases.base.R.color.common_green))
            }else{
                textView.setText( "-".plus(mDecPoint))
                textView.setTextColor(ContextCompat.getColor(context, com.oases.base.R.color.common_red))
            }

            mNameText.setText(item.category?:"")
            mDateText.setText(item.created?:"")
            mEarnDetailText.setText(item.activity?:"")
        }

        if (selectChildItem == childPosition && selectParentItem == groupPosition) {
            Log.i("++++++++++", "点击：$groupPosition,$childPosition")
        }
        return view
    }

    //获得子列表项的数目
    override fun getChildrenCount(groupPosition: Int): Int {
        // TODO Auto-generated method stub
        if(itemData == null) return 0
        return itemData!!.get(groupPosition).size
    }
    //获得父列表项
    override fun getGroup(groupPosition: Int): Any {
        // TODO Auto-generated method stub
        if(groupData!=null){
            return groupData.get(groupPosition)
        }
       return  ""
    }

    //获得父列表项的Id
    override fun getGroupId(groupPosition: Int): Long {
        // TODO Auto-generated method stub
        return groupPosition.toLong()
    }

    //获得父列表项
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?,
                     parent: ViewGroup): View {
        // TODO Auto-generated method stub
        var view: View? = convertView
        Log.i("++++++++++", "groupPosition=$groupPosition")
        if (null == view) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.point_group, null)
        }
        val textView = view!!.findViewById(R.id.mPointGroupName) as TextView
       // if(groupData!=null){
            textView.setText(groupData.get(groupPosition))
       // }
        //val image = view.findViewById(R.id.mPointGroupIcon) as ImageView
        /*
        if (isExpanded) {
            image.setBackgroundResource(R.drawable.icon_arrow)
        } else {
            image.setBackgroundResource(R.mipmap.smile)
        }*/
        return view
    }

    override fun hasStableIds(): Boolean {
        // TODO Auto-generated method stub
        return true
    }
    //子列表项是否能否触发事件，返回true则为可以响应点击
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        // TODO Auto-generated method stub
        return true
    }
    fun pushData(map: Map<String,ArrayList<EnergyItem>>){//InquirePointsDetailResp
        this.groupData.clear()
        this.itemData?.clear()
            for(key in map.keys){
                this.groupData.add(key)
                this.itemData?.add(map[key]!!)
            }
            notifyDataSetChanged()
    }
}


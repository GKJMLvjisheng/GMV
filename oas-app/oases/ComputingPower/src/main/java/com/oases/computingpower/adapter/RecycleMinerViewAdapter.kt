package com.oases.computingpower.adapter


import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.oases.base.common.BaseApplication.Companion.context
import com.oases.computingpower.R
import com.oases.computingpower.data.protocol.MinerInfoItem
import com.oases.computingpower.ui.activity.BuyingMinerActivity
import com.oases.computingpower.ui.activity.BuyingMinerHistoryActivity
import kotlinx.android.synthetic.main.miner_item.view.*

class RecycleMinerViewAdapter(
        val mList:MutableList<MinerInfoItem>,
        private val viewJump: BuyingMinerHistoryActivity?,
        private val viewContent: BuyingMinerActivity?
)
    : RecyclerView.Adapter<RecycleMinerViewAdapter.RecyclerHolder>() {
    private var list :MutableList<MinerInfoItem> = mList

    //负责承载每个子项的布局
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleMinerViewAdapter.RecyclerHolder {
       val item:View =  LayoutInflater.from(parent.context).inflate(R.layout.miner_item,null,false)
       return RecyclerHolder(item)
    }

    override fun getItemCount(): Int {
        if(list == null) {
            return 0
        }
        return list!!.size
    }

    //弹框，用户购买确认信息
      private  fun alertView(){
            var dialogBuilder = AlertDialog.Builder(this.viewContent!!)
            var selectList:Array<String> = arrayOf("名称：","数量：","总计：")

            dialogBuilder?.setItems(selectList, DialogInterface.OnClickListener{
                _, _ ->
            })

            dialogBuilder?.setMessage("请您核对购买信息：")?.setPositiveButton("购买", DialogInterface.OnClickListener{
                dialog, id ->
                //此处填写实现购买的方法，购买成功然后跳进购买记录里面
                //  mPresenter.confirmBuyMiner(minerName,minerNum,minerTotalAccount)
            })?.setNegativeButton("取消", null)

            val dialog = dialogBuilder?.create() as AlertDialog
            dialog.show()
            dialog.run {
                getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.authentication)
                getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.authentication)
                getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(-0x1000000) //白色
                getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(-0x1000000)
            }
        }

    //负责将每个子项holder绑定数据
    override fun onBindViewHolder(holder: RecycleMinerViewAdapter.RecyclerHolder, position: Int) {
        if(list!=null){
            val mData = list.get(position)
            if (mData.minerGrade==1){
                holder.mExchangeItemView.mMinerImage.setImageResource(R.drawable.onelevel)
            }else if (mData.minerGrade==2){
                holder.mExchangeItemView.mMinerImage.setImageResource(R.drawable.twolevel)
            }
            else if (mData.minerGrade==3){
                holder.mExchangeItemView.mMinerImage.setImageResource(R.drawable.treelevel)
            }
            else if (mData.minerGrade==4){
                holder.mExchangeItemView.mMinerImage.setImageResource(R.drawable.fourlevel)
            }
            holder.mExchangeItemView.mMinerName.setText(mData.minerName)
            holder.mExchangeItemView.mMinerPrice.setText(mData.minerPrice.toString())
            holder.mExchangeItemView.mCpomutingPowerValue.setText(mData.minerPower.toString())
            holder.mExchangeItemView.mPrescription.setText(mData.minerPeriod.toString())
           // holder.mExchangeItemView.mMaxOutput.setText(mData.minerMaxOutput.toString())
            holder.mExchangeItemView.mBuyButton.setOnClickListener {
                alertView()
            }
        }

    }

    inner class RecyclerHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mExchangeItemView: RelativeLayout = mView.mMinerItem
    }

    fun setData(data:MutableList<MinerInfoItem>){
        if(null != data){
            this.list?.addAll(data)
            notifyDataSetChanged()
        }
    }
}

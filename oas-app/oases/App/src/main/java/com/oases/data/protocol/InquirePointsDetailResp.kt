package com.oases.data.protocol

import java.math.BigDecimal
import android.os.Parcel
import android.os.Parcelable

data class PointItem(val title:String?, val value:BigDecimal?, val created:String?, val subTitle:String?,val inOrOut:Int,val comment:String?,val uuid:String?,val address:String?,val remark:String?,val txHash:String?,val txResult:Int,val txNetwork:String?): Parcelable{
//data class PointItem(val source:String, val value:Int, val created:String, val activity:String) : Parcelable{
    override fun writeToParcel(p0: Parcel?, p1: Int) {
       /* p0?.writeString(if(title == null) "" else title)
        p0?.writeString(if(value==null) "0" else value.toString())
        p0?.writeString(if(created == null)  "" else created)
        p0?.writeString(if(subTitle == null) "" else subTitle)
        p0?.writeString(if(inOrOut == null) "" else inOrOut.toString())
        p0?.writeString(if(comment == null) "" else comment)*/
/*        p0?.writeString(if(activity == null) "" else activity)
        p0?.writeString(if(source == null) "" else source)
        p0?.writeString(if(decPoint==null) "0" else decPoint.toString())
        p0?.writeString(if(category == null) "" else category)*/
        p0?.writeString(title)
        p0?.writeString(value.toString())
        p0?.writeString(created)
        p0?.writeString(subTitle)
        p0?.writeInt(inOrOut)
        p0?.writeString(comment)
        p0?.writeString(uuid)
        p0?.writeString(address)
        p0?.writeString(remark)
        p0?.writeString(txHash)
        p0?.writeInt(txResult)
        p0?.writeString(txNetwork)
    }
    override fun describeContents(): Int {
        return 0
    }

    constructor(source: Parcel) : this(source.readString(), source.readString().toBigDecimal(), source.readString(), source.readString(),source.readInt(),source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),source.readInt(),source.readString())

   // constructor(title:String, value:BigDecimal,  created:String,  subTitle:String, inOrOut:Int, comment:String):this(title,value,created,subTitle,inOrOut,comment)

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PointItem> = object : Parcelable.Creator<PointItem> {
            override fun createFromParcel(source: Parcel): PointItem {
                return PointItem(source)
            }

            override fun newArray(size: Int): Array<PointItem?> {
                return arrayOfNulls(size)
            }
        }
    }
}
data class InquirePointsDetailResp (val pageNum:Int, val pageSize:Int, val rows:ArrayList<PointItem>)


 data class EnergyItem(val source:String?, val value:BigDecimal?, val created:String?, val activity:String?, val decPoint:BigDecimal?,val category:String?,val comment:String?,val inOrOut:Int) : Parcelable{
    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(source)
        p0?.writeString(value.toString())
        p0?.writeString(created)
        p0?.writeString(activity)
        p0?.writeString(decPoint.toString())
        p0?.writeString(category)
        p0?.writeString(comment)
        p0?.writeInt(inOrOut)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(source: Parcel) : this(source.readString(), source.readString().toBigDecimal(), source.readString(), source.readString(),source.readString().toBigDecimal(),source.readString(),source.readString(),source.readInt())

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<EnergyItem> = object : Parcelable.Creator<EnergyItem> {
            override fun createFromParcel(source: Parcel): EnergyItem {
                return EnergyItem(source)
            }

            override fun newArray(size: Int): Array<EnergyItem?> {
                return arrayOfNulls(size)
            }
        }
    }
}
data class InquireEnergyDetailResp (val pageNum:Int, val pageSize:Int, val rows:ArrayList<EnergyItem>)
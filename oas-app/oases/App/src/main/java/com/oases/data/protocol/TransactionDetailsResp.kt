package com.oases.data.protocol

import android.os.Parcel
import android.os.Parcelable

data class TransactionItem(val scope:String, val name:String, val created:String, val value:String)
data class TransactionDetailsResp(val name:String,
                                  val point:String,
                                  val date:String,
                                  val action:String)

/**
 * 交易记录data
 */
data class TransactionListItems(val title:String, val value:Int, val created:String, val subTitle:String,val inOrOut:Int) : Parcelable {
    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(title)
        p0?.writeInt(value)
        p0?.writeString(created)
        p0?.writeString(subTitle)
        p0?.writeInt(inOrOut)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(source: Parcel) : this(source.readString(), source.readInt(), source.readString(), source.readString(),source.readInt())

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<TransactionListItems> = object : Parcelable.Creator<TransactionListItems> {
            override fun createFromParcel(source: Parcel): TransactionListItems {
                return TransactionListItems(source)
            }

            override fun newArray(size: Int): Array<TransactionListItems?> {
                return arrayOfNulls(size)
            }
        }
    }
}
data class TransactionDetailListResp (val pageNum:Int, val pageSize:Int, val rows:ArrayList<TransactionListItems>)
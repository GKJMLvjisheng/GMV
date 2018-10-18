package com.oases.data.protocol

import android.os.Parcel
import android.os.Parcelable
import java.math.BigInteger

data class CoinItem(val name:String, val balance:Double, val value:Double,val address:String,val contract:String,val ethBalance:Double,val unconfirmedBalance:Double)/*: Parcelable {
    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(name)
        p0?.writeDouble(balance)
        p0?.writeDouble(value)
        p0?.writeString(address)
        p0?.writeString(contract)
        p0?.writeDouble(ethBalance)
        p0?.writeDouble(unconfirmedBalance)
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun describeContents(): Int {
      return 0
    }

    override fun toString(): String {
        return super.toString()
    }
    constructor(source: Parcel) : this(source.readString(), source.readDouble(), source.readDouble(), source.readString(),source.readString(),source.readDouble(),source.readDouble())

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<CoinItem> = object : Parcelable.Creator<CoinItem> {
            override fun createFromParcel(source: Parcel): CoinItem {
                return CoinItem(source)
            }

            override fun newArray(size: Int): Array<CoinItem?> {
                return arrayOfNulls(size)
            }
        }
    }
}*///,
//data class ListCoinResp (val data: Array<CoinItem>)
//typealias ListCoinResp =  Array<CoinItem>
data class ListCoinResp(val userCoin:Array<CoinItem>,val noShowCoin:Array<CoinItem>)
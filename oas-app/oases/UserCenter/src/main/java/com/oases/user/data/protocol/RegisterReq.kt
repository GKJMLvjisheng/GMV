package com.oases.user.data.protocol

/**
 * Created by Bingo Zhu
 * 2018/8/14
 */
data class RegisterReq(val name:String, val password:String, val inviteFrom:String, val imei: String)
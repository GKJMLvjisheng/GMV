package com.oases.user.data.protocol

import java.io.File

data class UserInfo (val userId:String,
                     val token:String,
                     val name:String,
                     val profile:String,
                     val nickname:String,
                     val gender:String,
                     val address:String,
                     val birthday:String,
                     val mobile:String,
                     val email:String,
                     val inviteCode:String)
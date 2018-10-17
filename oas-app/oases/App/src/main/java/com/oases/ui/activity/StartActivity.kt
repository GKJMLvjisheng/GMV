package com.oases.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.oases.base.common.BaseConstant.Companion.USER_TOKEN
import com.oases.base.utils.AppPrefsUtils
import com.oases.provider.common.isLogined
import com.oases.user.ui.activity.LoginActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult

class StartActivity : AppCompatActivity() {
    val LoginResultRequestCode:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("zbb", "StartActivity on create")
        AppPrefsUtils.putString(USER_TOKEN, "")
        if(isLogined()){
            Log.d("zbb", "User is logined")
            startActivity<MainActivity>()
            finish()
        }
        else{
            startActivityForResult<LoginActivity>(LoginResultRequestCode)
        }
    }

    private fun checkLoginResult(resultCode: Int){
        when (resultCode){
            Activity.RESULT_OK -> if (isLogined()) {
                startActivity<MainActivity>()
            }
        }
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            LoginResultRequestCode -> checkLoginResult(resultCode)
        }
    }
}

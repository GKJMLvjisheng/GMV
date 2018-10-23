package com.oases.user.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.util.Log
import com.lahm.library.EasyProtectorLib
import com.oases.user.BuildConfig
import org.jetbrains.anko.toast

fun requestPhoneStatePermission(activity: Activity){
    if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_PHONE_STATE), 2)
    }
}
@SuppressWarnings("deprecated")
fun getDeviceId(activity: Activity): String {
    var ImeiNumber = ""
    if (EasyProtectorLib.checkIsDebug(activity)
        && EasyProtectorLib.checkIsRunningInEmulator()){
        return ImeiNumber;
    }
    requestPhoneStatePermission(activity)
    if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
        val telephonyManager = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            ImeiNumber = telephonyManager.imei
            Log.d("zbb", "get imei $ImeiNumber")
            if (ImeiNumber == null){
                ImeiNumber = telephonyManager.meid
            }
        }
        else {
            @SuppressWarnings("deprecated")
            ImeiNumber = telephonyManager.getDeviceId()
        }
    }
    else{
        activity.toast("需要读取本机识别码保证账户安全")
    }

    return ImeiNumber
}
package com.oases.user.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.oases.user.R
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.webkit.*
import android.widget.Toast
import com.darsh.multipleimageselect.helpers.Constants
import com.oases.base.common.BaseConstant
import com.oases.base.utils.AppPrefsUtils


class HelpAndFeedbackActivity : AppCompatActivity() {

    lateinit var myWebHelp: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_and_feedback)
        requestPermission()
        myWebHelp = findViewById(R.id.mDescription)
        myWebHelp.settings.setAppCacheEnabled(true)   //启用appCache
        myWebHelp.settings.domStorageEnabled = true
        myWebHelp.settings.databaseEnabled = true
        myWebHelp.settings.blockNetworkImage = false
        myWebHelp.settings.loadsImagesAutomatically = true
        myWebHelp.settings?.javaScriptEnabled = true
        myWebHelp.settings?.allowUniversalAccessFromFileURLs = true
        myWebHelp.settings?.allowContentAccess = true
        myWebHelp.settings?.cacheMode = WebSettings.LOAD_DEFAULT
        myWebHelp.settings?.textZoom = 100
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            myWebHelp.settings?.mixedContentMode=WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }
        val url = BaseConstant.HELP_FEEDBACK_DESCRIPTION
        myWebHelp.loadUrl(url)
        myWebHelp.webViewClient = WebViewClient()
        myWebHelp.webChromeClient = WebChromeClient()

    }


    private fun loadWeb(){

    }

private fun initView(){
    val uri = Uri.parse("https://mp.weixin.qq.com/s/6ab2dHLCekLJlq21p-TiPg")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(intent)
}

    //获取读写权限
    private fun requestPermission() {
        val checkWritePermission = ActivityCompat.checkSelfPermission(this@HelpAndFeedbackActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkWritePermission== PackageManager.PERMISSION_GRANTED) {
            //initView()
        } else {
            //requset permission
            ActivityCompat.requestPermissions(this@HelpAndFeedbackActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
               // initView()
            } else{
                //permission denied
                Toast.makeText(this@HelpAndFeedbackActivity,"permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



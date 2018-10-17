package com.oases.ui.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.oases.R
import com.oases.base.common.BaseConstant
import com.oases.base.ui.activity.BaseActivity
import com.oases.base.utils.AppPrefsUtils
import kotlinx.android.synthetic.main.activity_wallet_in.*
import com.google.zxing.WriterException
import com.yzq.zxinglibrary.encode.CodeCreator
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.text.TextUtils
import android.widget.Toast
import android.util.DisplayMetrics
import android.content.Context.WINDOW_SERVICE
import android.view.WindowManager
import com.oases.base.ext.onClick
import kotlinx.android.synthetic.main.activity_wallet_out.*
import org.jetbrains.anko.startActivity
import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Canvas
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import android.app.ActivityManager
import com.oases.base.common.BaseApplication.Companion.context
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop



class WalletInActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_in)

        mWalletInSend.setRightTopText(AppPrefsUtils.getString(BaseConstant.USER_NAME))

        generateQrCode()

        walletInHead!!.onClickRightTv({
            //startActivity<WalletOutActivity>()
            startActivity(intentFor<WalletOutActivity>().singleTop().clearTop())
            /*this.finish()
            ARouter.getInstance().build("/app/WalletOutActivity").navigation()*/
        })


        mCopyBtn.onClick {
            //获取剪贴板管理器：
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText("Label", mWalletInSend.getRightTopText().toString())
            // 将ClipData内容放到系统剪贴板里。
            if(cm!=null){
                cm.setPrimaryClip(mClipData)
            }

        }

    }

    private fun generateQrCode(){
        val contentEtString:String = mWalletInSend.getRightTopText()

        if (TextUtils.isEmpty(contentEtString)) {
            Toast.makeText(this, "contentEtString不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        var bitmap: Bitmap
        try {
            /*
                    * contentEtString：字符串内容
                    * w：图片的宽
                    * h：图片的高
                    * logo：不需要logo的话直接传null
                    * */

            //val logo = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            wm.defaultDisplay.getMetrics(dm)
            val width = dm.widthPixels.toInt()         // 屏幕宽度（像素）
            bitmap = CodeCreator.createQRCode(contentEtString, width/2, width/2, null)
            if(bitmap==null){
                Toast.makeText(this, "二维码生成失败", Toast.LENGTH_SHORT).show()
                return
            }

            qrCodeIn.setImageBitmap(bitmap)
            qrCodeIn.setPadding(width/2,100,width/2,100)
           // qrCodeIn.set

        } catch (e: WriterException) {
            e.printStackTrace()
        }

    }

}
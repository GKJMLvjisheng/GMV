package com.oases.computingpower.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.darsh.multipleimageselect.helpers.Constants
import com.google.zxing.WriterException
import com.jph.takephoto.uitl.TImageFiles
import com.oases.base.common.BaseConstant
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.computingpower.R
import com.oases.computingpower.data.protocol.InviteFriendsInfoResp
import com.oases.computingpower.injection.component.DaggerComputingPowerComponent
import com.oases.computingpower.injection.module.ComputingPowerModule
import com.oases.computingpower.presenter.InviteFriendsInfoPresenter
import com.oases.computingpower.presenter.view.InviteFriendsInfoView
import com.yzq.zxinglibrary.encode.CodeCreator
import kotlinx.android.synthetic.main.activity_invite_friends.*
import java.io.File

class InviteFriendsActivity : BaseMvpActivity<InviteFriendsInfoPresenter>(), InviteFriendsInfoView {


    private lateinit var mTempFile:File
    private var upLoadAppUrl = AppPrefsUtils.getString(BaseConstant.PACKAGE_URL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_friends)
        initView()
    }

    fun initView(){

        mPresenter.getInviteFriendsInfo()
        mInviteNumberTv.text = AppPrefsUtils.getString(BaseConstant.USER_INVITE_CODE)
        generateQrCode()
        mHeadBar.onClickRightTv {
            requestPermission()
        }
    }

    //生成二维码图片
    private fun generateQrCode(){
        val contentEtString:String = upLoadAppUrl

        var bitmap: Bitmap? = null
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
            val width = dm.widthPixels       // 屏幕宽度（像素）
            bitmap = CodeCreator.createQRCode(contentEtString, width/2, width/2, null)
            if(bitmap==null){
                Toast.makeText(this, "二维码生成失败", Toast.LENGTH_SHORT).show()
                return
            }

            mInviteQrCode.setImageBitmap(bitmap)
            mInviteQrCode.setPadding(width/2,100,width/2,0)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    //创建路径存放图片
    private fun createTempFile(){
        val  mTempFileName = "picture"
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        mTempFile = File.createTempFile(
                mTempFileName,
                ".jpg",
                storageDir)
    }

    //获取全屏并进行截屏
    private fun saveCurrentQrCodeImage() {
        //获取当前屏幕的大小
        val width = window.decorView.rootView.width
        val height = window.decorView.rootView.height
        //val width = mQrCodeImage.width
        //val height = mQrCodeImage.height
        //生成相同大小的图片
        var temBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        //找到当前页面的跟布局
        val view = window.decorView.rootView
        //设置缓存
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        //从缓存中获取当前屏幕的图片
        temBitmap = view.drawingCache
        if (temBitmap != null) {
            //获取分享图片的尺寸
            var outWidth = mQrCodeImage.width
            var outHeight = mQrCodeImage.height
            //获取坐标进行截屏
            var loc = intArrayOf(0, 0)
            mQrCodeImage.getLocationOnScreen(loc)
            temBitmap = Bitmap.createBitmap(temBitmap,loc[0],loc[1],outWidth,outHeight)

        }

        //输出到sd卡
        TImageFiles.writeToFile(temBitmap, Uri.fromFile(mTempFile))
    }


    //分享图片
    private fun shareImage(){
        val sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mTempFile))
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        sendIntent.setType("image/*")

        startActivity(Intent.createChooser(sendIntent,"分享二维码图片给好友"))
    }

    //获取读写权限
    private fun requestPermission() {
        val checkWritePermission = ActivityCompat.checkSelfPermission(this@InviteFriendsActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkWritePermission== PackageManager.PERMISSION_GRANTED) {
            createTempFile()
            saveCurrentQrCodeImage()
            shareImage()
        } else {
            //requset permission
            ActivityCompat.requestPermissions(this@InviteFriendsActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                createTempFile()
                saveCurrentQrCodeImage()
                shareImage()
            } else{
                //permission denied
                Toast.makeText(this@InviteFriendsActivity,"permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun injectComponent() {
        DaggerComputingPowerComponent
                .builder()
                .activityComponent(mActivityComponent)
                .computingPowerModule(ComputingPowerModule())
                .build()
                .inject(this)
        Log.d("lh", "InviteFriendsInfo injected")
        mPresenter.mView = this
    }

    override fun onInviteFriendsInfoResult(result: InviteFriendsInfoResp) {
        mInviteNumber.setValue(result.sumUserInvited.toString())
        mTotalLift.setValue(result.sumPowerPromoted)
    }
}

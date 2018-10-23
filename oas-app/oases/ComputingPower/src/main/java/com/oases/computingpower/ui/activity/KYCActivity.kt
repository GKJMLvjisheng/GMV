package com.oases.computingpower.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.darsh.multipleimageselect.helpers.Constants
import com.jph.takephoto.app.TakePhoto
import com.jph.takephoto.app.TakePhotoImpl
import com.jph.takephoto.compress.CompressConfig
import com.jph.takephoto.model.CropOptions
import com.jph.takephoto.model.TResult
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.computingpower.R
import com.oases.computingpower.data.protocol.KYCInfoResp
import com.oases.computingpower.data.protocol.KYCVerifyStatusResp
import com.oases.computingpower.injection.component.DaggerComputingPowerComponent
import com.oases.computingpower.injection.module.ComputingPowerModule
import com.oases.computingpower.presenter.KYCPresenter
import com.oases.computingpower.presenter.view.KYCView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_kyc.*
import kotlinx.android.synthetic.main.activity_watching_wechat_public_account.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.toast
import q.rorbin.badgeview.QBadgeView
import java.io.File

@Route(path = "/computingPower/KYCActivity")
class KYCActivity : BaseMvpActivity<KYCPresenter>(), KYCView,TakePhoto.TakeResultListener,ActivityCompat.OnRequestPermissionsResultCallback {

    var selectPosition:Int =1       //选择拍照还是相册，拍照为0
    private lateinit var mTakePhoto: TakePhoto
    private lateinit var mTempFile:File     //临时文件
    private lateinit var mCompressFile:File  //压缩文件
    private lateinit var mFinalTempFileName:String  //文件的名字
    var mIDPosition:Int =0      //身份证照片的位置 1：正面 2：背面 3：手持身份证和手写OAS账号
    var serverAddress:String = BaseConstant.SERVER_ADDRESS+"null"   //图片路径，没有图片时，返回的是服务器路径+null
    var frontOfId:String = ""    //照片路径，回显得到的  用于点击提交时，查看是否都存在三张照片
    var backOfId:String = ""
    var handOfId:String = ""

    var KYCVerifyStatus: Int =0

    var frontChangeUri:String = ""    //用户进行上传照片之后的路径
    var backChangeUri:String = ""
    var handChangeUri:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kyc)

        mTakePhoto = TakePhotoImpl(this,this)
        mTakePhoto.onCreate(savedInstanceState)
        initView()
    }

    fun initView(){

        //进入页面去后台读取KYC认证结果以及身份证图片信息
        mPresenter.getKYCVerifyStatus()

        //上传身份证正面
        mFrontID.onClick {
            if (KYCVerifyStatus==0||KYCVerifyStatus==3) {
                var IDPosition = 1
                mIDPosition = IDPosition
                showAlertView()
            }else if(KYCVerifyStatus==1){
                toast("你的KYC认证正在审核中，暂不能上传图片")
            }else if (KYCVerifyStatus==2){
                toast("你的KYC认证已通过，不能重复上传图片")
            }
        }

        //上传身份证背面
        mBackID.onClick {
            if (KYCVerifyStatus==0||KYCVerifyStatus==3) {
                var IDPosition = 2
                mIDPosition = IDPosition
                showAlertView()
            }else if(KYCVerifyStatus==1){
                toast("你的KYC认证正在审核中，暂不能上传图片")
            }else if (KYCVerifyStatus==2){
                toast("你的KYC认证已通过，不能重复上传图片")
            }
        }

        //上传手持身份证和手写OAS账号
        mHandIDCounter.onClick {
            if (KYCVerifyStatus==0||KYCVerifyStatus==3) {
                var IDPosition = 3
                mIDPosition = IDPosition
                showAlertView()
            }else if(KYCVerifyStatus==1){
                toast("你的KYC认证正在审核中，暂不能上传图片")
            }else if (KYCVerifyStatus==2){
                toast("你的KYC认证已通过，不能重复上传图片")
            }
        }

        //看三张照片是否都已存在，存在进行更改状态
        mSubmitKYC.onClick {
            if (KYCVerifyStatus==0||KYCVerifyStatus==3) {
               // if (frontOfId != serverAddress && backOfId != serverAddress && handOfId != serverAddress){
                    if (frontChangeUri == "" && backChangeUri=="" && handChangeUri=="") {
                        Log.d("qqqqq", frontOfId.length.toString())
                        toast("请上传图片后再提交")
                    }else if (frontOfId != serverAddress && backOfId != serverAddress && handOfId != serverAddress){
                        var frontIdUri = frontOfId.substring(serverAddress.length-4, frontOfId.length)
                        var backIdUri = backOfId.substring(serverAddress.length-4, backOfId.length)
                        var handIdUri = handOfId.substring(serverAddress.length-4, handOfId.length)
                        mPresenter.changeKYCStatus(frontIdUri, backIdUri, handIdUri)
                    }
                //}
                else{
                    toast("您上传的照片不全")
                }
            }else if(KYCVerifyStatus==1){
                toast("你的KYC认证正在审核中，暂不能提交")
            }else if (KYCVerifyStatus==2){
                toast("你的KYC认证已通过，不能重复提交")
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
        Log.d("lh", "KYC injected")
        mPresenter.mView = this
    }

    //选择图片弹窗
    private fun showAlertView() {
        AlertView("选择图片","","取消",null, arrayOf("拍照","相册"),this, AlertView.Style.ActionSheet,object : OnItemClickListener {
            override fun onItemClick(o: Any?, position: Int) {
                selectPosition = position
                mTakePhoto.onEnableCompress(CompressConfig.ofDefaultConfig(),false)  //图片压缩
                when(position){
                    0 ->  requestPermission()
                    1 -> requestPermission()
                }
            }
        }
        ).show()
    }

    //创建目录存放图片
    private fun createTempFile(){
        var mTempFileName = ""
        if (mIDPosition ==1){
        var  fileName = "face"
            mTempFileName = fileName
        }
        if (mIDPosition ==2){
            var  fileName = "back"
            mTempFileName = fileName
        }
        if (mIDPosition ==3){
            var  fileName = "hand"
            mTempFileName = fileName
        }
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        mTempFile = File.createTempFile(
                mTempFileName,
                ".jpg",
                storageDir)
        val tempFileName = mTempFile.name
        mFinalTempFileName = tempFileName
    }

    //获取相机权限
    fun requestPermission() {
        val checkCameraPermission = ActivityCompat.checkSelfPermission(this@KYCActivity, Manifest.permission.CAMERA)
        val checkWritePermission = ActivityCompat.checkSelfPermission(this@KYCActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkCameraPermission == PackageManager.PERMISSION_GRANTED && checkWritePermission== PackageManager.PERMISSION_GRANTED) {
            if(selectPosition==0) {
                createTempFile()
                //mTakePhoto.onPickFromCapture(Uri.fromFile(mTempFile))      //实现拍照
                var cropOptions: CropOptions = CropOptions.Builder().setAspectX(16).setAspectY(10).setWithOwnCrop(true).create()   //实现拍照+剪切
                mTakePhoto.onPickFromCaptureWithCrop(Uri.fromFile(mTempFile), cropOptions)     //实现拍照+剪切
            }else{
                //mTakePhoto.onPickFromGallery()
                createTempFile()
                var cropOptions: CropOptions = CropOptions.Builder().setAspectX(16).setAspectY(10).setWithOwnCrop(true).create()
                mTakePhoto.onPickFromGalleryWithCrop(Uri.fromFile(mTempFile), cropOptions)
            }
        } else {
            //requset permission
            ActivityCompat.requestPermissions(this@KYCActivity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0] == Manifest.permission.CAMERA && permissions[1] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                if(selectPosition==0) {
                    createTempFile()
                    //mTakePhoto.onPickFromCapture(Uri.fromFile(mTempFile))      //实现拍照
                    var cropOptions: CropOptions = CropOptions.Builder().setAspectX(16).setAspectY(10).setWithOwnCrop(true).create()   //实现拍照+剪切
                    mTakePhoto.onPickFromCaptureWithCrop(Uri.fromFile(mTempFile), cropOptions)     //实现拍照+剪切
                }else{
                    //mTakePhoto.onPickFromGallery()
                    createTempFile()
                    var cropOptions: CropOptions = CropOptions.Builder().setAspectX(16).setAspectY(10).setWithOwnCrop(true).create()
                    mTakePhoto.onPickFromGalleryWithCrop(Uri.fromFile(mTempFile), cropOptions)
                }
            } else{
                //permission denied
                Toast.makeText(this@KYCActivity,"permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun takeSuccess(result: TResult?) {
        Log.d("TakenPhoto",result?.image?.originalPath)
        Log.d("TakenPhoto",result?.image?.compressPath)
        mCompressFile = File(result?.image?.compressPath)
        val requestFile = RequestBody.create(MediaType.parse("png"), mCompressFile)
        val body = MultipartBody.Part.createFormData("file",mFinalTempFileName,requestFile)
        /*if (mIDPosition ==1){
            mFrontID.setImageURI(Uri.fromFile(mCompressFile))
        }
        if (mIDPosition ==2){
            mBackID.setImageURI(Uri.fromFile(mCompressFile))
        }
        if (mIDPosition ==3){
            mHandIDCounter.setImageURI(Uri.fromFile(mCompressFile))
        }*/
        mPresenter.upLoadKYCInfo(body)

    }

    override fun takeCancel() {

    }

    override fun takeFail(result: TResult?, msg: String?) {
        Log.e("TakePhoto",msg)
    }

    //得到图片
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mTakePhoto.onActivityResult(requestCode,resultCode,data)
    }

    override fun onUpLoadKYCInfoResult(result: KYCInfoResp) {
        if (mIDPosition ==1){
            var frontId = result.frontOfPhoto
            frontChangeUri = frontId
            frontOfId = frontId
            Picasso.get().load(result.frontOfPhoto)
                    .into(mFrontID)
            toast("上传成功")
           // mKYCVerifyInfo.setText("KYC认证等待审核")
            QBadgeView(this).bindTarget(mSpace).setBadgeText("!").setBadgeGravity(Gravity.START or Gravity.CENTER)
                    .setBadgeBackgroundColor(-0x14c5)   //黄色
        }
        if (mIDPosition ==2){
            var backId = result.backOfPhoto
            backChangeUri = backId
            backOfId = backId
            Picasso.get().load(result.backOfPhoto)
                    .into(mBackID)
            toast("上传成功")
          //  mKYCVerifyInfo.setText("KYC认证等待审核")
            QBadgeView(this).bindTarget(mSpace).setBadgeText("!").setBadgeGravity(Gravity.START or Gravity.CENTER)
                    .setBadgeBackgroundColor(-0x14c5)   //黄色
        }
        if (mIDPosition ==3){
            var handId = result.holdInHand
            handChangeUri = handId
            handOfId = handId
            Picasso.get().load(result.holdInHand)
                    .into(mHandIDCounter)
            toast("上传成功")
          //  mKYCVerifyInfo.setText("KYC认证等待审核")
            QBadgeView(this).bindTarget(mSpace).setBadgeText("!").setBadgeGravity(Gravity.START or Gravity.CENTER)
                    .setBadgeBackgroundColor(-0x14c5)   //黄色
        }
    }

    //用户的KYC状态结果
    override fun onGetKYCVerifyStatus(result: KYCVerifyStatusResp) {
        KYCVerifyStatus = result.verifyStatus
        if (result.verifyStatus ==0){
                mKYCVerifyInfo.setText("KYC未认证")
            //在认证信息前面打钩，因为直接对mKYCVerifyInfo打钩，是打在文字内部，所以在文字前面加入空格，xml文件中的id为mSpace
            QBadgeView(this).bindTarget(mSpace).setBadgeText("!").setBadgeGravity(Gravity.START or Gravity.CENTER)
                    .setBadgeBackgroundColor(-0x14c5)   //黄色
        }
        if (result.verifyStatus ==1){
            mKYCVerifyInfo.setText("KYC认证正在审核中...")
            QBadgeView(this).bindTarget(mSpace).setBadgeText("!").setBadgeGravity(Gravity.START or Gravity.CENTER)
                    .setBadgeBackgroundColor(-0x14c5)  //黄色

        }
        if (result.verifyStatus ==2){
            mKYCVerifyInfo.setText("KYC认证审核成功")
            QBadgeView(this).bindTarget(mSpace).setBadgeText("√").setBadgeGravity(Gravity.START or Gravity.CENTER)
                    .setBadgeBackgroundColor(-0x743cb6)  //绿色
        }
        if (result.verifyStatus ==3){
            mKYCVerifyInfo.setText("KYC认证审核未通过："+result.remark)
            QBadgeView(this).bindTarget(mSpace).setBadgeText("!").setBadgeGravity(Gravity.START or Gravity.CENTER)
                    .setBadgeBackgroundColor(-0x14c5)   //黄色
        }
        //读取身份证正面
        if (result.frontOfPhoto ==serverAddress){
            frontOfId = result.frontOfPhoto
        }else{
            Picasso.get().load(result.frontOfPhoto)
                    .into(mFrontID)
            frontOfId = result.frontOfPhoto
        }
        //读取身份证背面
        if (result.backOfPhoto ==serverAddress){
            backOfId = result.backOfPhoto
        }else{
            Picasso.get().load(result.backOfPhoto)
                    .into(mBackID)
            backOfId = result.backOfPhoto
        }
        //读取手持身份证和手写OAS账号
        if (result.holdInHand ==serverAddress){
            handOfId = result.holdInHand
        }else{
            Picasso.get().load(result.holdInHand)
                    .into(mHandIDCounter)
            handOfId = result.holdInHand
        }
    }

    //用户点击提交后更改KYC状态后台返回的结果
    override fun onChangeKYCStatus(result: Int) {
        if (result == 0){
            toast("提交成功")
        }
    }

}

package com.oases.user.ui.activity

import android.Manifest
import android.app.DatePickerDialog
import android.app.Fragment
import android.app.FragmentManager
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.jph.takephoto.app.TakePhoto
import com.jph.takephoto.app.TakePhotoImpl
import com.jph.takephoto.compress.CompressConfig
import com.jph.takephoto.model.TResult
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ui.activity.BaseMvpActivity
import com.oases.base.utils.AppPrefsUtils
import com.oases.user.R
import com.oases.user.data.protocol.UserInfo
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.UserInfoPresenter
import com.oases.user.presenter.view.UserInfoView
import kotlinx.android.synthetic.main.activity_user_info.*
import java.io.File
import android.widget.Toast
import com.darsh.multipleimageselect.helpers.Constants.REQUEST_CODE
import com.jph.takephoto.model.CropOptions
import com.oases.user.R.drawable.icon_default_user
import com.oases.user.data.protocol.uploadImgResp
import com.oases.user.injection.component.DaggerUserComponent
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


class UserInfoActivity : BaseMvpActivity<UserInfoPresenter>(), UserInfoView,TakePhoto.TakeResultListener,ActivityCompat.OnRequestPermissionsResultCallback{

    var sexValue :String=""
    var selectPosition:Int =1
    private val phoneJumpFlag: String = "fromUserInfo"
    private val mailJumpFlag: String = "fromUserInfo"
    private lateinit var mTakePhoto:TakePhoto
    private lateinit var mTempFile:File
    private lateinit var mCompressFile:File
    private lateinit var mFinalTempFileName:String
    private var serverAddress:String = ""
    var bitmap:Bitmap? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        mTakePhoto = TakePhotoImpl(this,this)
        mTakePhoto.onCreate(savedInstanceState)
        initView()
    }

    private fun setView(){
        mUserNameTv.text = AppPrefsUtils.getString(BaseConstant.USER_NAME)
        //Picasso.get().load("http://18.219.19.160:8080/image/profile/33ca282980e8444daa9b63f164f96f14-1536986886766.png")
        //        .into(mUserIconIv)
        serverAddress =BaseConstant.SERVER_ADDRESS+"null"
        if (AppPrefsUtils.getString(BaseConstant.USER_ICON)==serverAddress||AppPrefsUtils.getString(BaseConstant.USER_ICON)==""){
            mUserIconIv.setImageResource(R.drawable.icon_default_user)
                }else{
        Picasso.get().load(AppPrefsUtils.getString(BaseConstant.USER_ICON))
                .into(mUserIconIv)
        Log.d("ffff",AppPrefsUtils.getString(BaseConstant.USER_ICON))
        }
        mNickName.setValue(AppPrefsUtils.getString(BaseConstant.USER_NICK_NAME))
        mGender.setValue(AppPrefsUtils.getString(BaseConstant.USER_GENDER))
        mAddress.setValue(AppPrefsUtils.getString(BaseConstant.USER_ADDRESS))
        mBirthday.setValue(AppPrefsUtils.getString(BaseConstant.USER_BIRTHDAY))
        mPhoneNumber.setValue(AppPrefsUtils.getString(BaseConstant.USER_PHONE_NUMBER))
        mMailAddress.setValue(AppPrefsUtils.getString(BaseConstant.USER_MAIL_ADDRESS))
    }

    private fun initView() {

        setView()
        mUserIconIv.onClick {
            showAlertView()
        }

        mNickName.onClick{
            intentNickName ()
        }

        mGender.onClick{
            showSexChooseDialog()
        }

        mAddress.onClick{
            startActivity<AddressActivity>()
        }

        mPhoneNumber.onClick {
            var intent = Intent(this@UserInfoActivity,PhoneStepOneActivity::class.java)
            var bundle = Bundle()
            bundle.putString("originalPhoneNumber",mPhoneNumber.getValue())
            bundle.putString("phoneJumpFlag",phoneJumpFlag)
            intent.putExtras(bundle)
            startActivity(intent)
            //startActivity<PhoneStepOneActivity>()
        }

        mMailAddress.onClick {
            var intent = Intent(this@UserInfoActivity,MailStepOneActivity::class.java)
            var bundle = Bundle()
            bundle.putString("originalMail",mMailAddress.getValue())
            bundle.putString("mailJumpFlag",mailJumpFlag)
            intent.putExtras(bundle)
            startActivity(intent)
            //startActivity<MailStepOneActivity>()
        }



        //设置生日
        mBirthday.onClick{
            if (mBirthday.getValue()=="未设置" || mBirthday.getValue()==""){
                mBirthday.setValue("1990-07-01")
            }
            val date = mBirthday.getValue().splitToSequence('-')
            Log.d("birthday",date.toString())
            val datePickerDialog = DatePickerDialog(
                    this,
                    DatePickerDialog.THEME_HOLO_LIGHT,
                    dateSetListener,
                    date.elementAt(0).toInt(),date.elementAt(1).toInt(),0)
            datePickerDialog.show()

        }


    }

    //修改昵称跳转传值
    private fun intentNickName (){
        var intent = Intent(this@UserInfoActivity,NickNameActivity::class.java)
        var bundle = Bundle()
        bundle.putString("NickName",mNickName.getValue())
        intent.putExtras(bundle)
        startActivity(intent)
    }

    //修改性别弹框
    private fun  showSexChooseDialog(){
        var  sexArry:Array<String> = arrayOf("男","女")
        var sex:String
        android.support.v7.app.AlertDialog.Builder(this).setItems(sexArry, DialogInterface.OnClickListener { _, i ->
            when (i) {
                0 -> {
                    sex ="男"
                    sexValue = sex
                    Log.d("ass",sex)
                    mPresenter.updateGender(sexValue)
                }
                1 -> {
                    sex ="女"
                    sexValue = sex
                    mPresenter.updateGender(sexValue)
                }
            }
        }).create().show()
    }


    override fun onResume() {
        super.onResume()
        setView()
    }

    //修改生日监听
    private var dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
        mBirthday.setValue(year.toString() + "-" + "%02d".format(month + 1)+"-"+"%02d".format(day))
        mBirthday.invalidate()
        mPresenter.updateBirthday(mBirthday.getValue())
    }

    //选择图片
    private fun showAlertView() {
        AlertView("选择图片","","取消",null, arrayOf("拍照","相册"),this,AlertView.Style.ActionSheet,object : OnItemClickListener{
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

    override fun injectComponent() {
        DaggerUserComponent
                .builder()
                .activityComponent(mActivityComponent)
                .userModule(UserModule())
                .build()
                .inject(this)
        mPresenter.mView = this
    }

    override fun onUpdateUserIconResult(result: uploadImgResp) {
        AppPrefsUtils.putString(BaseConstant.USER_ICON,result.profile)
        Picasso.get().load(AppPrefsUtils.getString(BaseConstant.USER_ICON))
                .into(mUserIconIv)
        toast("上传成功")
    }

    override fun onUpdateGenderResult(result: UserInfo) {
        AppPrefsUtils.putString(BaseConstant.USER_GENDER,sexValue)
        mGender.setValue(AppPrefsUtils.getString(BaseConstant.USER_GENDER))
        toast("保存成功")
    }

    override fun onUpdateBirthdayResult(result: UserInfo) {
        toast("修改成功")
    }

    override fun takeSuccess(result: TResult?) {
        Log.d("TakenPhoto",result?.image?.originalPath)
        Log.d("TakenPhoto",result?.image?.compressPath)
        //mCompressFile = File(result?.image?.originalPath)
        mCompressFile = File(result?.image?.compressPath)
        val requestFile = RequestBody.create(MediaType.parse("png"), mCompressFile)
       // val requestFile = RequestBody.create(MediaType.parse("png"), mTempFile)
        //val body = MultipartBody.Part.createFormData("file", "1536855220515.png", requestFile)
        val body = MultipartBody.Part.createFormData("file",mFinalTempFileName,requestFile)
        mPresenter.updateUserIcon(body)

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
        //bitmap = data!!.getParcelableExtra<Bitmap>("data")
        //xuanzhuan()
    }

    /*private fun xuanzhuan(){
        var matrix:Matrix =Matrix() //旋转图片 动作
        matrix.setRotate(90.toFloat())//旋转角度
        var width = bitmap!!.getWidth()
        var height = bitmap!!.getHeight(); // 创建新的图片
        var resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        mUserIconIv.setImageBitmap(resizedBitmap)
    }*/

    //获取相机权限
    fun requestPermission() {
        val checkCameraPermission = ActivityCompat.checkSelfPermission(this@UserInfoActivity, Manifest.permission.CAMERA)
        val checkReadPermission = ActivityCompat.checkSelfPermission(this@UserInfoActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
        val checkWritePermission = ActivityCompat.checkSelfPermission(this@UserInfoActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkCameraPermission == PackageManager.PERMISSION_GRANTED && checkReadPermission== PackageManager.PERMISSION_GRANTED && checkWritePermission== PackageManager.PERMISSION_GRANTED) {
            if(selectPosition==0) {
                createTempFile()
                //mTakePhoto.onPickFromCapture(Uri.fromFile(mTempFile))      //实现拍照
                var cropOptions: CropOptions = CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create()   //实现拍照+剪切
                mTakePhoto.onPickFromCaptureWithCrop(Uri.fromFile(mTempFile), cropOptions)     //实现拍照+剪切
            }else{
                //mTakePhoto.onPickFromGallery()
                createTempFile()
                var cropOptions: CropOptions = CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create()
                mTakePhoto.onPickFromGalleryWithCrop(Uri.fromFile(mTempFile), cropOptions)
            }
        } else {
            //requset permission
            ActivityCompat.requestPermissions(this@UserInfoActivity, arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0] == Manifest.permission.CAMERA && permissions[1] == Manifest.permission.READ_EXTERNAL_STORAGE && permissions[2] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                if(selectPosition==0) {
                    createTempFile()
                    //mTakePhoto.onPickFromCapture(Uri.fromFile(mTempFile))      //实现拍照
                    var cropOptions: CropOptions = CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create()   //实现拍照+剪切
                    mTakePhoto.onPickFromCaptureWithCrop(Uri.fromFile(mTempFile), cropOptions)     //实现拍照+剪切
                }else{
                    createTempFile()
                    //mTakePhoto.onPickFromGallery()
                    var cropOptions: CropOptions = CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create()
                    mTakePhoto.onPickFromGalleryWithCrop(Uri.fromFile(mTempFile), cropOptions)
                }
            } else{
                //permission denied
                Toast.makeText(this@UserInfoActivity,"permission denied",Toast.LENGTH_SHORT).show()
            }
        }
    }

    //实现拍照
    private fun createTempFile(){
        val  mTempFileName = "picture"
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        mTempFile = File.createTempFile(
                mTempFileName,
                ".jpg",
                storageDir)
        val tempFileName = mTempFile.name
        mFinalTempFileName = tempFileName
    }

}

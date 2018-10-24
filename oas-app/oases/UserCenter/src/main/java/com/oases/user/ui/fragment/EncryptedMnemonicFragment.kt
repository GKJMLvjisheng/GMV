package com.oases.user.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.darsh.multipleimageselect.helpers.Constants
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.utils.AppPrefsUtils

import com.oases.user.R
import kotlinx.android.synthetic.main.fragment_encrypted_mnemonic.*
import org.jetbrains.anko.support.v4.toast
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EncryptedMnemonicFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EncryptedMnemonicFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class EncryptedMnemonicFragment : Fragment(),ActivityCompat.OnRequestPermissionsResultCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var mEncryptUri:TextView
    private lateinit var mExportFile:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_encrypted_mnemonic, container, false)
        mEncryptUri = view.findViewById(R.id.mEncryptUri)
        mExportFile = view.findViewById(R.id.mExportFile)
        initView()
        return view
    }

    private fun isFileExist(){
    }

    private fun initView(){
            var userName= AppPrefsUtils.getString(BaseConstant.USER_NAME)
            //val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            //val storageDir = Environment.getExternalStorageDirectory()
            val parentFile = File(Environment.getExternalStorageDirectory().toString() + File.separator + "OASES" + File.separator +userName)

            if (!parentFile.exists()){
                parentFile.mkdirs()
            }
            val file = File(parentFile.toString() + "_encrypt_seed.txt")
            val saveUri = (file.path).substring(19,(file.path).length)
            if (!file.exists())
            {
                mEncryptUri.text=saveUri
            }else{
                //createTempFile()
                mEncryptUri.text=""
            }
        mExportFile.onClick {
            createTempFile()
            toast("导出成功")
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        mListener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EncryptedMnemonicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                EncryptedMnemonicFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


    //创建路径存放图片
    private fun createTempFile(){
        var userName= AppPrefsUtils.getString(BaseConstant.USER_NAME)
        //val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        //val storageDir = Environment.getExternalStorageDirectory()
        val parentFile = File(Environment.getExternalStorageDirectory().toString() + File.separator + "OASES" + File.separator +userName)

        if (!parentFile.exists()){
            parentFile.mkdirs()
        }
        val file = File(parentFile.toString() + "_encrypt_seed.txt")
        if (!file.exists()) {
            file.createNewFile()
        }
        val saveUri = (file.path).substring(19,(file.path).length)
        Log.d("lihui", "$file, ${file.isFile}, ${file.path}")
        file.writeText("hello world")
        mEncryptUri.text=saveUri
    }

    //获取读写权限

    /*private fun requestPermission(context:Context, func: ()->Unit) {
        val checkWritePermission = ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkWritePermission== PackageManager.PERMISSION_GRANTED) {
            createTempFile()

        } else {
            //requset permission
           // ActivityCompat.requestPermissions(context!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE)
            //this.requestPermission(context!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                createTempFile()
            } else{
                //permission denied
                Toast.makeText(context!!,"permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }*/


}

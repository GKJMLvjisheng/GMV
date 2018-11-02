package com.oases.user.ui.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.oases.base.common.BaseConstant.Companion.WALLET_BACKUP
import com.oases.base.ext.onClick
import com.oases.base.ui.fragment.BaseMvpFragment
import com.oases.base.utils.AppPrefsUtils

import com.oases.user.R
import com.oases.user.data.protocol.PrivateKeyMnemonicResp
import com.oases.user.injection.component.DaggerUserComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.presenter.PrivateKeyMnemonicPresenter
import com.oases.user.presenter.view.PrivateKeyMnemonicView
import kotlinx.android.synthetic.main.fragment_private_key_mnemonic.*
import org.jetbrains.anko.support.v4.toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PrivateKeyMnemonicFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PrivateKeyMnemonicFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PrivateKeyMnemonicFragment : BaseMvpFragment<PrivateKeyMnemonicPresenter>(), PrivateKeyMnemonicView {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var saveWalletBackup:Button
    private val sourceCode:String = "BACKUPWALLET"     //备份钱包
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        val view= inflater.inflate(R.layout.fragment_private_key_mnemonic, container, false)
        saveWalletBackup = view.findViewById(R.id.mSaveWalletBackup)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("lihui", "on view created")
        initView()
    }
    private fun initView(){
        mPresenter.getPrivateKeyMnemonic()
        saveWalletBackup.onClick {
            AppPrefsUtils.putBoolean(WALLET_BACKUP, true)
            toast("已确定您已保存")
            mPresenter.getReward(sourceCode)
        }
    }

    override fun injectComponent() {
        DaggerUserComponent
                .builder()
                .activityComponent(mActivityComponent)
                .userModule(UserModule())
                .build()
                .inject(this)
        mPresenter.mView = this
        Log.d("lihui", "injection")
    }

    override fun onGetPrivateKeyMnemonicResult(result: PrivateKeyMnemonicResp) {
        var mlist:String=""
        for (i in result.mnemonicList ){
            var mnemonicList = mlist.plus(i).plus(" ")
            mlist=mnemonicList
        }
        mPrivateKeyMnemonic.text=mlist
        mPrivateKey.text=result.privateKey
    }

    override fun onGetRewardResult(result: Int) {
        if (result ==0) {
            toast("活动参与成功,奖励只在第一次参与时获得哦！")
        }else{
            toast("活动参与失败")
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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
         * @return A new instance of fragment PrivateKeyMnemonicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                PrivateKeyMnemonicFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}

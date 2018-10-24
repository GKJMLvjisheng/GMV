package com.oases.ui.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.oases.R
import com.oases.base.common.BaseConstant
import com.oases.base.ext.onClick
import com.oases.base.ui.fragment.BaseMvpFragment
import com.oases.base.utils.AppPrefsUtils
import com.oases.data.protocol.ListCoinResp
import com.oases.data.protocol.TransitWalletSummary
import com.oases.injection.component.DaggerMainComponent
import com.oases.injection.module.WalletModule
import com.oases.presenter.TransitWalletPresenter
import com.oases.presenter.view.TransitWalletView
import com.oases.ui.activity.ExchangeCoinActivity
import kotlinx.android.synthetic.main.fragment_transit_wallet.*
import org.jetbrains.anko.support.v4.startActivity


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TransitWalletFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TransitWalletFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TransitWalletFragment : BaseMvpFragment<TransitWalletPresenter>(), TransitWalletView {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

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
        return inflater.inflate(R.layout.fragment_transit_wallet, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onTransitFragmentInteraction(uri)
    }
    override fun injectComponent() {
        DaggerMainComponent
                .builder()
                .activityComponent(mActivityComponent)
                .walletModule(WalletModule())
                .build()
                .inject(this)
        mPresenter.mView = this
    }

    override fun setCoin(coins: ListCoinResp) {
        val userCoinList = coins.userCoin
        val noShowCoinList = coins.noShowCoin
        mOas.setValue(userCoinList[0].balance)
        mOas.setEqualsValue(userCoinList[0].value)
        mEth.setValue(if(noShowCoinList!=null) noShowCoinList[0].balance else "0".toDouble())
        mEth.setEqualsValue(if(noShowCoinList!=null) noShowCoinList[0].value else "0".toDouble())
        AppPrefsUtils.putString(BaseConstant.MY_OAS_ADDRESS,userCoinList[0].address)
        AppPrefsUtils.putString(BaseConstant.MY_OAS_PROTOCOL,userCoinList[0].contract)
        //AppPrefsUtils.putString(BaseConstant.USER_OWN_ETH,userCoinList[0].ethBalance.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*        mPresenter.listCoin()
        mPresenter.summary()*/
        mOas.onClick {
            startActivity<ExchangeCoinActivity>()
            // startActivity(intentFor<ExchangeCoinActivity>().singleTop().clearTop())
             // ARouter.getInstance().build("/app/ExchangeCoinActivity").navigation()
                     /*  .withString("balance", mOas.getValue())
                       .withString("value",mOas.getEqualsValue())*/
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("zht","exchange on resume")
        mPresenter.listCoin()
        mPresenter.summary()

    }
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            Log.d("zht", "EXCHANGE, visible $isVisibleToUser")
            AppPrefsUtils.putString(BaseConstant.MORE_TYPE,"EXCHANGE")
        }
    }

    override fun setSummary(summary: TransitWalletSummary) {
        Log.d("zbb", "summary called")
        mSummary.text = summary.totalValue.toString()
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
        fun onTransitFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransitWalletFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                TransitWalletFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}

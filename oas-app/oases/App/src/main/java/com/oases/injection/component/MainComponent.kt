package com.oases.injection.component

import com.oases.base.injection.PerComponentScope
import com.oases.base.injection.component.ActivityComponent
import com.oases.injection.module.WalkModule
import com.oases.injection.module.WalletModule
import com.oases.ui.activity.*
import com.oases.ui.fragment.*
import dagger.Component

@PerComponentScope
@Component(dependencies = arrayOf(ActivityComponent::class), modules = arrayOf(WalletModule::class, WalkModule::class))
interface MainComponent {
    fun inject(fragment: MyPointsFragment)
    fun inject(fragment: WalletOnLineFragment)
    fun inject(fragment: TransitWalletFragment)
    fun inject(fragment: ExchangeItemFragment)
    fun inject(fragment: MeFragment)
    fun inject(activity: ExchangePointsActivity)
    fun inject(activity: RedrawOasActivity)
    fun inject(activity: WalletOutActivity)
    fun inject(activity: ExchangeDetailActivity)
    fun inject(activity: ExchangeOutActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: ExchangeCoinActivity)
    fun inject(activity: OasExchangeToOnlineActivity)
    fun inject(activity: ExchangeEthInActivity)

    //walk
    fun inject(fragment: HomeFragment)

}
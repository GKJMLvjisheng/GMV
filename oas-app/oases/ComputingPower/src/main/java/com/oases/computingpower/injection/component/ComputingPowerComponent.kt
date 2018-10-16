package com.oases.computingpower.injection.component

import com.oases.base.injection.PerComponentScope
import com.oases.base.injection.component.ActivityComponent
import com.oases.computingpower.injection.module.ComputingPowerModule
import com.oases.computingpower.ui.activity.*
import dagger.Component

@PerComponentScope
@Component(dependencies = arrayOf(ActivityComponent::class), modules = arrayOf(ComputingPowerModule::class))
interface ComputingPowerComponent {
    fun inject(activity: ComputingPowerMainActivity)
    fun inject(activity: WatchingWeChatPublicAccountActivity)
    fun inject(activity: InviteFriendsActivity)
    fun inject(activity: ComputingPowerHistoryActivity)
    fun inject(activity: KYCActivity)
    fun inject(activity: BuyingMinerActivity)
}
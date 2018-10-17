package com.oases.user.injection.component

import com.oases.base.injection.PerComponentScope
import com.oases.base.injection.component.ActivityComponent
import com.oases.user.injection.module.UserModule
import com.oases.user.ui.activity.*
import dagger.Component

/**
 * Created by Bingo Zhu
 * 2018/8/14
 */
@PerComponentScope
@Component(dependencies = arrayOf(ActivityComponent::class), modules = arrayOf(UserModule::class))
interface UserComponent {
    fun inject(activity:RegisterActivity)
    fun inject(activity:LoginActivity)
    fun inject(activity:ConfirmHelpWordsActivity)
    fun inject(activity:RegisterSucceedActivity)
    fun inject(activity: UserInfoActivity)
    fun inject(activity: NickNameActivity)
    fun inject(activity: AddressActivity)
    fun inject(activity: PhoneStepOneActivity)
    fun inject(activity: PhoneStepTwoActivity)
    fun inject(activity: DonePhoneActivity)
    fun inject(activity:MailStepOneActivity)
    fun inject(activity: MailStepTwoActivity)
    fun inject(activity: DoneMailActivity)
    fun inject(activity: ForgetPwdOneActivity)
    fun inject(activity: ForgetPwdTwoActivity)
    fun inject(activity: ResetPwdActivity)
}
package com.oases.user.injection.module

import com.oases.user.service.UserService
import com.oases.user.service.impl.UserServiceImpl
import dagger.Module
import dagger.Provides

/**
 * Created by Bingo Zhu
 * 2018/8/14
 */
@Module
class UserModule {
    @Provides
    fun provideUserService(userService: UserServiceImpl):UserService{
        return userService
    }
}
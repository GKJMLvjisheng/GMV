package com.oases.injection.module

import com.oases.service.WalkPointService
import com.oases.service.WalletService
import com.oases.service.impl.WalkPointServiceImpl
import com.oases.service.impl.WalletServiceImpl
import dagger.Module
import dagger.Provides

@Module
class WalkModule {
    @Provides
    fun provideWalkPointService(walkPointService: WalkPointServiceImpl): WalkPointService {
        return walkPointService
    }
}
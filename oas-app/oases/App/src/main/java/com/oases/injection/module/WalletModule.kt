package com.oases.injection.module

import com.oases.service.WalletService
import com.oases.service.impl.WalletServiceImpl
import dagger.Module
import dagger.Provides

@Module
class WalletModule {
    @Provides
    fun provideWalletService(walletService: WalletServiceImpl): WalletService {
        return walletService
    }
}
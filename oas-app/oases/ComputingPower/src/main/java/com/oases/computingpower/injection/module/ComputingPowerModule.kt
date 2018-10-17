package com.oases.computingpower.injection.module

import com.oases.computingpower.service.ComputingPowerService
import com.oases.computingpower.service.impl.ComputingPowerServiceImpl
import dagger.Module
import dagger.Provides

@Module
class ComputingPowerModule {
    @Provides
    fun provideComputingPowerService(computingPowerService: ComputingPowerServiceImpl): ComputingPowerService {
        return computingPowerService
    }
}
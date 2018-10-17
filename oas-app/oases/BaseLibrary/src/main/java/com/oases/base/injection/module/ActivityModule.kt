package com.oases.base.injection.module

import android.app.Activity
import android.content.Context
import com.oases.base.common.BaseApplication
import com.oases.base.injection.ActivityScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/*
    Activity级别Module
 */
@Module
class ActivityModule(private val activity: Activity) {

    @ActivityScope
    @Provides
    fun provideActivity(): Activity {
        return this.activity
    }
}

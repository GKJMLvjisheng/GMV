package com.oases.base.injection

import kotlin.annotation.Retention
import javax.inject.Scope

/*
    Activity级别 作用域
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

package com.oases.base.injection

import kotlin.annotation.Retention

import javax.inject.Scope

/*
    组件级别 作用域
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerComponentScope

package com.darwinit.annotation.autodsl

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AutoDsl(val functionName: String="", val builderAnnotations: Array<String> = [])

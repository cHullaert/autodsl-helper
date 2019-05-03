package com.darwinit.annotation.autodsl.generator

import com.darwinit.annotation.autodsl.AutoDslOption
import com.darwinit.annotation.autodsl.AutoDslOptionLoader
import com.squareup.kotlinpoet.FileSpec

abstract class AbstractGenerator {

    protected val options: AutoDslOption by lazy {
        AutoDslOptionLoader().load()
    }

    abstract fun build(): FileSpec

    companion object {
        const val BUILDER_CLASS_PATTERN = "%sAutoBuilder"
        const val FUNCTION_CLASS_PATTERN = "%sFunction"
    }
}
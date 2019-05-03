package com.darwinit.annotation.autodsl

import com.squareup.kotlinpoet.FileSpec
import javax.lang.model.element.Element

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
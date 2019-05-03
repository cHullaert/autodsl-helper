package com.darwinit.annotation.autodsl

import com.squareup.kotlinpoet.FileSpec
import javax.lang.model.element.Element

interface Generator {

    fun build(): FileSpec

    companion object {
        const val BUILDER_CLASS_PATTERN = "%sAutoBuilder"
        const val FUNCTION_CLASS_PATTERN = "%sFunction"
    }
}
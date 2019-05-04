package com.darwinit.annotation.autodsl.generator

import com.darwinit.annotation.autodsl.AutoDsl
import com.darwinit.annotation.autodsl.AutoDslOption
import com.darwinit.annotation.autodsl.AutoDslOptionLoader
import com.squareup.kotlinpoet.FileSpec
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

abstract class AbstractGenerator(protected val clazz: TypeElement,
                                 protected val fields: Iterable<VariableElement>,
                                 protected val clazzList: Iterable<Element>) {

    protected fun getPackageName(): String {
        return clazz.qualifiedName.toString().substring(0, clazz.qualifiedName.toString().lastIndexOf("."))
    }

    protected val autoDsl: AutoDsl by lazy {
        clazz.getAnnotation(AutoDsl::class.java)
    }

    protected val options: AutoDslOption by lazy {
        AutoDslOptionLoader().load()
    }

    abstract fun build(): FileSpec

    companion object {
        const val BUILDER_CLASS_PATTERN = "%sAutoBuilder"
        const val FUNCTION_CLASS_PATTERN = "%sFunction"
    }
}
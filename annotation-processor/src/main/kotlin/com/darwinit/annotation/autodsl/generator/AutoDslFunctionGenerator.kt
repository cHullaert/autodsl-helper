package com.darwinit.annotation.autodsl.generator

import com.darwinit.annotation.autodsl.javaToKotlinType
import com.squareup.kotlinpoet.*
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class AutoDslFunctionGenerator(clazz: TypeElement,
                               fields: Iterable<VariableElement>,
                               clazzList: Iterable<Element>): AbstractGenerator(clazz, fields, clazzList) {

    override fun build(): FileSpec {
        return FileSpec.builder(getPackageName(), FUNCTION_CLASS_PATTERN.format(clazz.simpleName.toString()))
            .addFunction(buildFunction("value"))
            .build()
    }

    fun buildFunction(variableName: String): FunSpec {
        val lambda= LambdaTypeName.get(receiver = TypeVariableName(BUILDER_CLASS_PATTERN.format(clazz.simpleName.toString())),
            returnType = Unit::class.asClassName())

        val functionName = if(this.autoDsl.functionName.isNotBlank()) this.autoDsl.functionName else clazz.simpleName.toString().decapitalize()

        return FunSpec.builder(functionName)
            .addParameter("block", lambda)
            .addStatement("%s = %s().apply(block).build()".format(variableName, BUILDER_CLASS_PATTERN.format(clazz.simpleName.toString())))
            .build()
    }

}
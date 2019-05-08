package com.darwinit.annotation.autodsl.generator

import com.darwinit.annotation.autodsl.javaToKotlinType
import com.squareup.kotlinpoet.*
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class AutoDslCollectionFunctionGenerator {

    fun buildFunction(variableName: String): FunSpec {
        val lambda= LambdaTypeName.get(receiver = TypeVariableName(variableName.toUpperCase()),
            returnType = Unit::class.asClassName())

        return FunSpec.builder(variableName)
            .addParameter("block", lambda)
            .addStatement("(%s as MutableCollection<*>).addAll(%s.apply(block))".format(variableName, variableName.toUpperCase()))
            .build()
    }

}
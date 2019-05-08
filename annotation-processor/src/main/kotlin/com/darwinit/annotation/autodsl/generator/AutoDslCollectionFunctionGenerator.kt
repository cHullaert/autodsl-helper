package com.darwinit.annotation.autodsl.generator

import com.darwinit.annotation.autodsl.javaToKotlinType
import com.squareup.kotlinpoet.*
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class AutoDslCollectionFunctionGenerator {

    fun buildFunction(field: VariableElement): FunSpec {
        val parameterizedTypeName=field.asType().asTypeName() as ParameterizedTypeName
        val type=parameterizedTypeName.typeArguments[0].javaToKotlinType() as ClassName

        val variableName=field.simpleName.toString()
        val lambda= LambdaTypeName.get(receiver = TypeVariableName(variableName.toUpperCase()),
            returnType = Unit::class.asClassName())

        return FunSpec.builder(variableName)
            .addParameter("block", lambda)
            .addStatement("(%s as MutableList<%s>).addAll(%s().apply(block))".format(variableName,
                                                                                            type.simpleName,
                                                                                            variableName.toUpperCase()))
            .build()
    }

}
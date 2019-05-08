package com.darwinit.annotation.autodsl.generator

import com.darwinit.annotation.autodsl.getClassname
import com.darwinit.annotation.autodsl.javaToKotlinType
import com.squareup.kotlinpoet.FileSpec
import javax.lang.model.element.VariableElement
import com.darwinit.annotation.autodsl.*
import com.squareup.kotlinpoet.*
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ClassName


class CollectionGenerator(private val packageName: String, private val fields: List<VariableElement>) {

    fun build(): FileSpec {
        val builder=FileSpec.builder(packageName,
                                    "CollectionWrapper")

        fields.distinctBy {
            it.simpleName.toString()
        }.filter {
            isAutoDslObjectCollection(it)
        }.forEach {
            builder.addType(createCollectionType(it))
        }

        return builder.build()
    }

    private fun buildFunction(field: VariableElement): FunSpec {
        val parameterizedTypeName=field.asType().asTypeName() as ParameterizedTypeName
        val type=parameterizedTypeName.typeArguments[0].javaToKotlinType() as ClassName
        val builderName = AbstractGenerator.BUILDER_CLASS_PATTERN.format(type.simpleName)
        val lambda= LambdaTypeName.get(receiver = TypeVariableName(builderName),
            returnType = Unit::class.asClassName())

        val functionName = type.simpleName.decapitalize()

        return FunSpec.builder(functionName)
            .addParameter("block", lambda)
            .addStatement("add(%s().apply(block).build())".format(builderName))
            .build()
    }

    private fun createCollectionType(field: VariableElement): TypeSpec {
        val parameterizedTypeName=field.asType().asTypeName() as ParameterizedTypeName
        val newParameterizedTypeName=kotlin.collections.ArrayList::class.asClassName().parameterizedBy(*parameterizedTypeName.typeArguments.map { it.javaToKotlinType() }.toTypedArray())

        return TypeSpec.classBuilder(field.simpleName.toString().toUpperCase())
            .superclass(newParameterizedTypeName)
            .addFunction(buildFunction(field))
            .build()
    }

    private fun isAutoDslObjectCollection(field: VariableElement): Boolean {
        var type=field.asType().asTypeName()
        if (type is ParameterizedTypeName) {
            return type.typeArguments[0].javaToKotlinType() as ClassName!=ClassName("kotlin", "String")
        }

        return false
    }
}
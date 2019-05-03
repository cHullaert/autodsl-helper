package com.darwinit.annotation.autodsl

import com.squareup.kotlinpoet.*
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class FunctionGenerator(private val clazz: TypeElement, private val fields: Iterable<VariableElement>): Generator {

    private fun getPackageName(): String {
        return clazz.qualifiedName.toString().substring(0, clazz.qualifiedName.toString().lastIndexOf("."))
    }

    override fun build(): FileSpec {
        return FileSpec.builder(getPackageName(), Generator.FUNCTION_CLASS_PATTERN.format(clazz.simpleName.toString()))
            .addFunction(buildFunction())
            .build()
    }

    fun buildFunction(): FunSpec {
        val lambda= LambdaTypeName.get(receiver = TypeVariableName(Generator.BUILDER_CLASS_PATTERN.format(clazz.simpleName.toString())),
            returnType = Unit::class.asClassName())

        return FunSpec.builder(clazz.simpleName.toString().toLowerCase())
            .addParameter("block", lambda)
            .returns(clazz.javaToKotlinType())
            .addStatement("return %s().apply(block).build()".format(Generator.BUILDER_CLASS_PATTERN.format(clazz.simpleName.toString())))
            .build()
    }

}
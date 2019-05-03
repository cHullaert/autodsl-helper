package com.darwinit.annotation.autodsl

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class BuilderGenerator(private val clazz: TypeElement, private val fields: Iterable<VariableElement>): Generator {

    private fun getPackageName(): String {
        return clazz.qualifiedName.toString().substring(0, clazz.qualifiedName.toString().lastIndexOf("."))
    }

    private fun createBuilderType(): TypeSpec {
        return TypeSpec.classBuilder(Generator.BUILDER_CLASS_PATTERN.format(clazz.simpleName.toString()))
            .addAnnotation(Builder::class)
            .addProperties(createProperties())
            .addFunction(createBuildFunction())
            .build()
    }

    private fun createBuildFunction(): FunSpec {
        val paramList = fields.map {
            "%s=this.%s".format(it.simpleName.toString(), it.simpleName.toString())
        }.joinToString(",")

        return FunSpec.builder("build")
            .returns(clazz.javaToKotlinType())
            .addStatement("return "+clazz.simpleName.toString()+"("+paramList+")")
            .build()
    }

    private fun createProperties(): Iterable<PropertySpec> {

        return fields.map {
            PropertySpec.builder(it.simpleName.toString(), it.javaToKotlinType().copy(nullable = false))
                .mutable()
                .build()
        }.asIterable()
    }

    override fun build(): FileSpec {
        return FileSpec.builder(getPackageName(), Generator.BUILDER_CLASS_PATTERN.format(clazz.simpleName.toString()))
            .addType(createBuilderType())
            .addFunction(FunctionGenerator(clazz, fields).buildFunction())
            .build()

    }

}
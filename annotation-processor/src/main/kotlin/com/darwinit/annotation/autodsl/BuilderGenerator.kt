package com.darwinit.annotation.autodsl

import com.squareup.kotlinpoet.*
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class BuilderGenerator(private val clazz: TypeElement, private val fields: Iterable<VariableElement>): Generator {

    private val option: AutoDslOption by lazy {
        AutoDslOptionLoader().load()
    }

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
            "%s=%s".format(it.simpleName.toString(), it.simpleName.toString())
        }.joinToString(",")

        return FunSpec.builder("build")
            .returns(clazz.javaToKotlinType())
            .addStatement("return "+clazz.simpleName.toString()+"("+paramList+")")
            .build()
    }

    private fun createProperties(): Iterable<PropertySpec> {

        return fields.map {
            PropertySpec.builder(it.simpleName.toString(), it.javaToKotlinType().copy(nullable = it.javaToKotlinType().isNullable))
                .mutable()
                .initializer(it.javaToKotlinType().getDefaultValue().toString())
                .build()
        }.asIterable()
    }

    override fun build(): FileSpec {
        TODO(option.toString())
        /*return FileSpec.builder(getPackageName(), Generator.BUILDER_CLASS_PATTERN.format(clazz.simpleName.toString()))
            .addType(createBuilderType())
            .addFunction(FunctionGenerator(clazz, fields).buildFunction())
            .build()*/

    }

}
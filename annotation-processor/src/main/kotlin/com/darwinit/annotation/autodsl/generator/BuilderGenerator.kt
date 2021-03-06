package com.darwinit.annotation.autodsl.generator

import com.darwinit.annotation.autodsl.*
import com.squareup.kotlinpoet.*
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ClassName

class BuilderGenerator(
    clazz: TypeElement,
    fields: Iterable<VariableElement>,
    clazzList: Iterable<Element>
): AbstractGenerator(clazz, fields, clazzList) {

    private fun createBuilderType(): TypeSpec {
        val builder=TypeSpec.classBuilder(BUILDER_CLASS_PATTERN.format(clazz.simpleName.toString()))
            .addAnnotation(Builder::class)

        autoDsl.builderAnnotations
            .forEach {
                builder.addAnnotation(ClassName(it.substring(0, it.lastIndexOf(".")), it.substring(it.lastIndexOf(".")+1, it.length)))
            }

        return builder.addProperties(createProperties())
            .addFunctions(createAutoDslFunctions())
            .addFunctions(createAutoDslCollectionFunctions())
            .addFunction(createBuildFunction())
            .build()
    }

    private fun createAutoDslFunctions(): Iterable<FunSpec> {
        return this.fields.filter { isAutoDslObject(it) }
            .map {
                val autoDsl=getAutoDslObject(it)
                val generator=AutoDslFunctionGenerator(autoDsl as TypeElement, fields, this.clazzList)
                generator.buildFunction(it.simpleName.toString())
            }
    }

    private fun createAutoDslCollectionFunctions(): Iterable<FunSpec> {
        return this.fields.filter { isAutoDslObjectCollection(it) }
            .map {
                val generator=AutoDslCollectionFunctionGenerator()
                generator.buildFunction(it)
            }
    }

    private fun createBuildFunction(): FunSpec {
        val paramList = fields.map {
            val transformer = this.options.transformers.find {
                    transformer ->  it.javaToKotlinType()==transformer.sourceClass.getClassname()
            }

            "%s=%s".format(it.simpleName.toString(), transformer?.value?.replace("%property%", it.simpleName.toString()) ?: it.simpleName.toString())
        }.joinToString(",")

        return FunSpec.builder("build")
            .returns(clazz.javaToKotlinType())
            .addStatement("return "+clazz.simpleName.toString()+"("+paramList+")")
            .build()
    }

    private fun createProperties(): Iterable<PropertySpec> {

        return fields.map {
            val typeName = it.javaToKotlinType().copy(nullable = it.javaToKotlinType().isNullable)

            val transformer = this.options.transformers.find {
                transformer ->  typeName==transformer.sourceClass.getClassname()
            }

            val modifier=if(!(isAutoDslObject(it) || (isAutoDslObjectCollection(it)))) KModifier.PUBLIC else KModifier.PRIVATE

            if(transformer !=null) {
                PropertySpec.builder(it.simpleName.toString(), transformer.substitutionClass.getClassname())
                    .mutable()
                    .initializer(transformer.defaultValue)
                    .addModifiers(modifier)
                    .build()
            } else if (isAutoDslObjectCollection(it)) {
                val parameterizedTypeName=typeName as ParameterizedTypeName
                val type=parameterizedTypeName.typeArguments[0].javaToKotlinType() as ClassName

                val newParameterizedTypeName=kotlin.collections.MutableList::class.asClassName().parameterizedBy(*parameterizedTypeName.typeArguments.map { it.javaToKotlinType() }.toTypedArray())

                PropertySpec.builder(it.simpleName.toString(), newParameterizedTypeName.javaToKotlinType())
                    .initializer("mutableListOf<%s>()".format(type.simpleName))
                    .addModifiers(modifier)
                    .build()
            }
            else
            PropertySpec.builder(it.simpleName.toString(), typeName)
                    .mutable()
                    .initializer(it.javaToKotlinType().getDefaultValue().toString())
                    .addModifiers(modifier)
                    .build()
        }.asIterable()
    }

    private fun getAutoDslObject(field: VariableElement): Element? {
        return this.clazzList.find { field.javaToKotlinType() == it.javaToKotlinType() }
    }

    private fun isAutoDslObject(field: VariableElement): Boolean {
        val value=this.clazzList.find { field.javaToKotlinType() == it.javaToKotlinType() }
        return value!=null
    }

    private fun isAutoDslObjectCollection(field: VariableElement): Boolean {
        var type=field.asType().asTypeName()
        if (type is ParameterizedTypeName) {
            return type.typeArguments[0].javaToKotlinType() as ClassName!=ClassName("kotlin", "String")
        }

        return false
    }

    override fun build(): FileSpec {
        val builder=FileSpec.builder(getPackageName(), BUILDER_CLASS_PATTERN.format(clazz.simpleName.toString()))

        fields.map {
                field -> this.options.transformers.find {
                                    field.javaToKotlinType() == it.sourceClass.getClassname()
                         }
        }.forEach {
            it?.imports?.forEach {
                import -> builder.addImport(import.packageName, import.name)
            }
        }

        return builder.addType(createBuilderType())
            .addFunction(FunctionGenerator(clazz, fields, clazzList).buildFunction())
            .build()

    }

}
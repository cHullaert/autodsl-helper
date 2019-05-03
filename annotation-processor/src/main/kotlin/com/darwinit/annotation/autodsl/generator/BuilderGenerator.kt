package com.darwinit.annotation.autodsl.generator

import com.darwinit.annotation.autodsl.*
import com.squareup.kotlinpoet.*
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class BuilderGenerator(private val clazz: TypeElement, private val fields: Iterable<VariableElement>): AbstractGenerator() {

    private fun getPackageName(): String {
        return clazz.qualifiedName.toString().substring(0, clazz.qualifiedName.toString().lastIndexOf("."))
    }

    private fun createBuilderType(): TypeSpec {
        val builder=TypeSpec.classBuilder(BUILDER_CLASS_PATTERN.format(clazz.simpleName.toString()))
            .addAnnotation(Builder::class)

        clazz.getAnnotation(AutoDsl::class.java).builderAnnotations
            .forEach {
                builder.addAnnotation(ClassName(it.substring(0, it.lastIndexOf(".")), it.substring(it.lastIndexOf(".")+1, it.length)))
            }

        return builder.addProperties(createProperties())
            .addFunction(createBuildFunction())
            .build()
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

            if(transformer !=null) {
                PropertySpec.builder(it.simpleName.toString(), transformer.substitutionClass.getClassname())
                    .mutable()
                    .initializer(transformer.defaultValue)
                    .build()
            }
            else
            PropertySpec.builder(it.simpleName.toString(), typeName)
                    .mutable()
                    .initializer(it.javaToKotlinType().getDefaultValue().toString())
                    .build()
        }.asIterable()
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
            .addFunction(FunctionGenerator(clazz, fields).buildFunction())
            .build()

    }

}
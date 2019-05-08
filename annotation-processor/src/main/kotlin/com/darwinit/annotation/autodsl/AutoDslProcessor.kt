package com.darwinit.annotation.autodsl

import com.darwinit.annotation.autodsl.generator.BuilderGenerator
import com.darwinit.annotation.autodsl.generator.CollectionGenerator
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.lang.model.util.ElementFilter
import javax.lang.model.element.VariableElement

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.lang.model.element.Element
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName

@AutoService(Processor::class)
@SupportedOptions(AutoDslProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class AutoDslProcessor: AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(AutoDsl::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    private fun getFields(typeElement: TypeElement): List<VariableElement> {
        val fields = ElementFilter.fieldsIn(typeElement.enclosedElements)
        if(typeElement.superclass != null) {
            val superElement=processingEnv.typeUtils.asElement(typeElement.superclass)
            if(superElement != null) {
                return fields+getFields(superElement as TypeElement)
            }
        }

        return fields
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        this.processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "evaluate auto dsl processor")

        val clazzList=roundEnv!!.getElementsAnnotatedWith(AutoDsl::class.java)

        processClasses(clazzList)
        processCollections(clazzList)

        return false
    }

    private fun processCollections(clazzList: Set<Element>) {
        val clazzGroup = clazzList
            .groupBy {
                clazz -> (clazz as TypeElement).asClassName().packageName
            }

        clazzGroup.forEach {
            clazz ->

            val allFields=clazz.value.map { it as TypeElement }
                .filter { it.kind === javax.lang.model.element.ElementKind.CLASS}
                .flatMap { getFields(it) }

            if(allFields.isNotEmpty())
                CollectionGenerator(clazz.key, allFields)
                    .build()
                    .writeTo(processingEnv.filer)
        }
    }

    private fun processClasses(clazzList: Set<Element>) {
        clazzList
            .asSequence()
            .map { it as TypeElement }
            .filter { it.kind === ElementKind.CLASS}
            .forEach {
                this.processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "class %s is annotated with autoDsl".format(it.simpleName.toString()))

                val fields=getFields(it)
                BuilderGenerator(it, fields, clazzList)
                    .build()
                    .writeTo(processingEnv.filer)
            }
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}


fun Element.javaToKotlinType(): TypeName =
    asType().asTypeName().javaToKotlinType()


fun TypeName.javaToKotlinType(): TypeName {
    return if (this is ParameterizedTypeName) {
        val className = rawType.javaToKotlinType() as ClassName
        className.parameterizedBy(*typeArguments.map { it.javaToKotlinType() }.toTypedArray())
    } else {
        val className =
            JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(FqName(toString()))
                ?.asSingleFqName()?.asString()

        return if (className == null) {
            this
        } else {
            ClassName.bestGuess(className)
        }
    }
}

fun TypeName.getDefaultValue(): Any? {
    if(this.isNullable)
        return null

    when(this) {
        ANY -> return "???any"
        ARRAY ->  return "???array"
        UNIT ->  return "???unit"
        BOOLEAN ->  return "false"
        BYTE,
        SHORT,
        INT,
        LONG ->  return "0"
        CHAR -> return "''"
        FLOAT,
        DOUBLE -> return "0.0"
        ClassName("java.util", "UUID") -> return "UUID(0, 0)"
        ClassName("kotlin", "String") -> return "\"\""
    }

    if(this.toString().startsWith("kotlin.collections."))
        return "emptyList()"

    return "%s()".format(this.toString())
}
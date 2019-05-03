package com.darwinit.annotation.autodsl

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.lang.reflect.Modifier.isStatic
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.lang.model.util.ElementFilter
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

@AutoService(Processor::class)
@SupportedOptions(AutoDslProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class AutoDslProcessor: AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(AutoDsl::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        println("test")
        this.processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "start process")

        roundEnv!!.getElementsAnnotatedWith(AutoDsl::class.java)
            .asSequence()
            .map { it as TypeElement }
            .filter { it.kind === ElementKind.CLASS}
            .forEach {
                val fields = ElementFilter.fieldsIn(it.enclosedElements)
                BuilderGenerator(it, fields)
                    .build()
                    .writeTo(processingEnv.filer)
            }

        return false
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
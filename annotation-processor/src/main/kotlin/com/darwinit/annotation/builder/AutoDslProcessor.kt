package com.darwinit.annotation.builder

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
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
import com.sun.deploy.util.SystemUtils.getSimpleName
import jdk.nashorn.internal.objects.NativeArray.forEach





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
        this.processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "start process")

        roundEnv!!.getElementsAnnotatedWith(AutoDsl::class.java)
            .asSequence()
            .map { it as TypeElement }
            .filter { it.kind === ElementKind.CLASS}
            .forEach {
                val fields = ElementFilter.fieldsIn(it.enclosedElements)
                val file=createBuilderClass(it, fields)

                file.writeTo(processingEnv.filer)
            }

        return true
    }

    private fun getPackageName(clazz: TypeElement): String {
        return clazz.qualifiedName.toString().substring(0, clazz.qualifiedName.toString().lastIndexOf("."))
    }
    private fun createBuilderClass(clazz: TypeElement, fields: List<VariableElement>): FileSpec {

        return FileSpec.builder(getPackageName(clazz), clazz.simpleName.toString()+"Builder")
            .addType(createBuilderType(clazz, fields))
            .build()
    }

    private fun createBuilderType(clazz: TypeElement, fields: List<VariableElement>): TypeSpec {
        return TypeSpec.classBuilder(clazz.simpleName.toString()+"Builder")
            .addProperties(createProperties(fields))
            .build()
    }

    private fun createProperties(fields: List<VariableElement>): Iterable<PropertySpec> {

        return fields.map {
            PropertySpec.builder(it.simpleName.toString(), it.asType().asTypeName().copy(nullable = true))
                .mutable()
                .initializer("null")
                .build()
        }.asIterable()
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}
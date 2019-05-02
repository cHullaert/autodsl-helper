package com.darwinit.annotation.builder;

import java.lang.System;

@com.google.auto.service.AutoService(value = {javax.annotation.processing.Processor.class})
@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u001a2\u00020\u0001:\u0001\u001aB\u0005\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0002J\u001e\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0002J\u001c\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u000e\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u0014H\u0016J$\u0010\u0015\u001a\u00020\u00162\u0010\u0010\u0017\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0006\u0018\u00010\u00122\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016\u00a8\u0006\u001b"}, d2 = {"Lcom/darwinit/annotation/builder/AutoDslProcessor;", "Ljavax/annotation/processing/AbstractProcessor;", "()V", "createBuilderClass", "Lcom/squareup/kotlinpoet/FileSpec;", "clazz", "Ljavax/lang/model/element/TypeElement;", "fields", "", "Ljavax/lang/model/element/VariableElement;", "createBuilderType", "Lcom/squareup/kotlinpoet/TypeSpec;", "createProperties", "", "Lcom/squareup/kotlinpoet/PropertySpec;", "getPackageName", "", "getSupportedAnnotationTypes", "", "getSupportedSourceVersion", "Ljavax/lang/model/SourceVersion;", "process", "", "annotations", "roundEnv", "Ljavax/annotation/processing/RoundEnvironment;", "Companion", "annotation-processor"})
@javax.annotation.processing.SupportedOptions(value = {"kapt.kotlin.generated"})
public final class AutoDslProcessor extends javax.annotation.processing.AbstractProcessor {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated";
    public static final com.darwinit.annotation.builder.AutoDslProcessor.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.Set<java.lang.String> getSupportedAnnotationTypes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public javax.lang.model.SourceVersion getSupportedSourceVersion() {
        return null;
    }
    
    @java.lang.Override()
    public boolean process(@org.jetbrains.annotations.Nullable()
    java.util.Set<? extends javax.lang.model.element.TypeElement> annotations, @org.jetbrains.annotations.Nullable()
    javax.annotation.processing.RoundEnvironment roundEnv) {
        return false;
    }
    
    private final java.lang.String getPackageName(javax.lang.model.element.TypeElement clazz) {
        return null;
    }
    
    private final com.squareup.kotlinpoet.FileSpec createBuilderClass(javax.lang.model.element.TypeElement clazz, java.util.List<? extends javax.lang.model.element.VariableElement> fields) {
        return null;
    }
    
    private final com.squareup.kotlinpoet.TypeSpec createBuilderType(javax.lang.model.element.TypeElement clazz, java.util.List<? extends javax.lang.model.element.VariableElement> fields) {
        return null;
    }
    
    private final java.lang.Iterable<com.squareup.kotlinpoet.PropertySpec> createProperties(java.util.List<? extends javax.lang.model.element.VariableElement> fields) {
        return null;
    }
    
    public AutoDslProcessor() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/darwinit/annotation/builder/AutoDslProcessor$Companion;", "", "()V", "KAPT_KOTLIN_GENERATED_OPTION_NAME", "", "annotation-processor"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
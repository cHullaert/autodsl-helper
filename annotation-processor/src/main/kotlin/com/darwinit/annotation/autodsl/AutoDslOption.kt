package com.darwinit.annotation.autodsl

import com.fasterxml.jackson.annotation.JsonProperty
import com.squareup.kotlinpoet.ClassName

data class OptionClass(@JsonProperty("package") val packageName: String,
                       val name: String)
data class Transformer(@JsonProperty("class") val sourceClass: OptionClass,
                       val value: String,
                       @JsonProperty("default") val defaultValue: String,
                       val substitutionClass: OptionClass,
                       val imports: List<OptionClass>)
data class AutoDslOption(val transformers: List<Transformer>)

fun OptionClass.getClassname(): ClassName {
    return ClassName(this.packageName, this.name)
}
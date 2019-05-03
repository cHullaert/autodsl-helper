package com.darwinit.annotation.autodsl

data class Transformer(val classname: String, val value: String)
data class AutoDslOption(val transformers: List<Transformer>)
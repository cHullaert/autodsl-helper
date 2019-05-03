package com.darwinit.annotation.autodsl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

class AutoDslOptionLoader {

    fun load(): AutoDslOption {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val json = AutoDslOptionLoader::class.java.getResource("/autodsl.json").readText()


        return mapper.readValue(json)
    }
}
package com.darwinit.annotation.demo

import com.darwinit.annotation.autodsl.*
import java.util.*

@AutoDsl(builderAnnotations = ["com.darwinit.annotation.demo.Dummy"])
open class Person (val name: String,
                   val age: Int,
                   val uuid: UUID,
                   val tags: List<String>,
                   val address: Address,
                   val friends: List<Person>)

@AutoDsl
open class Address(var street: String? = null,
                   var number: Int? = null,
                   var city: String? = null)


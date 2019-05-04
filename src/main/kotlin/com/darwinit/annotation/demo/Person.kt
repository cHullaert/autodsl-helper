package com.darwinit.annotation.demo

import com.darwinit.annotation.autodsl.*
import java.util.*

@AutoDsl(builderAnnotations = ["com.darwinit.annotation.demo.Dummy"])
open class Person (val name: String,
                   val age: Int,
                   val uuid: UUID,
                   val tags: List<String>,
                   val friends: List<Person>)
@AutoDsl(functionName = "overrideSuperPerson")
open class SuperPerson (name: String,
                        age: Int,
                        uuid: UUID,
                        friends: List<Person>,
                        tags: List<String>,
                        val attribute: String): Person(name, age, uuid, tags, friends)
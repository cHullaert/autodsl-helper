package com.darwinit.annotation.demo

import com.darwinit.annotation.autodsl.AutoDsl
import java.util.*

@AutoDsl(functionName = "overrideSuperPerson")
open class SuperPerson (name: String,
                        age: Int,
                        uuid: UUID,
                        friends: List<Person>,
                        tags: List<String>,
                        address: Address,
                        val attribute: String): Person(name, age, uuid, tags, address, friends)
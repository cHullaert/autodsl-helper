package com.darwinit.annotation.demo

import com.darwinit.annotation.autodsl.*
import java.util.*

@AutoDsl
open class Person (val name: String, val age: Int, val uuid: UUID, val friends: List<Person>)
@AutoDsl
open class SuperPerson (name: String, age: Int, uuid: UUID, friends: List<Person>, val attribute: String): Person(name, age, uuid, friends)
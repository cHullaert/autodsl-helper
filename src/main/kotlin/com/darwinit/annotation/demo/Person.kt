package com.darwinit.annotation.demo

import com.darwinit.annotation.autodsl.*
import java.util.*

@AutoDsl
class Person (val name: String, val age: Int, val uuid: UUID, val friends: List<Person>)
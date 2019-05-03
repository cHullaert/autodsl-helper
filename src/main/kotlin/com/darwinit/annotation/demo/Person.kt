package com.darwinit.annotation.demo

import com.darwinit.annotation.autodsl.*

@AutoDsl
class Person (val name: String, val age: Int, val friends: Array<Person>)
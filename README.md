https://github.com/bintray/gradle-bintray-plugin/issues/134
https://reflectoring.io/guide-publishing-to-bintray-with-gradle/
https://bintray.com/profile/edit

# Introduction

The goal of this project is to offer a convenient way to produce dsl via kotlin classes.  The project is not a fork but
it's largely inspired by https://github.com/juanchosaravia/autodsl this one. Thanks to him.

The following link is a documentation used by the creation of this project.  https://proandroiddev.com/writing-dsls-in-kotlin-part-1-7f5d2193f277

This project provide also a convenient way to execute the script produced with the dsl syntax.  

Please feel free to use it as you want.

# DSL generation
## code examples
```kotlin
// the simple annotation AutoDsl will create automatically the builder and the function to use your dsl
// here, there is also the parameter "builderAnnotations" which allow to add a personnal annotation on the class
// it can be useful with some framework like spring
@AutoDsl(builderAnnotations = ["com.darwinit.annotation.demo.Dummy"])
open class Person (val name: String, 
                   val age: Int, 
                   // notice that the type is java.lang.UUID 
                   val uuid: UUID, 
                   val friends: List<Person>)

// will gives this class
// notice, the @Builder annotation is automatically added, but the @Dummy come from the parametrization
@Builder
@Dummy
class PersonAutoBuilder {
    var name: String = ""
    var age: Int = 0
    // the uuid in the builder is not exaclty the same since we want our users
    // use String instead more complex type as UUID
    var uuid: String = "00000000-0000-0000-0000-000000000000"
    var friends: List<Person> = emptyList()
    
    // at the build method level, we can see the transformation automatically in UUID from string
    // all of this is possible, thanks to the configuration file describe later in this document
    fun build(): Person = Person(name=name,age=age,uuid=UUID.fromString(uuid),friends=friends)
}

// the function person will allow to use the dsl
fun person(block: PersonAutoBuilder.() -> Unit): Person = PersonAutoBuilder().apply(block).build()


// amazing, it works also with inheritance
@AutoDsl(functionName = "overrideSuperPerson")
// with the parameter functionName we can choose the name of the function (normally it's based on the classname
open class SuperPerson (name: String, 
                        age: Int, 
                        uuid: UUID, 
                        friends: List<Person>, 
                        val attribute: String): Person(name, age, uuid, friends)

// will gives
@Builder
class SuperPersonAutoBuilder {
    var attribute: String = ""
    var name: String = ""
    var age: Int = 0
    var uuid: String = "00000000-0000-0000-0000-000000000000"
    var friends: List<Person> = emptyList()
    fun build(): SuperPerson =
            SuperPerson(attribute=attribute,name=name,age=age,uuid=UUID.fromString(uuid),friends=friends)
}

// see here the custom function name
fun overrideSuperPerson(block: SuperPersonAutoBuilder.() -> Unit): SuperPerson =
        SuperPersonAutoBuilder().apply(block).build()

```

## configuration
 
```json
{
  "transformers": [
    {
      "comment_1": "if a property of the source class is a java.util.UUID",
      "class": {
        "package": "java.util",
        "name": "UUID"
      },
      "comment_2": "please use the following class",
      "substitutionClass": {
        "package": "kotlin",
        "name": "String"
      },
      "comment_3": "with this default value",
      "default": "\"00000000-0000-0000-0000-000000000000\"",
      "comment_4": "and transform it like this",
      "value": "UUID.fromString(%property%)",
      "imports": [
        {
          "comment_5": "don't forget to import the class type in the parametrization since it's used on the line value",          
          "package": "java.util",
          "name": "UUID"
        }
      ]
    }
  ]
}
```

:exclamation: the "comment_*" properties are not really part of the configuration, there are just there to add comments 
in json file

# Script runner

This project allows also the execution of script kotlin.

## Preparation

First you need a file named **javax.script.ScriptEngineFactory** in the classpath of your application to follow 
JSR233 specifications.  This file is a simple file with a simple content.

```text
org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory
```

## Examples

```kotlin

// execution of the script
// the list of hooks is to specify that we wan to call container.add method with all the persons created in the script.
// that's the way to retrieve the information

val scriptHooks= mutableListOf(ScriptHook("container", "add", patterns = arrayOf("person")))
// prepare the runner
val runner=ScriptRunner(ScriptRunner.Script("path/to/the/script",
                                            listOf(ScriptRunner.Dependency("com.darwinit.annotation.demo.*"))),
                        scriptHooks,
                        // we don't have top level in our script
                        setOf(ScriptRunner.Option.GENERATE_MAIN))
val container = mutableListOf<Any>()
// run the script with the binding **container** for the variable **container** 
runner.run(listOf(ScriptRunner.Variable("container", "MutableList<Any>", container)))
// we can see that the person is well retrieved inside
assertThat(container.size).isEqualTo(1)
assertThat(container[0]).isInstanceOf(Person::class.java)
assertThat((container[0] as Person).name).isEqualTo("John")
assertThat((container[0] as Person).address.city).isEqualTo("London")




// here a script to execute on the runner
person {
    name = "John"
    age = 15
    address {
        street = "Main Street"
        number = 12
        city = "London"
    }
}
```

# Readings

* https://github.com/cretz/kastree/
* https://github.com/square/kotlinpoet/issues/236
* https://github.com/FasterXML/jackson-module-kotlin
* https://stackoverflow.com/questions/8587096/how-do-you-debug-java-annotation-processors-using-intellij
* https://medium.com/@joachim.beckers/debugging-an-annotation-processor-using-intellij-idea-in-2018-cde72758b78a
* https://kotlinexpertise.com/run-kotlin-scripts-from-kotlin-programs/
# usage

The goal of this project is to offer a convenient way to produce dsl via kotlin classes.  The project is not a fork but
it's largely inspired by https://github.com/juanchosaravia/autodsl this one. Thanks to him.

The following link is a documentation used by the creation of this project.  https://proandroiddev.com/writing-dsls-in-kotlin-part-1-7f5d2193f277

This project provide also a convenient way to execute the script produced with the dsl syntax.

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
@AutoDsl
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

fun superperson(block: SuperPersonAutoBuilder.() -> Unit): SuperPerson =
        SuperPersonAutoBuilder().apply(block).build()

```

## configuration
```json
{
  "transformers": [
    {
      # if a property of the source class is a java.util.UUID
      "class": {
        "package": "java.util",
        "name": "UUID"
      },
      # please use the following class
      "substitutionClass": {
        "package": "kotlin",
        "name": "String"
      },
      // with this default value
      "default": "\"00000000-0000-0000-0000-000000000000\"",
      // and transform it like this
      "value": "UUID.fromString(%property%)",
      "imports": [
        {
          // don't forget to import the class type in the parametrization since it's used on the line value
          "package": "java.util",
          "name": "UUID"
        }
      ]
    }
  ]
}
```

# readings

* https://github.com/cretz/kastree/
* https://github.com/square/kotlinpoet/issues/236
* https://github.com/FasterXML/jackson-module-kotlin
* https://stackoverflow.com/questions/8587096/how-do-you-debug-java-annotation-processors-using-intellij
* https://medium.com/@joachim.beckers/debugging-an-annotation-processor-using-intellij-idea-in-2018-cde72758b78a
* https://kotlinexpertise.com/run-kotlin-scripts-from-kotlin-programs/
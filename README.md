# usage

## code examples
```kotlin
@AutoDsl(builderAnnotations = ["com.darwinit.annotation.demo.Dummy"])
open class Person (val name: String, 
                   val age: Int, 
                   val uuid: UUID, 
                   val friends: List<Person>)

// will gives
@Builder
@Dummy
class PersonAutoBuilder {
    var name: String = ""
    var age: Int = 0
    var uuid: String = "00000000-0000-0000-0000-000000000000"
    var friends: List<Person> = emptyList()
    fun build(): Person = Person(name=name,age=age,uuid=UUID.fromString(uuid),friends=friends)
}

fun person(block: PersonAutoBuilder.() -> Unit): Person = PersonAutoBuilder().apply(block).build()


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
      "class": {
        "package": "java.util",
        "name": "UUID"
      },
      "substitutionClass": {
        "package": "kotlin",
        "name": "String"
      },
      "default": "\"00000000-0000-0000-0000-000000000000\"",
      "value": "UUID.fromString(%property%)",
      "imports": [
        {
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
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.darwinit.annotation.demo.Person
import org.junit.jupiter.api.Test

class PersonDslTest {

    @Test
    fun personCreatedAndGetBackInBindings() {
        val scriptHooks= mutableListOf(ScriptHook("container", "add", patterns = arrayOf("person")))
        val runner=ScriptRunner(ScriptRunner.Script("/scripts/SimplePerson.ylib",
                                                    listOf(ScriptRunner.Dependency("com.darwinit.annotation.demo.*"))),
                                scriptHooks,
                                setOf(ScriptRunner.Option.GENERATE_MAIN))
        val container = mutableListOf<Any>()
        runner.run(listOf(ScriptRunner.Variable("container", "MutableList<Any>", container)))
        assertThat(container.size).isEqualTo(1)
        assertThat(container[0]).isInstanceOf(Person::class.java)
        assertThat((container[0] as Person).name).isEqualTo("John")
        assertThat((container[0] as Person).address.city).isEqualTo("London")
    }
}
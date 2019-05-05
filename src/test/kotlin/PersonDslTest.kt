import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class PersonDslTest {

    @Test
    fun testSimplePerson() {
        val scriptHooks= mutableListOf(ScriptHook("container", "add", patterns = arrayOf("person")))
        val runner=ScriptRunner("/scripts/SimplePerson.ylib", scriptHooks, setOf(ScriptRunner.Option.GENERATE_MAIN))
        val container = mutableListOf<Any>()
        runner.run(listOf(ScriptRunner.Variable("container", "MutableList<Any>", container)))
        assertThat(container.size).isEqualTo(1)
    }
}
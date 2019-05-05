import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class PersonDslTest {

    @Test
    fun testSimplePerson() {
        val scriptHooks= mutableListOf(ScriptHook("container", "add", patterns = arrayOf("person")))
        val runner=ScriptRunner(ScriptRunner.Script("/scripts/SimplePerson.ylib",
                                                    listOf(ScriptRunner.Dependency("com.darwinit.annotation.demo.*"))),
                                scriptHooks,
                                setOf(ScriptRunner.Option.GENERATE_MAIN))
        val container = mutableListOf<Any>()
        runner.run(listOf(ScriptRunner.Variable("container", "MutableList<Any>", container)))
        assertThat(container.size).isEqualTo(1)
    }
}
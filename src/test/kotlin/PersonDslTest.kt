import org.junit.jupiter.api.Test

class PersonDslTest {

    @Test
    fun testSimplePerson() {
        val scriptHooks= mutableListOf(ScriptHook("container.add", patterns = arrayOf("person {")))
        val runner=ScriptRunner("/scripts/SimplePerson.ylib", scriptHooks)
        runner.load()
    }
}
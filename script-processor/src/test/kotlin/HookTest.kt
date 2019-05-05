import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HookTest {

    private fun clearFile(input: String): String {
        return input.replace(" ", "").replace("\n", "")
    }

    @Test
    fun whenHookThenUpdate() {
        val scriptHooks= mutableListOf(ScriptHook("container", "add", patterns = arrayOf("person")))
        val before=ScriptRunner::class.java.getResource("/hook-before.kt").readText()
        val after=ScriptRunner::class.java.getResource("/hook-after.kt").readText()
        assertEquals(clearFile(after), clearFile(ScriptHookModifier(scriptHooks).update(before)))

    }
}
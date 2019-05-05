import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class ScriptRunner(private val scriptFile: String?=null,
                   private val scriptHooks: List<ScriptHook>) {

    private val engine: ScriptEngine by lazy {
        (ScriptEngineManager().getEngineByExtension("kts"))
    }

    private val script: String by lazy {
        if(scriptFile!=null)
            prepare(ScriptRunner::class.java.getResource(this.scriptFile).readText())
        else ""
    }

    fun load() {
        println("script: \n%s".format(script))
        //this.engine.eval(script)
    }

    private fun clean(script: String): String {
        return script.split("\n")
            .map { it.trim() }
            .filterNot { it.startsWith("import") }
            .joinToString("\n")
    }

    private fun prepare(script: String): String {
        return "import com.darwinit.annotation.demo.*\n\n" + clean(ScriptHookModifier(this.scriptHooks).update(script))
    }

}
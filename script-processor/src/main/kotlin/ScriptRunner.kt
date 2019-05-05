import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.SimpleBindings

class ScriptRunner(private val script: Script? = null,
                   private val scriptHooks: List<ScriptHook>,
                   private val options: Set<Option>) {

    class Dependency(val name: String)
    class Script(val file: String?=null,
                 val dependencies: List<Dependency>)
    class Variable(val name: String,
                   val type: String,
                   val value: Any)

    enum class Option {
        GENERATE_MAIN
    }

    private val engine: ScriptEngine by lazy {
        (ScriptEngineManager().getEngineByExtension("kts"))
    }

    private val scriptText: String by lazy {
        if(script!=null)
            prepare(ScriptRunner::class.java.getResource(this.script.file).readText())
        else ""
    }

    fun run(variables: List<Variable> = emptyList()) {
        val bindings=this.engine.createBindings()
        variables.forEach {
            bindings[it.name]=it.value
        }

        val bindingCode = variables.map { "val %s = bindings[\"%s\"] as %s".format(it.name, it.name, it.type)  }
                                   .joinToString("\n")
        val code = scriptText.replace("###BINDINGS###", "%s\n".format(bindingCode))
        this.engine.eval(code, bindings)
    }

    private fun clean(script: String): String {
        return script.split("\n")
            .filterNot { it.trim().startsWith("import") }
            .joinToString("\n")
    }

    private fun prepare(scriptText: String): String {
        var current = scriptText

        current = clean(current)
        if(options.contains(Option.GENERATE_MAIN)) {
            current = "fun main() {%s}".format(current)
        }

        current = ScriptHookModifier(this.scriptHooks).update(current)

        if(options.contains(Option.GENERATE_MAIN)) {
            current = current.split("\n")
                                .drop(1)
                                .dropLast(2)
                                .joinToString("\n")
        }

        return script!!.dependencies.map {
            "import %s".format(it.name)
        }.joinToString("\n")+"\n\n###BINDINGS###%s".format(current)
    }

}
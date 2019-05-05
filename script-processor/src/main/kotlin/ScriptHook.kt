import kastree.ast.MutableVisitor
import kastree.ast.Node
import kastree.ast.Writer
import kastree.ast.psi.Parser

class ScriptHook(val obj: String, val method: String, val patterns: Array<String>)

class ScriptHookModifier(private val scriptHooks: List<ScriptHook>) {

    fun update(script: String): String {
        val file = Parser.parseFile(script)

        val list = mutableListOf<Node>()
        var mutedFile=file

        scriptHooks.forEach {
            hook -> mutedFile=MutableVisitor.preVisit(mutedFile) { v, _ ->
                    if(v is Node.Expr.Call) {
                        if(v.expr is Node.Expr.Name) {
                            if ((hook.patterns.indexOf((v.expr as Node.Expr.Name).name)>-1) && (list.filter { v === it }.isEmpty())) {
                                val container=Node.Expr.Name(hook.obj)
                                val dot=Node.Expr.BinaryOp.Oper.Token(Node.Expr.BinaryOp.Token.DOT)
                                val addExpr=Node.Expr.Name(hook.method)
                                val arg= Node.ValueArg(null, false, v)
                                val add=Node.Expr.Call(addExpr, emptyList(), listOf(arg), null)
                                list.add(v)

                                Node.Expr.BinaryOp(container, dot, add)
                            }
                            else v
                        }
                        else v
                    }
                    else v
                }
        }

        return Writer.write(mutedFile)
    }
}

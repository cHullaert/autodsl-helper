val container = mutableListOf<Object>()

fun person(function: () -> Unit): Object {
    function()
    return container.last()
}

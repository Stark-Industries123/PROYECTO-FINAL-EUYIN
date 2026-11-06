package condiciones.io

/**
 * Doble de prueba que suministra una cola predefinida de líneas de texto como [LectorConsola].
 */
class LectorListaConsola(entradas: List<String>) : LectorConsola {
    private val cola: MutableList<String> = entradas.toMutableList()

    override fun leerLinea(): String? = if (cola.isNotEmpty()) cola.removeAt(0) else null
}

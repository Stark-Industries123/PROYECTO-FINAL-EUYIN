package condiciones.io

/**
 * Doble de prueba que almacena en memoria el texto emitido a través de [EscritorConsola].
 */
class EscritorMemoriaConsola : EscritorConsola {
    private val lineas: MutableList<String> = mutableListOf()

    override fun escribir(texto: String) {
        lineas.add(texto)
    }

    override fun escribirLinea(texto: String) {
        lineas.add(texto + "\n")
    }

    fun contenidoCompleto(): String = lineas.joinToString("")
}

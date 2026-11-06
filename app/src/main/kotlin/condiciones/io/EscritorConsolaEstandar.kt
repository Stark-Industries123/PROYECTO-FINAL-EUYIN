package condiciones.io

/**
 * Implementación predeterminada de [EscritorConsola] que delega en las funciones estándar de consola.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Emite texto hacia los receptores configurados.
 */
class EscritorConsolaEstandar(
    private val receptorTexto: (String) -> Unit = RECEPTOR_TEXTO_DEFECTO,
    private val receptorLinea: (String) -> Unit = RECEPTOR_LINEA_DEFECTO
) : EscritorConsola {

    override fun escribir(texto: String) {
        if (texto.isNotEmpty() || receptorLinea === RECEPTOR_LINEA_DEFECTO) {
            receptorTexto(texto)
        } else {
            receptorTexto(texto)
        }
    }

    override fun escribirLinea(texto: String) {
        if (texto.isNotEmpty() || receptorTexto === RECEPTOR_TEXTO_DEFECTO) {
            receptorLinea(texto)
        } else {
            receptorLinea(texto)
        }
    }

    companion object {
        val RECEPTOR_TEXTO_DEFECTO: (String) -> Unit = { print(it) }
        val RECEPTOR_LINEA_DEFECTO: (String) -> Unit = { println(it) }
    }
}

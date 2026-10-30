package condiciones.io

/**
 * Abstracción de operaciones de salida de texto hacia la interfaz de usuario.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Interface Segregation Principle (ISP): Contrato acotado y dedicado exclusivamente a la emisión de texto.
 * - Dependency Inversion Principle (DIP): Desacopla la lógica de aplicación de `System.out` o `println()`.
 */
interface EscritorConsola {

    /**
     * Escribe un fragmento de texto sin salto de línea.
     *
     * @param texto Mensaje a imprimir.
     */
    fun escribir(texto: String)

    /**
     * Escribe una línea de texto agregando un salto de línea al final.
     *
     * @param texto Mensaje a imprimir (cadena vacía por defecto).
     */
    fun escribirLinea(texto: String = "")
}

/**
 * Implementación predeterminada de [EscritorConsola] que delega en las funciones estándar de consola.
 *
 * @property receptorTexto Función de escritura sin salto inyectable (por defecto `print`).
 * @property receptorLinea Función de escritura con salto inyectable (por defecto `println`).
 */
class EscritorConsolaEstandar(
    private val receptorTexto: (String) -> Unit = RECEPTOR_TEXTO_DEFECTO,
    private val receptorLinea: (String) -> Unit = RECEPTOR_LINEA_DEFECTO
) : EscritorConsola {

    override fun escribir(texto: String) = receptorTexto(texto)

    override fun escribirLinea(texto: String) = receptorLinea(texto)

    companion object {
        val RECEPTOR_TEXTO_DEFECTO: (String) -> Unit = { print(it) }
        val RECEPTOR_LINEA_DEFECTO: (String) -> Unit = { println(it) }
    }
}

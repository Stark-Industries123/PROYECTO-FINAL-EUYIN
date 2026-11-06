package condiciones.io

/**
 * Abstracción de operaciones de salida de texto hacia la interfaz de usuario.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Define exclusivamente el contrato de emisión de texto.
 */
interface EscritorConsola {

    /**
     * Escribe un fragmento de texto sin salto de línea.
     */
    fun escribir(texto: String)

    /**
     * Escribe una línea de texto agregando un salto de línea al final.
     */
    fun escribirLinea(texto: String = "")
}

package ui

/**
 * Componente dedicado estrictamente al renderizado de caracteres ASCII y a la emisión
 * del texto resultante en la terminal a partir de una [ProyeccionGrilla] ya calculada.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Se limita a construir las cadenas de texto del lienzo
 *   ASCII e imprimirlas mediante [escritorLinea]. No realiza cálculos geométricos ni mapeos físicos.
 * - Dependency Inversion Principle (DIP): Recibe la función [escritorLinea] por inyección en el
 *   constructor, desacoplando la salida de `System.out` para facilitar las pruebas unitarias.
 * - Alta Cohesión: Sus métodos operan conjuntamente sobre la configuración simbólica y la salida
 *   textual de consola.
 *
 * @property caracterProyectil Símbolo utilizado para representar los puntos ocupados por el proyectil.
 * @property caracterFondo Símbolo utilizado para representar el espacio vacío del lienzo.
 * @property caracterEjeOrigen Símbolo utilizado para trazar la línea vertical del origen `X = 0`.
 * @property escritorLinea Función receptora para emitir líneas de texto en la terminal.
 */
class RenderizadorConsolaAscii(
    val caracterProyectil: Char = DEFAULT_CARACTER_PROYECTIL,
    val caracterFondo: Char = DEFAULT_CARACTER_FONDO,
    val caracterEjeOrigen: Char = DEFAULT_CARACTER_EJE_ORIGEN,
    private val escritorLinea: (String) -> Unit = ESCRITOR_LINEA_DEFECTO
) {

    /**
     * Construye las filas de texto del lienzo ASCII a partir de una [ProyeccionGrilla].
     *
     * @param proyeccion Proyección matricial previamente calculada.
     * @return Lista inmutable de cadenas de tamaño `proyeccion.filas` y ancho `proyeccion.columnas`.
     */
    fun renderizarLienzo(proyeccion: ProyeccionGrilla): List<String> {
        val matriz = Array(proyeccion.filas) { CharArray(proyeccion.columnas) { caracterFondo } }

        val columnaOrigen = proyeccion.columnaOrigen
        if (columnaOrigen != null && columnaOrigen in 0 until proyeccion.columnas) {
            for (fila in 0 until proyeccion.filas) {
                matriz[fila][columnaOrigen] = caracterEjeOrigen
            }
        }

        for (celda in proyeccion.celdasProyectil) {
            if (celda.estaDentroDe(proyeccion.filas, proyeccion.columnas)) {
                matriz[celda.fila][celda.columna] = caracterProyectil
            }
        }

        return matriz.map { String(it) }
    }

    /**
     * Imprime en la terminal la representación ASCII completa de la [ProyeccionGrilla],
     * incluyendo encabezado y leyenda del eje X cuando corresponde.
     *
     * @param proyeccion Proyección matricial a emitir en consola.
     */
    fun imprimir(proyeccion: ProyeccionGrilla) {
        if (proyeccion.estaVacia()) {
            escritorLinea("No hay puntos en la trayectoria para graficar.")
            return
        }

        escritorLinea("\n--- TRAYECTORIA DEL PROYECTIL (ASCII) ---")
        for (fila in renderizarLienzo(proyeccion)) {
            escritorLinea(fila)
        }
        proyeccion.formatearLeyendaEjeX(caracterEjeOrigen)?.let { escritorLinea(it) }
    }

    companion object {
        val ESCRITOR_LINEA_DEFECTO: (String) -> Unit = { println(it) }
        const val DEFAULT_CARACTER_PROYECTIL: Char = '*'
        const val DEFAULT_CARACTER_FONDO: Char = '.'
        const val DEFAULT_CARACTER_EJE_ORIGEN: Char = '|'
    }
}

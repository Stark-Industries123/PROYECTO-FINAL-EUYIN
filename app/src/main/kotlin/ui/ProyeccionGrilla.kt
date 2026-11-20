package ui

/**
 * Resultado inmutable de proyectar una trayectoria física continua sobre una grilla matricial
 * discreta, desacoplando el cálculo de coordenadas ([MapeadorGrillaTrayectoria]) del renderizado
 * de texto en consola ([RenderizadorConsolaAscii]).
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Encapsula el conjunto de celdas proyectadas y los
 *   metadatos espaciales necesarios para su representación visual, sin acoplarse a E/S ni al motor físico.
 * - Inmutabilidad: Todo su estado se expone mediante propiedades `val` de solo lectura.
 *
 * @property filas Cantidad de filas de la grilla proyectada.
 * @property columnas Cantidad de columnas de la grilla proyectada.
 * @property celdasProyectil Secuencia ordenada de celdas ocupadas por la trayectoria.
 * @property columnaOrigen Índice de columna correspondiente al origen físico `X = 0.0` cuando
 *   existe desplazamiento negativo, o `null` si toda la trayectoria avanza en `X >= 0`.
 * @property minX Coordenada física mínima en el eje X considerada para la proyección.
 * @property maxX Coordenada física máxima en el eje X considerada para la proyección.
 */
data class ProyeccionGrilla(
    val filas: Int,
    val columnas: Int,
    val celdasProyectil: List<CeldaGrilla>,
    val columnaOrigen: Int? = null,
    val minX: Double = 0.0,
    val maxX: Double = 0.0
) {

    init {
        require(filas > 0) { "El número de filas debe ser estrictamente mayor a 0 (recibido: $filas)." }
        require(columnas > 0) { "El número de columnas debe ser estrictamente mayor a 0 (recibido: $columnas)." }
    }

    /**
     * Indica si la proyección no contiene celdas de trayectoria para dibujar.
     */
    fun estaVacia(): Boolean = celdasProyectil.isEmpty()

    /**
     * Indica si la trayectoria proyectada presenta retroceso hacia el Sur (`X < 0`)
     * y por lo tanto incluye una línea vertical de referencia en el origen.
     */
    fun tieneDesplazamientoNegativo(): Boolean = columnaOrigen != null

    /**
     * Formatea la leyenda descriptiva del eje X cuando existe desplazamiento negativo,
     * o retorna `null` si no aplica mostrar la referencia del eje de origen.
     */
    fun formatearLeyendaEjeX(caracterEjeOrigen: Char): String? {
        if (columnaOrigen == null) return null
        return "Eje X [Sur (-X) <-- $caracterEjeOrigen Origen (0.00 m) --> Norte (+X)]: [%.2f m a %.2f m]"
            .format(minX, maxX)
            .replace(',', '.')
    }
}

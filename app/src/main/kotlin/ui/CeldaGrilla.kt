package ui

/**
 * Representa una celda discreta dentro de la grilla matricial ASCII de la consola.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Inmutabilidad y Encapsulamiento: Todas sus propiedades son inmutables (`val`).
 * - Alta Cohesión: Concentra las coordenadas discretas de una celda y las consultas
 *   geométricas sobre los límites de la grilla.
 *
 * @property fila Índice vertical de fila (0 corresponde al borde superior / altura máxima).
 * @property columna Índice horizontal de columna (0 corresponde al borde izquierdo).
 */
data class CeldaGrilla(
    val fila: Int,
    val columna: Int
) {

    /**
     * Indica si la celda se encuentra dentro de los límites válidos de una grilla de
     * dimensiones [filas] x [columnas].
     */
    fun estaDentroDe(filas: Int, columnas: Int): Boolean =
        fila in 0 until filas && columna in 0 until columnas

    /**
     * Retorna una nueva celda desplazada según [deltaFila] y [deltaColumna].
     */
    fun desplazada(deltaFila: Int, deltaColumna: Int): CeldaGrilla =
        CeldaGrilla(fila + deltaFila, columna + deltaColumna)
}

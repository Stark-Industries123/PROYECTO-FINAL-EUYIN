package ui

import fisicas.motor.PuntoTrayectoria
import kotlin.math.roundToInt

/**
 * Mapeador de coordenadas físicas continuas (X, Y) a índices discretos de una grilla ASCII.
 *
 * Responsabilidad única (SRP): encapsular las dimensiones de la grilla y la proyección
 * matemática entre metros y filas/columnas, sin acoplamiento a símbolos ni salida por consola.
 */
class MapeadorGrillaConsola(
    val filas: Int,
    val columnas: Int,
    private val tolerancia: Double = GraficadorConsola.EPSILON
) {

    init {
        require(filas > 0) { "El número de filas debe ser estrictamente mayor a 0 (recibido: $filas)." }
        require(columnas > 0) { "El número de columnas debe ser estrictamente mayor a 0 (recibido: $columnas)." }
    }

    /**
     * Crea una matriz de caracteres con las dimensiones configuradas rellena con [caracterFondo].
     */
    fun crearGrillaVacia(caracterFondo: Char): Array<CharArray> {
        return Array(filas) { CharArray(columnas) { caracterFondo } }
    }

    /**
     * Determina si la trayectoria contiene puntos con desplazamiento negativo en el eje X.
     */
    fun tieneDesplazamientoNegativo(trayectoria: List<PuntoTrayectoria>): Boolean {
        return trayectoria.any { it.x < -tolerancia }
    }

    /**
     * Calcula los límites mínimo y máximo del eje X para la trayectoria dada,
     * garantizando la inclusión del origen (0.0) cuando existen desplazamientos negativos.
     */
    fun calcularLimitesX(trayectoria: List<PuntoTrayectoria>): DoubleArray {
        val hayNegativos = trayectoria.any { it.x < -tolerancia }
        val minX = if (hayNegativos) minOf(0.0, trayectoria.minOf { it.x }) else trayectoria.minOf { it.x }
        val maxX = if (hayNegativos) maxOf(0.0, trayectoria.maxOf { it.x }) else trayectoria.maxOf { it.x }
        return doubleArrayOf(minX, maxX)
    }

    /**
     * Mapea una coordenada física continua X al índice discreto de columna en [0, columnas - 1].
     */
    fun mapearCoordenadaX(x: Double, minX: Double, maxX: Double): Int {
        if (maxX <= minX || (maxX - minX) <= tolerancia) {
            return 0
        }
        val proporcion = ((x - minX) / (maxX - minX)).coerceIn(0.0, 1.0)
        return (proporcion * (columnas - 1)).roundToInt().coerceIn(0, columnas - 1)
    }

    /**
     * Mapea una coordenada física continua Y al índice discreto de fila en [0, filas - 1],
     * invirtiendo el eje vertical para que la fila 0 represente la altura máxima.
     */
    fun mapearCoordenadaY(y: Double, minY: Double, maxY: Double): Int {
        if (maxY <= minY || (maxY - minY) <= tolerancia) {
            return filas - 1
        }
        val proporcion = ((y - minY) / (maxY - minY)).coerceIn(0.0, 1.0)
        val filaInvertida = ((1.0 - proporcion) * (filas - 1)).roundToInt()
        return filaInvertida.coerceIn(0, filas - 1)
    }
}

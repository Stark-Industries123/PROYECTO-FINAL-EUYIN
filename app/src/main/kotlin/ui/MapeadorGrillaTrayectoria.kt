package ui

import fisicas.motor.PuntoTrayectoria
import kotlin.math.roundToInt

/**
 * Servicio dedicado exclusivamente a transformar y mapear los puntos físicos continuos
 * de una trayectoria ([PuntoTrayectoria]) a coordenadas discretas de una grilla matricial
 * ([ProyeccionGrilla] y [CeldaGrilla]).
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Concentra únicamente la lógica matemática de
 *   normalización, inversión del eje vertical e identificación del eje de origen. No realiza
 *   renderizado de caracteres ni operaciones de entrada/salida en consola.
 * - Alta Cohesión: Todas sus operaciones trabajan sobre las dimensiones matriciales ([filas], [columnas]).
 * - Inmutabilidad y Thread-Safety: Su configuración es inmutable y carece de estado mutable compartido.
 *
 * @property filas Cantidad de líneas verticales de la grilla matricial.
 * @property columnas Cantidad de columnas horizontales de la grilla matricial.
 */
class MapeadorGrillaTrayectoria(
    val filas: Int = DEFAULT_FILAS,
    val columnas: Int = DEFAULT_COLUMNAS
) {

    init {
        require(filas > 0) { "El número de filas debe ser estrictamente mayor a 0 (recibido: $filas)." }
        require(columnas > 0) { "El número de columnas debe ser estrictamente mayor a 0 (recibido: $columnas)." }
    }

    /**
     * Transforma la colección de puntos físicos en una [ProyeccionGrilla] con las celdas
     * discretas del proyectil y la columna del eje de origen cuando existe retroceso en X.
     *
     * @param trayectoria Colección inmutable de [PuntoTrayectoria] calculada por el motor físico.
     * @return Proyección matricial lista para ser consumida por un renderizador de texto.
     */
    fun mapear(trayectoria: List<PuntoTrayectoria>): ProyeccionGrilla {
        if (trayectoria.isEmpty()) {
            return ProyeccionGrilla(filas = filas, columnas = columnas, celdasProyectil = emptyList())
        }

        val hayNegativos = tieneDesplazamientoNegativo(trayectoria)
        val minX = if (hayNegativos) minOf(0.0, trayectoria.minOf { it.x }) else trayectoria.minOf { it.x }
        val maxX = if (hayNegativos) maxOf(0.0, trayectoria.maxOf { it.x }) else trayectoria.maxOf { it.x }
        val minY = minOf(0.0, trayectoria.minOf { it.y })
        val maxY = trayectoria.maxOf { it.y }

        val columnaOrigen = if (hayNegativos) mapearCoordenadaX(0.0, minX, maxX) else null
        val celdas = trayectoria.map { punto ->
            CeldaGrilla(
                fila = mapearCoordenadaY(punto.y, minY, maxY),
                columna = mapearCoordenadaX(punto.x, minX, maxX)
            )
        }

        return ProyeccionGrilla(
            filas = filas,
            columnas = columnas,
            celdasProyectil = celdas,
            columnaOrigen = columnaOrigen,
            minX = minX,
            maxX = maxX
        )
    }

    /**
     * Determina si la trayectoria contiene puntos con desplazamiento negativo en el eje X
     * (retroceso hacia el Sur).
     */
    fun tieneDesplazamientoNegativo(trayectoria: List<PuntoTrayectoria>): Boolean {
        return trayectoria.any { it.x < -EPSILON }
    }

    /**
     * Mapea una coordenada física continua X al índice discreto de columna dentro de la grilla.
     * Si el alcance en X es nulo o despreciable ([maxX] <= [minX] o diferencia <= [EPSILON]),
     * todos los puntos se mapean a la columna 0 para representar estrictamente el tiro vertical.
     *
     * @param x Coordenada física horizontal en metros.
     * @param minX Valor mínimo de X en la trayectoria.
     * @param maxX Valor máximo de X en la trayectoria.
     * @return Índice de columna comprendido en `[0, columnas - 1]`.
     */
    fun mapearCoordenadaX(x: Double, minX: Double, maxX: Double): Int {
        if (maxX <= minX || (maxX - minX) <= EPSILON) {
            return 0
        }
        val proporcion = ((x - minX) / (maxX - minX)).coerceIn(0.0, 1.0)
        return (proporcion * (columnas - 1)).roundToInt().coerceIn(0, columnas - 1)
    }

    /**
     * Mapea una coordenada física continua Y al índice discreto de fila dentro de la grilla.
     * Invierte el eje vertical: la fila 0 representa la altura máxima ([maxY]) y la fila
     * `filas - 1` representa la altura mínima del suelo ([minY]).
     *
     * @param y Coordenada física vertical en metros.
     * @param minY Valor mínimo de Y en la trayectoria (referenciado al suelo).
     * @param maxY Valor máximo de Y en la trayectoria.
     * @return Índice de fila comprendido en `[0, filas - 1]`.
     */
    fun mapearCoordenadaY(y: Double, minY: Double, maxY: Double): Int {
        if (maxY <= minY || (maxY - minY) <= EPSILON) {
            return filas - 1
        }
        val proporcion = ((y - minY) / (maxY - minY)).coerceIn(0.0, 1.0)
        val filaInvertida = ((1.0 - proporcion) * (filas - 1)).roundToInt()
        return filaInvertida.coerceIn(0, filas - 1)
    }

    companion object {
        const val DEFAULT_FILAS: Int = 15
        const val DEFAULT_COLUMNAS: Int = 60
        const val EPSILON: Double = 1e-6
    }
}

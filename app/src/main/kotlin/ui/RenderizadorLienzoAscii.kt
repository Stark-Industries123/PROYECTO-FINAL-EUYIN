package ui

import fisicas.motor.PuntoTrayectoria

/**
 * Renderizador encargado de construir el lienzo ASCII (lista de líneas) y la leyenda del eje X
 * a partir de una colección de [PuntoTrayectoria], utilizando [MapeadorGrillaConsola] para
 * proyectar las coordenadas sobre la matriz.
 */
class RenderizadorLienzoAscii(
    val mapeador: MapeadorGrillaConsola,
    val caracterProyectil: Char,
    val caracterFondo: Char,
    val caracterEjeOrigen: Char
) {

    /**
     * Renderiza la colección de puntos en una lista de cadenas con tamaño [MapeadorGrillaConsola.filas]
     * y longitud [MapeadorGrillaConsola.columnas] por línea.
     */
    fun renderizar(trayectoria: List<PuntoTrayectoria>): List<String> {
        val matriz = mapeador.crearGrillaVacia(caracterFondo)
        if (trayectoria.isEmpty()) {
            return matriz.map { String(it) }
        }

        val limitesX = mapeador.calcularLimitesX(trayectoria)
        if (mapeador.tieneDesplazamientoNegativo(trayectoria)) {
            trazarEjeOrigen(matriz, limitesX[0], limitesX[1])
        }
        trazarPuntosTrayectoria(matriz, trayectoria, limitesX)
        return matriz.map { String(it) }
    }

    /**
     * Construye la leyenda explicativa del eje X cuando existen desplazamientos negativos hacia el Sur.
     * Retorna `null` si toda la trayectoria permanece en X >= 0.
     */
    fun construirLeyendaEjeX(trayectoria: List<PuntoTrayectoria>): String? {
        if (!mapeador.tieneDesplazamientoNegativo(trayectoria)) {
            return null
        }
        val limitesX = mapeador.calcularLimitesX(trayectoria)
        return "Eje X [Sur (-X) <-- $caracterEjeOrigen Origen (0.00 m) --> Norte (+X)]: [%.2f m a %.2f m]"
            .format(limitesX[0], limitesX[1])
            .replace(',', '.')
    }

    private fun trazarEjeOrigen(matriz: Array<CharArray>, minX: Double, maxX: Double) {
        val columnaOrigen = mapeador.mapearCoordenadaX(0.0, minX, maxX)
        for (fila in 0 until mapeador.filas) {
            matriz[fila][columnaOrigen] = caracterEjeOrigen
        }
    }

    private fun trazarPuntosTrayectoria(
        matriz: Array<CharArray>,
        trayectoria: List<PuntoTrayectoria>,
        limitesX: DoubleArray
    ) {
        val minY = minOf(0.0, trayectoria.minOf { it.y })
        val maxY = trayectoria.maxOf { it.y }
        for (punto in trayectoria) {
            val fila = mapeador.mapearCoordenadaY(punto.y, minY, maxY)
            val columna = mapeador.mapearCoordenadaX(punto.x, limitesX[0], limitesX[1])
            matriz[fila][columna] = caracterProyectil
        }
    }
}

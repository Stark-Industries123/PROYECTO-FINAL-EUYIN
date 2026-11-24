package ui

import fisicas.motor.PuntoTrayectoria

/**
 * Componente de interfaz de usuario por consola encargado de emitir la trayectoria física
 * en formato ASCII utilizando [RenderizadorLienzoAscii] y [MapeadorGrillaConsola].
 *
 * Principios de Diseño SOLID y POO:
 * - Single Responsibility Principle (SRP): Coordina la impresión por consola y delega
 *   el mapeo de coordenadas a [MapeadorGrillaConsola] y la construcción de la grilla a
 *   [RenderizadorLienzoAscii].
 * - Dependency Inversion Principle (DIP): Admite la inyección de [escritorLinea] para
 *   desacoplar la salida estándar y facilitar las pruebas unitarias.
 */
class GraficadorConsola(
    filas: Int = DEFAULT_FILAS,
    columnas: Int = DEFAULT_COLUMNAS,
    caracterProyectil: Char = DEFAULT_CARACTER_PROYECTIL,
    caracterFondo: Char = DEFAULT_CARACTER_FONDO,
    private val escritorLinea: (String) -> Unit = ESCRITOR_LINEA_DEFECTO,
    caracterEjeOrigen: Char = DEFAULT_CARACTER_EJE_ORIGEN
) : Graficador {

    private val renderizador: RenderizadorLienzoAscii = RenderizadorLienzoAscii(
        mapeador = MapeadorGrillaConsola(filas, columnas, EPSILON),
        caracterProyectil = caracterProyectil,
        caracterFondo = caracterFondo,
        caracterEjeOrigen = caracterEjeOrigen
    )

    fun obtenerFilas(): Int = renderizador.mapeador.filas
    fun obtenerColumnas(): Int = renderizador.mapeador.columnas
    fun obtenerCaracterProyectil(): Char = renderizador.caracterProyectil
    fun obtenerCaracterFondo(): Char = renderizador.caracterFondo
    fun obtenerCaracterEjeOrigen(): Char = renderizador.caracterEjeOrigen

    /**
     * Dibuja la trayectoria en la consola emitiendo el encabezado, las filas ASCII y la leyenda.
     */
    override fun graficar(trayectoria: List<PuntoTrayectoria>) {
        if (trayectoria.isEmpty()) {
            escritorLinea("No hay puntos en la trayectoria para graficar.")
            return
        }

        escritorLinea("\n--- TRAYECTORIA DEL PROYECTIL (ASCII) ---")
        for (fila in renderizador.renderizar(trayectoria)) {
            escritorLinea(fila)
        }
        renderizador.construirLeyendaEjeX(trayectoria)?.let { escritorLinea(it) }
    }

    /**
     * Renderiza la colección de puntos en una lista de cadenas de texto ASCII.
     */
    fun renderizar(trayectoria: List<PuntoTrayectoria>): List<String> {
        return renderizador.renderizar(trayectoria)
    }

    /**
     * Determina si la trayectoria contiene puntos con desplazamiento negativo en el eje X.
     */
    internal fun tieneDesplazamientoNegativo(trayectoria: List<PuntoTrayectoria>): Boolean {
        return renderizador.mapeador.tieneDesplazamientoNegativo(trayectoria)
    }

    /**
     * Mapea una coordenada física continua X al índice discreto de columna dentro de la matriz.
     */
    internal fun mapearCoordenadaX(x: Double, minX: Double, maxX: Double): Int {
        return renderizador.mapeador.mapearCoordenadaX(x, minX, maxX)
    }

    /**
     * Mapea una coordenada física continua Y al índice discreto de fila dentro de la matriz.
     */
    internal fun mapearCoordenadaY(y: Double, minY: Double, maxY: Double): Int {
        return renderizador.mapeador.mapearCoordenadaY(y, minY, maxY)
    }

    companion object {
        val ESCRITOR_LINEA_DEFECTO: (String) -> Unit = { println(it) }
        const val DEFAULT_FILAS: Int = 15
        const val DEFAULT_COLUMNAS: Int = 60
        const val DEFAULT_CARACTER_PROYECTIL: Char = '*'
        const val DEFAULT_CARACTER_FONDO: Char = '.'
        const val DEFAULT_CARACTER_EJE_ORIGEN: Char = '|'
        const val EPSILON: Double = 1e-6
    }
}

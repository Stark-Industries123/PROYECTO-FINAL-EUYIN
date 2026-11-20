package ui

import fisicas.motor.PuntoTrayectoria
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Administra el estado de avance de la animación de una trayectoria y calcula las proyecciones
 * geométricas hacia coordenadas de pantalla y marcas de altura HUD.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Concentra exclusivamente el cálculo matemático y el
 *   estado de la animación, manteniéndose 100 % desacoplado de Swing y de `Graphics2D`.
 * - Alta Cohesión: Todos sus métodos operan sobre el estado de los puntos de la trayectoria.
 */
class EstadoAnimacionTrayectoria(
    private val puntosPorFrame: Int = PUNTOS_POR_FRAME_DEFAULT
) {

    private var puntos: List<PuntoTrayectoria> = emptyList()
    private var indiceFrameActual: Int = 0
    private var enCurso: Boolean = false

    /**
     * Reinicia el estado de animación con una nueva colección de [PuntoTrayectoria].
     */
    fun cargarTrayectoria(trayectoria: List<PuntoTrayectoria>) {
        puntos = trayectoria
        indiceFrameActual = if (trayectoria.isEmpty()) 0 else 1
        enCurso = trayectoria.size > 1
    }

    /**
     * Avanza la animación un bloque de frames y retorna `true` si alcanzó el último punto.
     */
    fun avanzarFrame(): Boolean {
        if (indiceFrameActual < puntos.size) {
            val salto = max(1, puntosPorFrame)
            indiceFrameActual = min(puntos.size, indiceFrameActual + salto)
        }
        val finalizada = indiceFrameActual >= puntos.size
        if (finalizada) {
            enCurso = false
        }
        return finalizada
    }

    /**
     * Salta directamente al último punto de la trayectoria y marca la animación como finalizada.
     * Retorna `true` si existían puntos cargados para completar.
     */
    fun completar(): Boolean {
        enCurso = false
        if (puntos.isEmpty()) return false
        indiceFrameActual = puntos.size
        return true
    }

    /**
     * Pausa el avance de la animación preservando los puntos ya cargados.
     */
    fun detener() {
        if (puntos.isNotEmpty() || enCurso) {
            enCurso = false
        }
    }

    /**
     * Indica si la trayectoria cargada requiere animación progresiva frame a frame.
     */
    fun estaEnCurso(): Boolean = enCurso && puntos.size > 1

    /**
     * Indica si existen puntos listos para dibujarse en el frame actual.
     */
    fun tienePuntosVisibles(): Boolean = puntos.isNotEmpty() && indiceFrameActual > 0

    /**
     * Retorna la colección completa de puntos cargada para la simulación actual.
     */
    fun obtenerPuntos(): List<PuntoTrayectoria> = puntos

    /**
     * Proyecta los puntos visibles hasta el frame actual a coordenadas de píxeles en pantalla.
     */
    fun calcularProyeccionVisible(ancho: Int, alto: Int): List<PuntoProyectado> {
        if (puntos.isEmpty() || indiceFrameActual <= 0) return emptyList()
        val (minX, escala) = calcularEscala(ancho, alto)
        val origenX = (ancho * PROPORCION_ORIGEN_X).toInt()
        val nivelSueloY = (alto * PROPORCION_SUELO).toInt()
        val limite = min(indiceFrameActual, puntos.size)
        return List(limite) { indice ->
            val punto = puntos[indice]
            val px = origenX + ((punto.x - minX) * escala).toInt()
            val py = nivelSueloY - (punto.y * escala).toInt()
            PuntoProyectado(px, py, punto.estaEnSuelo())
        }
    }

    /**
     * Calcula las marcas graduadas de la regla vertical de altura HUD para las dimensiones dadas.
     */
    fun calcularMarcasRegla(ancho: Int, alto: Int): List<MarcaAlturaHud> {
        val escala = calcularEscala(ancho, alto).second
        val intervalo = calcularIntervaloRegla(escala)
        val nivelSueloY = (alto * PROPORCION_SUELO).toInt()
        val topeReglaY = max(MARGEN_SUPERIOR_REGLA_Y, (nivelSueloY - alto * PROPORCION_ALTO_UTIL).toInt())
        if (nivelSueloY <= topeReglaY) return emptyList()
        val marcas = mutableListOf<MarcaAlturaHud>()
        var alturaMetros = 0
        while (true) {
            val py = nivelSueloY - (alturaMetros * escala).roundToInt()
            if (py < topeReglaY) break
            marcas.add(MarcaAlturaHud(py, alturaMetros))
            alturaMetros += intervalo
        }
        return marcas
    }

    private fun calcularEscala(ancho: Int, alto: Int): Pair<Double, Double> {
        val minX = min(0.0, puntos.minOfOrNull { it.x } ?: 0.0)
        val maxX = max(1.0, puntos.maxOfOrNull { it.x } ?: ALTURA_MAXIMA_DEFAULT)
        val rangoX = max(1.0, maxX - minX)
        val rangoY = max(1.0, puntos.maxOfOrNull { it.y } ?: ALTURA_MAXIMA_DEFAULT)
        val anchoUtil = max(1.0, ancho * PROPORCION_ANCHO_UTIL)
        val altoUtil = max(1.0, alto * PROPORCION_ALTO_UTIL)
        return minX to min(anchoUtil / rangoX, altoUtil / rangoY)
    }

    private fun calcularIntervaloRegla(escala: Double): Int {
        val maxY = max(1.0, puntos.maxOfOrNull { it.y } ?: ALTURA_MAXIMA_DEFAULT)
        val pasoObjetivo = max(1.0, maxY / DIVISIONES_OBJETIVO_REGLA)
        var indice = ESCALONES_REGLA_METROS.indexOfFirst { it >= pasoObjetivo }
        if (indice < 0) indice = ESCALONES_REGLA_METROS.lastIndex
        while (indice < ESCALONES_REGLA_METROS.lastIndex &&
            ESCALONES_REGLA_METROS[indice] * escala < SEPARACION_MINIMA_MARCAS_PX
        ) {
            indice++
        }
        return ESCALONES_REGLA_METROS[indice]
    }

    companion object {
        const val PUNTOS_POR_FRAME_DEFAULT: Int = 12
        const val PROPORCION_SUELO: Double = 0.86
        const val PROPORCION_ALTO_UTIL: Double = 0.70
        const val MARGEN_SUPERIOR_REGLA_Y: Int = 56

        private const val PROPORCION_ORIGEN_X: Double = 0.08
        private const val PROPORCION_ANCHO_UTIL: Double = 0.84
        private const val ALTURA_MAXIMA_DEFAULT: Double = 100.0
        private const val DIVISIONES_OBJETIVO_REGLA: Double = 5.0
        private const val SEPARACION_MINIMA_MARCAS_PX: Double = 28.0
        private val ESCALONES_REGLA_METROS = listOf(1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000)
    }
}

package ui

import fisicas.motor.PuntoTrayectoria
import javax.swing.Timer
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Colaborador encargado de gestionar el estado de la animación de trayectoria (temporizador y frames),
 * los cálculos de escala del escenario y la proyección de coordenadas físicas a píxeles de pantalla.
 */
class ControladorAnimacionTrayectoria(
    val cargadorFondo: CargadorFondoPlaneta,
    private val puntosPorFrame: Int,
    private val alActualizarFrame: () -> Unit,
    private var alFinalizarAnimacion: () -> Unit
) {

    private val proporcionSuelo: Double = 0.86
    private val proporcionAltoUtil: Double = 0.70
    private var puntos: List<PuntoTrayectoria> = emptyList()
    private var indiceFrameActual: Int = 0
    private val timerAnimacion: Timer = Timer(INTERVALO_FRAME_MS) { avanzarFrame() }.apply {
        initialDelay = 0
        isCoalesce = true
    }

    /**
     * Registra el callback a ejecutar cuando la animación alcanza el último punto.
     */
    fun establecerAlFinalizarAnimacion(callback: () -> Unit) {
        alFinalizarAnimacion = callback
    }

    /**
     * Carga una nueva trayectoria e inicia la animación progresiva a 60 FPS.
     */
    fun iniciarAnimacion(trayectoria: List<PuntoTrayectoria>) {
        timerAnimacion.stop()
        puntos = trayectoria
        indiceFrameActual = if (trayectoria.isEmpty()) 0 else 1
        if (trayectoria.size > 1) {
            timerAnimacion.start()
            alActualizarFrame()
        } else if (trayectoria.size == 1) {
            alActualizarFrame()
            alFinalizarAnimacion()
        } else {
            alActualizarFrame()
        }
    }

    /**
     * Detiene el temporizador de animación si se encuentra en ejecución.
     */
    fun detenerAnimacion() {
        if (timerAnimacion.isRunning) {
            timerAnimacion.stop()
        }
    }

    /**
     * Interrumpe la animación y posiciona el renderizado directamente en el último punto.
     */
    fun completarAnimacion() {
        if (timerAnimacion.isRunning) {
            timerAnimacion.stop()
        }
        if (puntos.isNotEmpty()) {
            indiceFrameActual = puntos.size
            alActualizarFrame()
            alFinalizarAnimacion()
        }
    }

    /**
     * Retorna la colección completa de puntos cargada para la simulación actual.
     */
    fun obtenerPuntos(): List<PuntoTrayectoria> = puntos

    /**
     * Calcula los límites verticales (suelo y tope) de la regla HUD para una altura de lienzo dada.
     */
    fun calcularLimitesReglaY(alto: Int): IntArray {
        val nivelSueloY = (alto * proporcionSuelo).toInt()
        val topeReglaY = max(MARGEN_SUPERIOR_REGLA_Y, (nivelSueloY - alto * proporcionAltoUtil).toInt())
        return intArrayOf(nivelSueloY, topeReglaY)
    }

    /**
     * Calcula las marcas `(posY, etiqueta)` de la regla vertical de altura en el HUD.
     */
    fun calcularMarcasRegla(ancho: Int, alto: Int): List<Pair<Int, String>> {
        val escala = calcularEscalasEscenario(ancho, alto)[1]
        val maxY = max(1.0, puntos.maxOfOrNull { it.y } ?: ALTURA_MAXIMA_DEFAULT)
        val pasoObjetivo = max(1.0, maxY / DIVISIONES_OBJETIVO_REGLA)
        val intervalo = ESCALONES_REGLA_METROS.firstOrNull {
            it >= pasoObjetivo && it * escala >= SEPARACION_MINIMA_MARCAS_PX
        } ?: ESCALONES_REGLA_METROS.last()
        val limites = calcularLimitesReglaY(alto)
        if (limites[0] <= limites[1]) return emptyList()
        val marcas = mutableListOf<Pair<Int, String>>()
        var alturaMetros = 0
        while (true) {
            val py = limites[0] - (alturaMetros * escala).roundToInt()
            if (py < limites[1]) break
            marcas.add(py to "$alturaMetros m")
            alturaMetros += intervalo
        }
        return marcas
    }

    /**
     * Proyecta a coordenadas de pantalla `[px, py, enSuelo]` todos los puntos visibles hasta el frame actual.
     */
    fun proyectarPuntosVisibles(ancho: Int, alto: Int): List<IntArray> {
        if (puntos.isEmpty() || indiceFrameActual <= 0) return emptyList()
        val escalas = calcularEscalasEscenario(ancho, alto)
        val limite = min(indiceFrameActual, puntos.size)
        val origenX = (ancho * 0.08).toInt()
        val nivelSueloY = (alto * proporcionSuelo).toInt()
        val escala = escalas[1]
        return List(limite) { indice ->
            val punto = puntos[indice]
            val px = origenX + ((punto.x - escalas[0]) * escala).toInt()
            val py = nivelSueloY - (punto.y * escala).toInt()
            intArrayOf(px, py, if (punto.estaEnSuelo()) 1 else 0)
        }
    }

    private fun calcularEscalasEscenario(ancho: Int, alto: Int): DoubleArray {
        val minX = min(0.0, puntos.minOfOrNull { it.x } ?: 0.0)
        val maxX = max(1.0, puntos.maxOfOrNull { it.x } ?: ALTURA_MAXIMA_DEFAULT)
        val rangoX = max(1.0, maxX - minX)
        val rangoY = max(1.0, puntos.maxOfOrNull { it.y } ?: ALTURA_MAXIMA_DEFAULT)
        val anchoUtil = max(1.0, ancho * 0.84)
        val altoUtil = max(1.0, alto * proporcionAltoUtil)
        val escala = min(anchoUtil / rangoX, altoUtil / rangoY)
        return doubleArrayOf(minX, escala, escala)
    }

    private fun avanzarFrame() {
        if (indiceFrameActual < puntos.size) {
            val salto = max(1, puntosPorFrame)
            indiceFrameActual = min(puntos.size, indiceFrameActual + salto)
            alActualizarFrame()
            if (indiceFrameActual >= puntos.size) {
                timerAnimacion.stop()
                alFinalizarAnimacion()
            }
        } else {
            timerAnimacion.stop()
        }
    }

    companion object {
        private const val INTERVALO_FRAME_MS: Int = 16
        private const val ALTURA_MAXIMA_DEFAULT: Double = 100.0
        private const val DIVISIONES_OBJETIVO_REGLA: Double = 5.0
        private const val SEPARACION_MINIMA_MARCAS_PX: Double = 28.0
        private const val MARGEN_SUPERIOR_REGLA_Y: Int = 56
        private val ESCALONES_REGLA_METROS = intArrayOf(1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000)
    }
}

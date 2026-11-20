package ui

import fisicas.motor.PuntoTrayectoria
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel
import javax.swing.Timer

/**
 * Lienzo Swing del escenario de juego que implementa [Graficador].
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Coordina únicamente el ciclo de vida del temporizador
 *   Swing (60 FPS) y el evento [paintComponent], delegando el cálculo del estado de animación a
 *   [EstadoAnimacionTrayectoria] y el dibujado `Graphics2D` a [RenderizadorTrayectoria].
 */
class PanelTrayectoria(
    planetaInicial: String = "Tierra",
    puntosPorFrame: Int = EstadoAnimacionTrayectoria.PUNTOS_POR_FRAME_DEFAULT,
    private var alFinalizarAnimacion: () -> Unit = {}
) : JPanel(), Graficador {

    private val estadoAnimacion = EstadoAnimacionTrayectoria(puntosPorFrame)
    private val renderizador = RenderizadorTrayectoria(CargadorFondoPlaneta(planetaInicial))
    private val timerAnimacion: Timer = Timer(INTERVALO_FRAME_MS) { avanzarFrame() }.apply {
        initialDelay = 0
        isCoalesce = true
    }

    init {
        isOpaque = true
    }

    /**
     * Registra un callback que se ejecuta cuando la animación termina de dibujar el último punto.
     */
    fun establecerAlFinalizarAnimacion(callback: () -> Unit) {
        alFinalizarAnimacion = callback
    }

    /**
     * Establece el nombre del planeta actual cuyo fondo se renderizará en [paintComponent].
     */
    fun establecerPlaneta(planeta: String) {
        renderizador.establecerPlaneta(planeta)
        repaint()
    }

    /**
     * Retorna el nombre del planeta actualmente seleccionado en el panel.
     */
    fun obtenerPlanetaActual(): String = renderizador.obtenerPlanetaActual()

    /**
     * Recibe la lista completa de [PuntoTrayectoria] del motor físico e inicia la animación
     * progresiva a 60 FPS avanzando en bloques de puntos por cada tick del temporizador.
     */
    override fun graficar(trayectoria: List<PuntoTrayectoria>) {
        timerAnimacion.stop()
        estadoAnimacion.cargarTrayectoria(trayectoria)
        if (estadoAnimacion.estaEnCurso()) {
            timerAnimacion.start()
            repaint()
        } else if (estadoAnimacion.tienePuntosVisibles()) {
            repaint()
            notificarFinAnimacion()
        } else {
            repaint()
        }
    }

    /**
     * Detiene el temporizador de animación activo.
     */
    fun detenerAnimacion() {
        if (timerAnimacion.isRunning) {
            timerAnimacion.stop()
        }
        estadoAnimacion.detener()
    }

    /**
     * Interrumpe la animación en curso, posiciona el dibujado en el último punto, repinta
     * la trayectoria completa de inmediato y dispara el callback de finalización.
     */
    fun completarAnimacion() {
        if (timerAnimacion.isRunning) {
            timerAnimacion.stop()
        }
        if (estadoAnimacion.completar()) {
            repaint()
            notificarFinAnimacion()
        }
    }

    /**
     * Retorna la colección completa de puntos cargada para la simulación actual.
     */
    fun obtenerPuntos(): List<PuntoTrayectoria> = estadoAnimacion.obtenerPuntos()

    private fun avanzarFrame() {
        val finalizada = estadoAnimacion.avanzarFrame()
        repaint()
        if (finalizada) {
            timerAnimacion.stop()
            notificarFinAnimacion()
        }
    }

    private fun notificarFinAnimacion() {
        if (isShowing && width > 0 && height > 0) {
            paintImmediately(0, 0, width, height)
        }
        alFinalizarAnimacion()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        renderizador.dibujarEscenario(g as Graphics2D, estadoAnimacion, width, height)
    }

    companion object {
        private const val INTERVALO_FRAME_MS: Int = 16
    }
}

package ui

import fisicas.motor.PuntoTrayectoria
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JPanel

/**
 * Lienzo Swing encargado exclusivamente de renderizar con [Graphics2D] el fondo del planeta,
 * la regla vertical de altura HUD y la estela/proyectil de la trayectoria.
 *
 * Delega la gestión del estado de animación, temporizador y cálculos de escala/proyección
 * a [ControladorAnimacionTrayectoria] respetando el Principio de Responsabilidad Única (SRP).
 */
class PanelTrayectoria(
    planetaInicial: String = "Tierra",
    puntosPorFrame: Int = PUNTOS_POR_FRAME_DEFAULT,
    alFinalizarAnimacion: () -> Unit = {}
) : JPanel(), Graficador {

    private var callbackFinAnimacion: () -> Unit = alFinalizarAnimacion
    private val controlador: ControladorAnimacionTrayectoria = ControladorAnimacionTrayectoria(
        cargadorFondo = CargadorFondoPlaneta(planetaInicial),
        puntosPorFrame = puntosPorFrame,
        alActualizarFrame = { repaint() },
        alFinalizarAnimacion = { notificarFinAnimacion() }
    )

    init {
        isOpaque = true
    }

    /**
     * Registra un callback que se ejecuta cuando la animación termina de dibujar el último punto.
     */
    fun establecerAlFinalizarAnimacion(callback: () -> Unit) {
        callbackFinAnimacion = callback
        controlador.establecerAlFinalizarAnimacion { notificarFinAnimacion() }
    }

    /**
     * Establece el nombre del planeta actual cuyo fondo se renderizará en [paintComponent].
     */
    fun establecerPlaneta(planeta: String) {
        controlador.cargadorFondo.establecerPlaneta(planeta)
        repaint()
    }

    /**
     * Retorna el nombre del planeta actualmente seleccionado en el panel.
     */
    fun obtenerPlanetaActual(): String = controlador.cargadorFondo.obtenerPlanetaActual()

    /**
     * Actualiza el planeta o ruta del recurso de fondo y solicita repintado.
     */
    fun cargarFondo(rutaOPlaneta: String) {
        controlador.cargadorFondo.establecerPlaneta(rutaOPlaneta)
        repaint()
    }

    /**
     * Recibe la lista completa de [PuntoTrayectoria] del motor físico e inicia la animación.
     */
    override fun graficar(trayectoria: List<PuntoTrayectoria>) {
        controlador.iniciarAnimacion(trayectoria)
    }

    /**
     * Detiene el temporizador de animación activo.
     */
    fun detenerAnimacion() {
        controlador.detenerAnimacion()
    }

    /**
     * Completa de inmediato la animación mostrando toda la trayectoria y notificando el fin.
     */
    fun completarAnimacion() {
        controlador.completarAnimacion()
    }

    /**
     * Retorna la colección completa de puntos cargada para la simulación actual.
     */
    fun obtenerPuntos(): List<PuntoTrayectoria> = controlador.obtenerPuntos()

    private fun notificarFinAnimacion() {
        if (isShowing && width > 0 && height > 0) {
            paintImmediately(0, 0, width, height)
        }
        callbackFinAnimacion()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        dibujarFondo(g2d)
        dibujarReglaAlturaHud(g2d)
        dibujarProyectilYEstela(g2d)
    }

    private fun dibujarFondo(g2d: Graphics2D) {
        val imagen = controlador.cargadorFondo.obtenerImagen()
        if (imagen != null) {
            g2d.drawImage(imagen, 0, 0, width, height, this)
        } else {
            g2d.color = Color(15, 23, 42)
            g2d.fillRect(0, 0, width, height)
        }
    }

    private fun dibujarReglaAlturaHud(g2d: Graphics2D) {
        val limites = controlador.calcularLimitesReglaY(height)
        val marcas = controlador.calcularMarcasRegla(width, height)
        if (limites[0] <= limites[1]) return
        g2d.color = Color(0, 0, 0, 170)
        g2d.stroke = BasicStroke(3.0f)
        g2d.drawLine(15, limites[0], 15, limites[1])
        g2d.color = Color(248, 250, 252, 220)
        g2d.stroke = BasicStroke(1.8f)
        g2d.drawLine(14, limites[0], 14, limites[1])
        g2d.font = Font("SansSerif", Font.BOLD, 11)
        for ((py, etiqueta) in marcas) {
            g2d.color = Color(0, 0, 0, 190)
            g2d.stroke = BasicStroke(2.5f)
            g2d.drawLine(11, py + 1, 23, py + 1)
            g2d.color = Color.WHITE
            g2d.stroke = BasicStroke(2.0f)
            g2d.drawLine(10, py, 22, py)
            val anchoTexto = g2d.fontMetrics.stringWidth(etiqueta)
            g2d.color = Color(15, 23, 42, 175)
            g2d.fillRoundRect(24, py - 9, anchoTexto + 8, 16, 6, 6)
            g2d.color = Color(0, 0, 0, 220)
            g2d.drawString(etiqueta, 29, py + 5)
            g2d.color = Color.WHITE
            g2d.drawString(etiqueta, 28, py + 4)
        }
    }

    private fun dibujarProyectilYEstela(g2d: Graphics2D) {
        val proyectados = controlador.proyectarPuntosVisibles(width, height)
        if (proyectados.isEmpty()) return
        var anterior: IntArray? = null
        for (actual in proyectados) {
            if (anterior != null) {
                g2d.color = Color(250, 204, 21, 190)
                g2d.stroke = BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
                g2d.drawLine(anterior[0], anterior[1], actual[0], actual[1])
            }
            if (actual[2] == 1) {
                g2d.color = Color(249, 115, 22, 210)
                g2d.fillOval(actual[0] - 5, actual[1] - 5, 10, 10)
            }
            anterior = actual
        }
        val ultimo = proyectados.last()
        g2d.color = Color(239, 68, 68)
        g2d.fillOval(ultimo[0] - RADIO_PROYECTIL, ultimo[1] - RADIO_PROYECTIL, RADIO_PROYECTIL * 2, RADIO_PROYECTIL * 2)
        g2d.color = Color.WHITE
        g2d.stroke = BasicStroke(1.5f)
        g2d.drawOval(ultimo[0] - RADIO_PROYECTIL, ultimo[1] - RADIO_PROYECTIL, RADIO_PROYECTIL * 2, RADIO_PROYECTIL * 2)
    }

    companion object {
        private const val PUNTOS_POR_FRAME_DEFAULT: Int = 12
        private const val RADIO_PROYECTIL: Int = 7
    }
}

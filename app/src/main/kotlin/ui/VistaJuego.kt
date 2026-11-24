package ui

import fisicas.motor.PuntoTrayectoria
import java.awt.BorderLayout
import java.awt.Color
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * Pantalla del escenario de juego que combina el lienzo [PanelTrayectoria] con la barra
 * superior de navegación rápida y el resumen de métricas en tiempo real.
 */
class VistaJuego(
    fabrica: FabricaComponentesGui,
    acciones: AccionesJuego,
    private val panelTrayectoria: PanelTrayectoria = PanelTrayectoria()
) : JPanel(BorderLayout()) {

    private val etiquetaResumen = JLabel("Listo para jugar.")

    init {
        add(panelTrayectoria, BorderLayout.CENTER)
        panelTrayectoria.layout = BorderLayout()
        panelTrayectoria.add(construirBarraSuperior(fabrica, acciones), BorderLayout.NORTH)
    }

    /**
     * Actualiza el texto de resumen o error visible en el HUD superior.
     */
    fun actualizarResumen(texto: String) {
        etiquetaResumen.text = texto
    }

    /**
     * Retorna el mensaje actualmente mostrado en el HUD superior.
     */
    fun obtenerTextoResumen(): String = etiquetaResumen.text

    /**
     * Detiene la animación en curso del lienzo de trayectoria.
     */
    fun detenerAnimacion() {
        panelTrayectoria.detenerAnimacion()
    }

    /**
     * Completa de inmediato la animación en curso mostrando todos los puntos.
     */
    fun completarAnimacion() {
        panelTrayectoria.completarAnimacion()
    }

    /**
     * Actualiza el planeta de fondo y, si se provee [trayectoria], inicia su animación.
     */
    fun mostrarEscenario(planeta: String, trayectoria: List<PuntoTrayectoria>? = null) {
        panelTrayectoria.establecerPlaneta(planeta)
        if (trayectoria != null) {
            panelTrayectoria.graficar(trayectoria)
        }
    }

    /**
     * Registra la acción a ejecutar al finalizar la animación de la trayectoria.
     */
    fun establecerAlFinalizarAnimacion(callback: () -> Unit) {
        panelTrayectoria.establecerAlFinalizarAnimacion(callback)
    }

    /**
     * Retorna los puntos de trayectoria actualmente cargados en el lienzo.
     */
    fun obtenerPuntos(): List<PuntoTrayectoria> = panelTrayectoria.obtenerPuntos()

    private fun construirBarraSuperior(
        fabrica: FabricaComponentesGui,
        acciones: AccionesJuego
    ): JPanel {
        etiquetaResumen.foreground = Color.WHITE
        etiquetaResumen.font = Font("SansSerif", Font.BOLD, 13)
        return JPanel(FlowLayout(FlowLayout.LEFT, 12, 8)).apply {
            isOpaque = false
            add(fabrica.crearBotonCompacto("Menú Principal") {
                acciones.alMostrarVista(AccionesJuego.VISTA_MENU_PRINCIPAL)
            })
            add(fabrica.crearBotonCompacto("Configuración") {
                acciones.alMostrarVista(AccionesJuego.VISTA_CONFIGURACION)
            })
            add(fabrica.crearBotonCompacto("Repetir Disparo") { acciones.alIniciarJuego() })
            add(fabrica.crearBotonCompacto("Finalizar") { acciones.alFinalizarSimulacion() })
            add(etiquetaResumen)
        }
    }
}

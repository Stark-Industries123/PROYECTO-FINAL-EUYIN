package ui

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
    private val fabrica: FabricaComponentesGui,
    panelTrayectoria: PanelTrayectoria,
    private val acciones: AccionesJuego
) : JPanel(BorderLayout()) {

    private val etiquetaResumen = JLabel("Listo para jugar.")

    init {
        add(panelTrayectoria, BorderLayout.CENTER)
        panelTrayectoria.layout = BorderLayout()
        panelTrayectoria.add(construirBarraSuperior(), BorderLayout.NORTH)
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

    private fun construirBarraSuperior(): JPanel {
        etiquetaResumen.foreground = Color.WHITE
        etiquetaResumen.font = Font("SansSerif", Font.BOLD, 13)
        return JPanel(FlowLayout(FlowLayout.LEFT, 12, 8)).apply {
            isOpaque = false
            add(fabrica.crearBotonCompacto("Menú Principal") {
                acciones.alMostrarVista(GestorVistasGui.VISTA_MENU_PRINCIPAL)
            })
            add(fabrica.crearBotonCompacto("Configuración") {
                acciones.alMostrarVista(GestorVistasGui.VISTA_CONFIGURACION)
            })
            add(fabrica.crearBotonCompacto("Repetir Disparo") { acciones.alIniciarJuego() })
            add(fabrica.crearBotonCompacto("Finalizar") { acciones.alFinalizarSimulacion() })
            add(etiquetaResumen)
        }
    }
}

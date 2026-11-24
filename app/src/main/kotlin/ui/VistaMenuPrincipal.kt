package ui

import java.awt.GridBagLayout
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JPanel

/**
 * Pantalla del menú principal del simulador 2D.
 *
 * Responsabilidad única (SRP): construir y disponer los elementos visuales del menú de inicio
 * delegando las acciones de navegación a [AccionesJuego].
 */
class VistaMenuPrincipal(
    private val fabrica: FabricaComponentesGui,
    private val acciones: AccionesJuego
) : JPanel(GridBagLayout()) {

    init {
        background = fabrica.colorFondoOscuro
        add(crearTarjetaCentral())
    }

    private fun crearTarjetaCentral(): JPanel {
        val btnJugar = fabrica.crearBotonPrimario("JUGAR") { acciones.alIniciarJuego() }
        val btnConfig = fabrica.crearBotonSecundario("CONFIGURACIÓN") {
            acciones.alMostrarVista(GestorVistasGui.VISTA_CONFIGURACION)
        }
        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            isOpaque = false
            add(fabrica.crearEtiquetaTitulo("JUEGUITO DEL PROYECTIL", 44))
            add(Box.createVerticalStrut(12))
            add(fabrica.crearEtiquetaSubtitulo("Stark Industries 123 presenta..."))
            add(Box.createVerticalStrut(48))
            add(btnJugar)
            add(Box.createVerticalStrut(20))
            add(btnConfig)
        }
    }
}

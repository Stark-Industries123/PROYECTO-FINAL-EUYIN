package ui

import java.awt.GridBagLayout
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JPanel

/**
 * Vista Swing del menú principal del simulador 2D de tiro parabólico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Se encarga exclusivamente de construir y presentar
 *   la pantalla de bienvenida con sus accesos de navegación hacia el juego y la configuración.
 */
class MenuPrincipalUI(
    private val estilo: EstiloUI = EstiloUI(),
    private val alJugar: () -> Unit = {},
    private val alAbrirConfiguracion: () -> Unit = {}
) : JPanel(GridBagLayout()) {

    init {
        construirVista()
    }

    private fun construirVista() {
        background = estilo.colorFondo
        add(crearTarjetaCentral())
    }

    private fun crearTarjetaCentral(): JPanel {
        val btnJugar = estilo.crearBotonPrimario("JUGAR") { alJugar() }
        val btnConfig = estilo.crearBotonSecundario("CONFIGURACIÓN") { alAbrirConfiguracion() }
        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            isOpaque = false
            add(estilo.crearEtiquetaTitulo("JUEGUITO DEL PROYECTIL", 44))
            add(Box.createVerticalStrut(12))
            add(estilo.crearEtiquetaSubtitulo("Stark Industries 123 presenta..."))
            add(Box.createVerticalStrut(48))
            add(btnJugar)
            add(Box.createVerticalStrut(20))
            add(btnConfig)
        }
    }
}

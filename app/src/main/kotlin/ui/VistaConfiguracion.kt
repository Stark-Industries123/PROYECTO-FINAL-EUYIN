package ui

import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

/**
 * Pantalla de configuración de misión del simulador 2D.
 *
 * Responsabilidad única (SRP): organizar el encabezado, el formulario de parámetros físicos,
 * la brújula interactiva de viento y los botones de navegación inferiores.
 */
class VistaConfiguracion(
    private val fabrica: FabricaComponentesGui,
    private val formulario: FormularioParametros,
    private val acciones: AccionesJuego
) : JPanel(BorderLayout(16, 16)) {

    init {
        background = fabrica.colorFondoOscuro
        border = BorderFactory.createEmptyBorder(24, 36, 24, 36)
        formulario.registrarCambioPlaneta { indice -> acciones.alCambiarPlaneta(indice) }
        add(crearEncabezado(), BorderLayout.NORTH)
        add(crearCuerpo(), BorderLayout.CENTER)
        add(crearPie(), BorderLayout.SOUTH)
    }

    private fun crearEncabezado(): JLabel {
        return fabrica.crearEtiquetaTitulo("CONFIGURACIÓN DE MISIÓN", 28).apply {
            horizontalAlignment = SwingConstants.CENTER
        }
    }

    private fun crearCuerpo(): JPanel {
        return JPanel(GridBagLayout()).apply {
            isOpaque = false
            val gbc = GridBagConstraints().apply {
                insets = Insets(8, 12, 8, 12)
                fill = GridBagConstraints.BOTH
                weighty = 1.0
            }
            gbc.gridx = 0
            gbc.weightx = 0.55
            add(formulario.construirPanelCampos(fabrica), gbc)
            gbc.gridx = 1
            gbc.weightx = 0.45
            add(crearContenedorBrujula(), gbc)
        }
    }

    private fun crearContenedorBrujula(): JPanel {
        return JPanel(BorderLayout(8, 8)).apply {
            background = fabrica.colorPanelInterno
            border = BorderFactory.createEmptyBorder(16, 16, 16, 16)
            val titulo = fabrica.crearEtiquetaCampo("Dirección del Viento (Clic o arrastrar):").apply {
                horizontalAlignment = SwingConstants.CENTER
            }
            add(titulo, BorderLayout.NORTH)
            add(formulario.panelBrujula, BorderLayout.CENTER)
        }
    }

    private fun crearPie(): JPanel {
        val btnVolver = fabrica.crearBotonSecundario("VOLVER AL MENÚ", 220, 48) {
            acciones.alMostrarVista(GestorVistasGui.VISTA_MENU_PRINCIPAL)
        }
        val btnJugar = fabrica.crearBotonPrimario("GUARDAR Y JUGAR", 220, 48) {
            acciones.alIniciarJuego()
        }
        return JPanel(FlowLayout(FlowLayout.CENTER, 24, 8)).apply {
            isOpaque = false
            add(btnVolver)
            add(btnJugar)
        }
    }
}

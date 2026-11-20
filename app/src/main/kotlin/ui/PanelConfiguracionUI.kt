package ui

import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.BorderFactory
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingConstants

/**
 * Vista Swing encargada del formulario de configuración de misión: selección de planeta,
 * parámetros cinemáticos y selector interactivo de dirección del viento.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Gestiona únicamente la presentación y captura
 *   de parámetros de entrada del usuario sin conocer reglas físicas ni navegación global.
 */
class PanelConfiguracionUI(
    private val estilo: EstiloUI = EstiloUI(),
    private val alVolverMenu: () -> Unit = {},
    private val alGuardarYJugar: () -> Unit = {},
    private val alCambiarPlaneta: (String) -> Unit = {}
) : JPanel(BorderLayout(16, 16)) {

    private val comboPlaneta = JComboBox(NOMBRES_PLANETAS)
    private val campoVelocidad = JTextField("35.0", 10)
    private val campoAngulo = JTextField("45.0", 10)
    private val campoMasa = JTextField("1.0", 10)
    private val campoVelocidadViento = JTextField("10.0", 10)
    private val panelBrujula = PanelBrujulaViento(anguloInicial = 0.0)

    init {
        construirVista()
    }

    private fun construirVista() {
        background = estilo.colorFondo
        border = BorderFactory.createEmptyBorder(24, 36, 24, 36)
        comboPlaneta.addActionListener { alCambiarPlaneta((comboPlaneta.selectedIndex + 1).toString()) }
        val encabezado = estilo.crearEtiquetaTitulo("CONFIGURACIÓN DE MISIÓN", 28).apply {
            horizontalAlignment = SwingConstants.CENTER
        }
        val cuerpo = JPanel(GridBagLayout()).apply {
            isOpaque = false
            val gbc = GridBagConstraints().apply {
                insets = Insets(8, 12, 8, 12); fill = GridBagConstraints.BOTH; weighty = 1.0
            }
            gbc.gridx = 0; gbc.weightx = 0.55; add(crearPanelFormulario(), gbc)
            gbc.gridx = 1; gbc.weightx = 0.45; add(crearContenedorBrujula(), gbc)
        }
        val pie = JPanel(FlowLayout(FlowLayout.CENTER, 24, 8)).apply {
            isOpaque = false
            add(estilo.crearBotonSecundario("VOLVER AL MENÚ", 220, 48) { alVolverMenu() })
            add(estilo.crearBotonPrimario("GUARDAR Y JUGAR", 220, 48) { alGuardarYJugar() })
        }
        add(encabezado, BorderLayout.NORTH); add(cuerpo, BorderLayout.CENTER); add(pie, BorderLayout.SOUTH)
    }

    private fun crearPanelFormulario(): JPanel {
        listOf(campoVelocidad, campoAngulo, campoMasa, campoVelocidadViento).forEach { estilo.estilizarCampoTexto(it) }
        estilo.estilizarCombo(comboPlaneta)
        val filas = listOf(
            "Planeta / Gravedad:" to comboPlaneta,
            "Velocidad inicial (m/s):" to campoVelocidad,
            "Ángulo de disparo (0° - 90°):" to campoAngulo,
            "Masa del proyectil (kg):" to campoMasa,
            "Velocidad del viento (km/h):" to campoVelocidadViento
        )
        return JPanel(GridBagLayout()).apply {
            background = estilo.colorPanelInterno
            border = BorderFactory.createEmptyBorder(20, 24, 20, 24)
            filas.forEachIndexed { indice, (etiqueta, componente) ->
                val gbcEtiqueta = GridBagConstraints().apply {
                    gridx = 0; gridy = indice; anchor = GridBagConstraints.WEST
                    insets = Insets(10, 6, 10, 12); weightx = 0.45
                }
                val gbcCampo = GridBagConstraints().apply {
                    gridx = 1; gridy = indice; fill = GridBagConstraints.HORIZONTAL
                    insets = Insets(10, 6, 10, 6); weightx = 0.55
                }
                add(estilo.crearEtiquetaCampo(etiqueta), gbcEtiqueta)
                add(componente, gbcCampo)
            }
        }
    }

    private fun crearContenedorBrujula(): JPanel {
        return JPanel(BorderLayout(8, 8)).apply {
            background = estilo.colorPanelInterno
            border = BorderFactory.createEmptyBorder(16, 16, 16, 16)
            val titulo = estilo.crearEtiquetaCampo("Dirección del Viento (Clic o arrastrar):").apply {
                horizontalAlignment = SwingConstants.CENTER
            }
            add(titulo, BorderLayout.NORTH)
            add(panelBrujula, BorderLayout.CENTER)
        }
    }

    /**
     * Lee los valores actuales del formulario y los empaqueta en [DatosFormularioSimulacion].
     */
    fun obtenerDatos(): DatosFormularioSimulacion {
        return DatosFormularioSimulacion(
            opcionPlaneta = (comboPlaneta.selectedIndex + 1).toString(),
            velocidadTexto = campoVelocidad.text,
            anguloTexto = campoAngulo.text,
            masaTexto = campoMasa.text,
            vientoTexto = campoVelocidadViento.text,
            direccionVientoGrados = panelBrujula.obtenerAnguloGrados()
        )
    }

    /**
     * Carga programáticamente los valores de [datos] en los campos del formulario.
     */
    fun cargarDatos(datos: DatosFormularioSimulacion) {
        val indice = datos.opcionPlanetaNormalizada().toIntOrNull()?.minus(1)
        if (indice != null && indice in NOMBRES_PLANETAS.indices) {
            comboPlaneta.selectedIndex = indice
        }
        campoVelocidad.text = datos.velocidadTexto
        campoAngulo.text = datos.anguloTexto
        campoMasa.text = datos.masaTexto
        campoVelocidadViento.text = datos.vientoTexto
        panelBrujula.establecerAnguloGrados(datos.direccionVientoGrados)
    }

    /**
     * Selecciona en el desplegable el planeta cuyo nombre coincida con [nombrePlaneta].
     */
    fun seleccionarPlaneta(nombrePlaneta: String) {
        val indice = NOMBRES_PLANETAS.indexOfFirst { it.equals(nombrePlaneta, ignoreCase = true) }
        if (indice >= 0 && comboPlaneta.selectedIndex != indice) {
            comboPlaneta.selectedIndex = indice
        }
    }

    companion object {
        private val NOMBRES_PLANETAS = arrayOf("Tierra", "Luna", "Mercurio", "Júpiter")
    }
}

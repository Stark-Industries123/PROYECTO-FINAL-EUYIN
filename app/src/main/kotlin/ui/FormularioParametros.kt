package ui

import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.BorderFactory
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

/**
 * Encapsula los controles de entrada de la pantalla de configuración (planeta, parámetros
 * numéricos y brújula de viento), gestionando su armado visual y lectura/escritura de valores.
 */
class FormularioParametros {

    private val comboPlaneta = JComboBox(NOMBRES_PLANETAS)
    private val camposTexto: Map<String, JTextField> = mapOf(
        CLAVE_VELOCIDAD to JTextField("35.0", 10),
        CLAVE_ANGULO to JTextField("45.0", 10),
        CLAVE_MASA to JTextField("1.0", 10),
        CLAVE_VIENTO to JTextField("10.0", 10)
    )
    val panelBrujula: PanelBrujulaViento = PanelBrujulaViento(anguloInicial = 0.0)

    /**
     * Construye y estiliza el panel del formulario de parámetros físicos.
     */
    fun construirPanelCampos(fabrica: FabricaComponentesGui): JPanel {
        camposTexto.values.forEach { fabrica.estilizarCampoTexto(it) }
        comboPlaneta.font = fabrica.crearFuenteCampo()
        val filas: List<Pair<String, JComponent>> = listOf(
            "Planeta / Gravedad:" to comboPlaneta,
            "Velocidad inicial (m/s):" to camposTexto.getValue(CLAVE_VELOCIDAD),
            "Ángulo de disparo (0° - 90°):" to camposTexto.getValue(CLAVE_ANGULO),
            "Masa del proyectil (kg):" to camposTexto.getValue(CLAVE_MASA),
            "Velocidad del viento (km/h):" to camposTexto.getValue(CLAVE_VIENTO)
        )
        return JPanel(GridBagLayout()).apply {
            background = fabrica.colorPanelInterno
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
                add(fabrica.crearEtiquetaCampo(etiqueta), gbcEtiqueta)
                add(componente, gbcCampo)
            }
        }
    }

    /**
     * Registra un listener para responder a cambios en el selector de planeta.
     */
    fun registrarCambioPlaneta(accion: (Int) -> Unit) {
        comboPlaneta.addActionListener { accion(comboPlaneta.selectedIndex) }
    }

    /**
     * Selecciona en el desplegable el planeta cuyo nombre coincide con [nombrePlaneta].
     */
    fun seleccionarPlaneta(nombrePlaneta: String) {
        val indice = NOMBRES_PLANETAS.indexOfFirst { it.equals(nombrePlaneta, ignoreCase = true) }
        if (indice >= 0) {
            comboPlaneta.selectedIndex = indice
        }
    }

    /**
     * Actualiza los valores de texto del formulario y opcionalmente el ángulo de la brújula.
     */
    fun actualizarEntradas(
        valores: Map<String, String>,
        direccionVientoGrados: Double = panelBrujula.obtenerAnguloGrados()
    ) {
        valores.forEach { (clave, texto) ->
            camposTexto[clave]?.text = texto
        }
        panelBrujula.establecerAnguloGrados(direccionVientoGrados)
    }

    /**
     * Extrae todos los valores crudos actualmente cargados en el formulario y la brújula.
     */
    fun extraerDatosCrudos(): Map<String, String> {
        val datos = mutableMapOf<String, String>()
        camposTexto.forEach { (clave, campo) -> datos[clave] = campo.text }
        datos[CLAVE_PLANETA] = (comboPlaneta.selectedIndex + 1).toString()
        datos[CLAVE_DIRECCION] = panelBrujula.obtenerAnguloGrados().toString()
        return datos
    }

    companion object {
        const val CLAVE_VELOCIDAD: String = "velocidad"
        const val CLAVE_ANGULO: String = "angulo"
        const val CLAVE_MASA: String = "masa"
        const val CLAVE_VIENTO: String = "viento"
        const val CLAVE_PLANETA: String = "planeta"
        const val CLAVE_DIRECCION: String = "direccion"
        private val NOMBRES_PLANETAS = arrayOf("Tierra", "Luna", "Mercurio", "Júpiter")
    }
}

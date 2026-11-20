package ui

import fisicas.motor.PuntoTrayectoria
import java.awt.CardLayout
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.SwingUtilities

/**
 * Ventana principal Swing de la aplicación 2D que actúa como contenedor de vistas ([CardLayout])
 * e implementa [VistaSimulacion].
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Gestiona exclusivamente el marco de ventana, la
 *   navegación entre pantallas ([MenuPrincipalUI], [PanelConfiguracionUI], [PanelJuegoUI]) y
 *   los diálogos modales, delegando la lógica de negocio y eventos a [SimulacionController].
 * - Bajo Acoplamiento (CBO) y Alta Cohesión: No conoce detalles del motor físico, validadores
 *   ni formateadores de estadísticas.
 */
class VentanaSimulacion(
    proveedorControlador: (VistaSimulacion) -> SimulacionController = { SimulacionController(it) }
) : JFrame("Simulador 2D de Tiro Parabólico"), VistaSimulacion {

    private val cardLayout = CardLayout()
    private val contenedorVistas = JPanel(cardLayout)
    private val controlador: SimulacionController
    private val panelMenu = MenuPrincipalUI(
        alJugar = { controlador.alIniciarJuego() },
        alAbrirConfiguracion = { controlador.alAbrirConfiguracion() }
    )
    private val panelConfiguracion = PanelConfiguracionUI(
        alVolverMenu = { controlador.alAbrirMenuPrincipal() },
        alGuardarYJugar = { controlador.alIniciarJuego() },
        alCambiarPlaneta = { controlador.alCambiarPlaneta(it) }
    )
    private val panelJuego = PanelJuegoUI()

    init {
        controlador = proveedorControlador(this)
        inicializarVentana()
    }

    private fun inicializarVentana() {
        defaultCloseOperation = EXIT_ON_CLOSE
        preferredSize = Dimension(ANCHO_VENTANA, ALTO_VENTANA)
        minimumSize = Dimension(ANCHO_VENTANA, ALTO_VENTANA)
        setSize(ANCHO_VENTANA, ALTO_VENTANA)
        contentPane = contenedorVistas
        setLocationRelativeTo(null)

        panelJuego.establecerAlFinalizarAnimacion { controlador.alCompletarAnimacion() }
        panelJuego.configurarAcciones(
            alMenu = { controlador.alAbrirMenuPrincipal() },
            alConfig = { controlador.alAbrirConfiguracion() },
            alRelanzar = { controlador.alIniciarJuego() },
            alFinalizar = { controlador.alFinalizarSimulacion() }
        )

        contenedorVistas.add(panelMenu, VISTA_MENU_PRINCIPAL)
        contenedorVistas.add(panelConfiguracion, VISTA_CONFIGURACION)
        contenedorVistas.add(panelJuego, VISTA_JUEGO)
        pack()
        controlador.inicializarVista()
    }

    override fun mostrarVista(nombreVista: String) {
        if (nombreVista != VISTA_JUEGO) {
            panelJuego.detenerAnimacion()
        }
        cardLayout.show(contenedorVistas, nombreVista)
    }

    override fun leerDatosFormulario(): DatosFormularioSimulacion = panelConfiguracion.obtenerDatos()

    override fun aplicarPlanetaVisual(nombrePlaneta: String) {
        panelConfiguracion.seleccionarPlaneta(nombrePlaneta)
        panelJuego.aplicarPlaneta(nombrePlaneta)
    }

    override fun mostrarTrayectoria(trayectoria: List<PuntoTrayectoria>, resumenHud: String) {
        panelJuego.mostrarTrayectoria(trayectoria, resumenHud)
    }

    override fun completarAnimacionActual() {
        panelJuego.completarAnimacion()
    }

    override fun mostrarError(mensaje: String) {
        panelJuego.mostrarMensajeResumen(mensaje)
        if (!GraphicsEnvironment.isHeadless() && isShowing) {
            JOptionPane.showMessageDialog(panelJuego, mensaje, "Error de validación", JOptionPane.ERROR_MESSAGE)
        }
    }

    override fun mostrarEstadisticas(mensaje: String) {
        if (!GraphicsEnvironment.isHeadless() && isShowing) {
            JOptionPane.showMessageDialog(
                panelJuego,
                mensaje,
                "Estadísticas Finales de la Misión",
                JOptionPane.INFORMATION_MESSAGE
            )
        }
    }

    /**
     * Carga programáticamente los datos del formulario de configuración.
     */
    fun ingresarDatos(datos: DatosFormularioSimulacion) {
        panelConfiguracion.cargarDatos(datos)
    }

    /**
     * Ejecuta la simulación con los datos actualmente cargados en el formulario.
     */
    fun ejecutarSimulacion(): Boolean = controlador.ejecutarSimulacion()

    /**
     * Retorna el mensaje de resumen o error actualmente visible en el HUD de juego.
     */
    fun obtenerTextoResumen(): String = panelJuego.obtenerResumen()

    /**
     * Retorna los puntos de la trayectoria cargados en el panel de juego.
     */
    fun obtenerPuntosGraficados(): List<PuntoTrayectoria> = panelJuego.obtenerPuntos()

    companion object {
        const val ANCHO_VENTANA: Int = 900
        const val ALTO_VENTANA: Int = 650
        const val VISTA_MENU_PRINCIPAL: String = SimulacionController.VISTA_MENU_PRINCIPAL
        const val VISTA_CONFIGURACION: String = SimulacionController.VISTA_CONFIGURACION
        const val VISTA_JUEGO: String = SimulacionController.VISTA_JUEGO

        @JvmStatic
        fun main(args: Array<String> = emptyArray()) {
            SwingUtilities.invokeLater {
                VentanaSimulacion().isVisible = true
            }
        }
    }
}

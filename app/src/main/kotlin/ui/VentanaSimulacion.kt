package ui

import config.ConfigSimulacion
import fisicas.gravedad.FuenteGravedad
import fisicas.gravedad.Tierra
import fisicas.motor.CalculadorTrayectoria
import fisicas.motor.PuntoTrayectoria
import fisicas.motor.ResultadoSimulacion
import fisicas.motor.Simulador
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.SwingUtilities

/**
 * Ventana principal interactiva tipo videojuego 2D en Java Swing para la simulación física.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Configura el [JFrame] raíz y expone la fachada
 *   pública de la ventana, delegando la construcción de vistas a [GestorVistasGui], la
 *   validación a [ValidadorFormularioGui], las estadísticas a [DialogoEstadisticas] y la
 *   orquestación de eventos a [ControladorSimulacionGui].
 * - Dependency Inversion Principle (DIP): Depende de abstracciones ([CalculadorTrayectoria],
 *   [FuenteGravedad], [ValidadorEntradaContract]) inyectadas por constructor.
 */
class VentanaSimulacion(
    calculador: CalculadorTrayectoria = Simulador(pasoTiempoMuestreo = 0.016),
    fuenteGravedad: FuenteGravedad = Tierra(),
    validador: ValidadorEntradaContract = ValidadorEntrada(),
    panelTrayectoria: PanelTrayectoria = PanelTrayectoria()
) : JFrame("Simulador 2D de Tiro Parabólico") {

    private val controlador: ControladorSimulacionGui = ControladorSimulacionGui(
        calculador = calculador,
        validadorFormulario = ValidadorFormularioGui(validador, FormularioParametros(), fuenteGravedad),
        dialogoEstadisticas = DialogoEstadisticas(this),
        gestorVistas = GestorVistasGui(panelTrayectoria)
    )

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        preferredSize = Dimension(ANCHO_VENTANA, ALTO_VENTANA)
        minimumSize = Dimension(ANCHO_VENTANA, ALTO_VENTANA)
        setSize(ANCHO_VENTANA, ALTO_VENTANA)
        contentPane = controlador.gestorVistas.contenedorVistas
        setLocationRelativeTo(null)
        controlador.inicializar()
        pack()
    }

    /**
     * Cambia la vista activa dentro del contenedor principal.
     */
    fun mostrarVista(nombreVista: String) {
        controlador.alMostrarVista(nombreVista)
    }

    /**
     * Salta la animación de la trayectoria en curso, renderizando todos los puntos de forma
     * instantánea y abriendo las estadísticas post-partida.
     */
    fun finalizarSimulacion() {
        controlador.alFinalizarSimulacion()
    }

    /**
     * Establece los valores de texto en los campos de entrada principales de configuración.
     */
    fun ingresarDatos(
        velocidadTexto: String,
        anguloTexto: String,
        masaTexto: String = "1.0",
        vientoTexto: String = "10.0"
    ) {
        controlador.validadorFormulario.formulario.actualizarEntradas(
            mapOf(
                FormularioParametros.CLAVE_VELOCIDAD to velocidadTexto,
                FormularioParametros.CLAVE_ANGULO to anguloTexto,
                FormularioParametros.CLAVE_MASA to masaTexto,
                FormularioParametros.CLAVE_VIENTO to vientoTexto
            )
        )
    }

    /**
     * Actualiza la dirección del viento en grados dentro de la brújula interactiva.
     */
    fun establecerDireccionViento(direccionVientoGrados: Double) {
        controlador.validadorFormulario.formulario.panelBrujula.establecerAnguloGrados(direccionVientoGrados)
    }

    /**
     * Retorna el mensaje de resumen o error actualmente visible en el HUD de juego.
     */
    fun obtenerTextoResumen(): String = controlador.gestorVistas.obtenerTextoResumen()

    /**
     * Retorna los puntos de la trayectoria cargados en el panel de juego.
     */
    fun obtenerPuntosGraficados(): List<PuntoTrayectoria> {
        return controlador.gestorVistas.panelTrayectoria.obtenerPuntos()
    }

    /**
     * Valida los datos configurados por el usuario, invoca al motor físico y lanza la animación.
     */
    fun ejecutarSimulacion(): Boolean = controlador.ejecutarSimulacion()

    /**
     * Construye el listado de resultados finales a partir de [config] y [resultado].
     */
    fun construirTextoEstadisticas(config: ConfigSimulacion, resultado: ResultadoSimulacion): String {
        return controlador.dialogoEstadisticas.construirTextoEstadisticas(config, resultado)
    }

    companion object {
        const val ANCHO_VENTANA: Int = 900
        const val ALTO_VENTANA: Int = 650
        const val VISTA_MENU_PRINCIPAL: String = GestorVistasGui.VISTA_MENU_PRINCIPAL
        const val VISTA_CONFIGURACION: String = GestorVistasGui.VISTA_CONFIGURACION
        const val VISTA_JUEGO: String = GestorVistasGui.VISTA_JUEGO

        @JvmStatic
        fun main(args: Array<String> = emptyArray()) {
            SwingUtilities.invokeLater {
                VentanaSimulacion().isVisible = true
            }
        }
    }
}

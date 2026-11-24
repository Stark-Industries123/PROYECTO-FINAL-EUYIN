package ui

import config.ConfigSimulacion
import fisicas.motor.PuntoTrayectoria
import fisicas.motor.ResultadoSimulacion
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.SwingUtilities

/**
 * Ventana principal interactiva tipo videojuego 2D en Java Swing para la simulación física.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Configura el [JFrame] raíz y expone la fachada
 *   pública de la ventana, delegando la composición a [EnsambladorVentanaGui] y toda la
 *   coordinación de vistas y simulación a la abstracción [ControladorVentanaGui].
 * - Dependency Inversion Principle (DIP): Depende de la abstracción [ControladorVentanaGui]
 *   sin instanciar motores físicos, validadores ni paneles internos concretos.
 */
class VentanaSimulacion(
    ensamblador: EnsambladorVentanaGui = EnsambladorVentanaGui()
) : JFrame("Simulador 2D de Tiro Parabólico") {

    private val controlador: ControladorVentanaGui = ensamblador.ensamblar(this)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        preferredSize = Dimension(ANCHO_VENTANA, ALTO_VENTANA)
        minimumSize = Dimension(ANCHO_VENTANA, ALTO_VENTANA)
        setSize(ANCHO_VENTANA, ALTO_VENTANA)
        contentPane = controlador.obtenerContenedor()
        setLocationRelativeTo(null)
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
        controlador.actualizarEntradas(
            mapOf(
                CLAVE_VELOCIDAD to velocidadTexto,
                CLAVE_ANGULO to anguloTexto,
                CLAVE_MASA to masaTexto,
                CLAVE_VIENTO to vientoTexto
            )
        )
    }

    /**
     * Actualiza la dirección del viento en grados dentro de la brújula interactiva.
     */
    fun establecerDireccionViento(direccionVientoGrados: Double) {
        controlador.actualizarEntradas(emptyMap(), direccionVientoGrados)
    }

    /**
     * Retorna el mensaje de resumen o error actualmente visible en el HUD de juego.
     */
    fun obtenerTextoResumen(): String = controlador.obtenerTextoResumen()

    /**
     * Retorna los puntos de la trayectoria cargados en el panel de juego.
     */
    fun obtenerPuntosGraficados(): List<PuntoTrayectoria> = controlador.obtenerPuntosGraficados()

    /**
     * Valida los datos configurados por el usuario, invoca al motor físico y lanza la animación.
     */
    fun ejecutarSimulacion(): Boolean = controlador.ejecutarSimulacion()

    /**
     * Construye el listado de resultados finales a partir de [config] y [resultado].
     */
    fun construirTextoEstadisticas(config: ConfigSimulacion, resultado: ResultadoSimulacion): String {
        return controlador.construirTextoEstadisticas(config, resultado)
    }

    companion object {
        const val ANCHO_VENTANA: Int = 900
        const val ALTO_VENTANA: Int = 650
        const val VISTA_MENU_PRINCIPAL: String = "MenuPrincipal"
        const val VISTA_CONFIGURACION: String = "Configuracion"
        const val VISTA_JUEGO: String = "Juego"
        private const val CLAVE_VELOCIDAD: String = "velocidad"
        private const val CLAVE_ANGULO: String = "angulo"
        private const val CLAVE_MASA: String = "masa"
        private const val CLAVE_VIENTO: String = "viento"

        @JvmStatic
        fun main(args: Array<String> = emptyArray()) {
            SwingUtilities.invokeLater {
                VentanaSimulacion().isVisible = true
            }
        }
    }
}
